/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.web;

import fakebook.business.PostServiceFacadeLocal;
import fakebook.business.UserServiceFacadeLocal;
import fakebook.business.WallServiceFacadeLocal;
import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

/**
 *
 * @author texus
 */
@WebServlet(name = "Wall", urlPatterns = {"/wall"})
@MultipartConfig
public class WallServlet extends HttpServlet {

    @EJB
    private UserServiceFacadeLocal userService;
    
    @EJB
    private WallServiceFacadeLocal wallService;
    

    private final static Logger LOGGER =
            Logger.getLogger(WallServlet.class.getCanonicalName());
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Long currentUserId = (Long)(request.getSession().getAttribute("currentUser"));
        if (currentUserId == null || userService.getUser(currentUserId) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        long userId = -1;
        if (request.getParameter("uid") != null) {
            try {
                userId = Long.decode(request.getParameter("uid"));
                if (userService.getUser(userId) == null) {
                    userId = -1;
                }
            }
            catch (NumberFormatException e) {
            }
        }
        else if (userId == -1) {
            userId = currentUserId;
        }

        request.setAttribute("friends", false);
        request.setAttribute("user", userId);

        if (userId != -1) {
            User currentUser = userService.getUser(currentUserId);
            User user = userService.getUser(userId);

            // Check if the user is allowed to watch this wall
            if (currentUserId.equals(user.getId()) || currentUser.getFriends().contains(user)) {
                request.setAttribute("friends", true);
                
                if(request.getMethod().equals("POST")) {
                    // Check for new wall post form
                    if(request.getParameter("new_wall_post") != null) {
                        String newPost = request.getParameter("new_wall_post");
                        
                        wallService.addPost(currentUser, user, newPost);

                        //final String path = request.getParameter("destination");
                        String path = "./";
                        final Part filePart = request.getPart("attachment");
                        final String fileName = filePart.getSubmittedFileName();

                        OutputStream out = null;
                        InputStream filecontent = null;
                        final PrintWriter writer = response.getWriter();

                        try {
                            out = new FileOutputStream(new File(path + File.separator
                                    + fileName));
                            filecontent = filePart.getInputStream();

                            int read = 0;
                            final byte[] bytes = new byte[1024];

                            while ((read = filecontent.read(bytes)) != -1) {
                                out.write(bytes, 0, read);
                            }
                            //writer.println("New file " + fileName + " created at " + path);
                            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
                                    new Object[]{fileName, path});
                        } catch (FileNotFoundException fne) {
                            //writer.println("You either did not specify a file to upload or are "
                            //        + "trying to upload a file to a protected or nonexistent "
                            //        + "location.");
                            //writer.println("<br/> ERROR: " + fne.getMessage());

                            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                                    new Object[]{fne.getMessage()});
                        } finally {
                            if (out != null) {
                                out.close();
                            }
                            if (filecontent != null) {
                                filecontent.close();
                            }
                            if (writer != null) {
                                //writer.close();
                            }
                        }
                    }

                    // Check for new comments
                    if ((request.getParameter("new_comment") != null) && (request.getParameter("parent_post_id") != null)) {
                        String newComment = request.getParameter("new_comment");
                        String parentPostId = request.getParameter("parent_post_id");
                        
                        wallService.addComment(currentUser, parentPostId, newComment);
                    }

                    // Check for new likes
                    if (request.getParameter("liked_post_id") != null) {
                        String postId = request.getParameter("liked_post_id");
                        wallService.addLike(currentUser, postId);
                    }
                }

                request.setAttribute("posts", wallService.getWallPosts(userId));
            }

            request.setAttribute("userName", user.getName());
        }

        request.getRequestDispatcher("wall.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Wall Servlet";
    }// </editor-fold>

}

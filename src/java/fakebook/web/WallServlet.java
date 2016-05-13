/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.web;

import fakebook.business.AttachmentServiceFacadeLocal;
import fakebook.business.UserServiceFacadeLocal;
import fakebook.business.WallServiceFacadeLocal;
import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    
    @EJB
    private AttachmentServiceFacadeLocal attachmentService;


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
                        String postContents = request.getParameter("new_wall_post").trim();
                        Post post = new Post(currentUser, user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Date(), postContents);
                        
                        final Part attachment = request.getPart("attachment");
                        if (attachment != null && !attachment.getSubmittedFileName().isEmpty()) {
                            final String attachmentFileName = attachment.getSubmittedFileName();
                            
                            // Only accept specific file types
                            if ((attachmentFileName.endsWith(".png"))
                             || (attachmentFileName.endsWith(".bmp"))
                             || (attachmentFileName.endsWith(".jpg"))
                             || (attachmentFileName.endsWith(".jpeg"))
                             || (attachmentFileName.endsWith(".gif")))
                            {
                                Long id = attachmentService.upload(currentUser, attachment);
                                if (id != null) {
                                    post.setPicture("uploads?id=" + id.toString());
                                }
                                else {
                                    request.setAttribute("error", "Something went wrong when saving the attachment.");
                                }
                            }
                            else if ((attachmentFileName.endsWith(".mp4"))
                                  || (attachmentFileName.endsWith(".ogg"))
                                  || (attachmentFileName.endsWith(".webm")))
                            {
                                Long id = attachmentService.upload(currentUser, attachment);
                                if (id != null) {
                                    post.setVideo("uploads?id=" + id.toString());
                                }
                                else {
                                    request.setAttribute("error", "Something went wrong when saving the attachment.");
                                }
                            }
                            else {
                                request.setAttribute("error", "Attachment had an invalid extension. Only common image and video formats are allowed.");
                            }
                        }

                        // The post can't be empty unless it contained an attachment
                        if (!postContents.isEmpty() || post.getType() != null)
                            wallService.addPost(post);
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

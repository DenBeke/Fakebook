/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.io.IOException;
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

/**
 *
 * @author texus
 */
@WebServlet(name = "Wall", urlPatterns = {"/wall"})
public class WallServlet extends HttpServlet {

    @EJB
    private UserServiceFacadeLocal userService;
    
    @EJB
    private PostServiceFacadeLocal postService;

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
        
        User currentUser = (User)(request.getSession().getAttribute("currentUser"));
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        response.addHeader("Access-Control-Allow-Origin", "*");
        
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
            userId = currentUser.getId();
        }

        request.setAttribute("friends", false);
        request.setAttribute("user", userId);

        if (userId != -1) {
            User user = userService.getUser(userId);

            // Check if the user is allowed to watch this wall
            if (currentUser.getId().equals(user.getId()) || currentUser.getFriends().contains(user)) {
                request.setAttribute("friends", true);
                
                // Check for new wall post form
                if(request.getMethod().equals("POST")) {
                    if(request.getParameter("new_wall_post") != null) {
                        String newPost = request.getParameter("new_wall_post");
                        if (!newPost.trim().isEmpty())
                            postService.newPost(new Post(currentUser, userService.getUser(userId), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Date(), newPost));
                    }
                }

                // Check for new likes
                if (request.getMethod().equals("POST")) {
                    if (request.getParameter("liked_post_id") != null) {
                        String postId = request.getParameter("liked_post_id");
                        if (!postId.trim().isEmpty()) {
                            Post post = postService.getPost(Long.decode(postId));
                            if (post != null) {
                                List<User> likes = post.getLikes();
                                if (!likes.contains(currentUser)) {
                                    likes.add(currentUser);
                                    postService.updatePost(post);
                                }
                            }
                        }
                    }
                }

                // Sort the posts based on their creation time (newest first)
                List<Post> posts = postService.getPostsOnWall(userId);
                Collections.sort(posts, new Comparator<Post>() {
                    public int compare(Post post1, Post post2) {
                        return post2.getTimestamp().compareTo(post1.getTimestamp());
                    }
                });
                request.setAttribute("posts", posts);
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

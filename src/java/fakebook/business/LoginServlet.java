/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType.Metadata;
import com.restfb.types.MessageTag;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post.Comments;
import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author texus
 */
@WebServlet(name = "Login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
        
        String fbToken = request.getParameter("fbToken");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null && fbToken == null) { // Accessing page directly
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        else {
            if (fbToken != null) {
                FacebookClient facebookClient = new DefaultFacebookClient(fbToken, Version.LATEST);
                com.restfb.types.User fbuser = facebookClient.fetchObject("me", com.restfb.types.User.class, Parameter.with("fields","first_name,last_name,email,gender,birthday"));
                
                if (fbuser.getEmail() != null && !fbuser.getEmail().isEmpty()) {
                    email = fbuser.getEmail();
                    
                    // Check if user exists
                    User user = userService.getUserByEmail(email);
                    if (user != null) {

                        // Update user data
                        user.setFbToken(fbToken);
                        if (fbuser.getFirstName() != null && !fbuser.getFirstName().isEmpty())
                            user.setFirstName(fbuser.getFirstName());
                        if (fbuser.getLastName() != null && !fbuser.getLastName().isEmpty())
                            user.setLastName(fbuser.getLastName());
                        if (fbuser.getGender() != null && !fbuser.getGender().isEmpty())
                            user.setGender(fbuser.getGender());
                        if (fbuser.getBirthday() != null && !fbuser.getBirthday().isEmpty())
                            user.setBirthday(fbuser.getBirthday());
                        
                        userService.editUser(user);
                    }
                    else { // New account
                        user = new User(fbuser.getEmail(),
                                        fbToken,
                                        null,
                                        fbuser.getFirstName(),
                                        fbuser.getLastName(),
                                        fbuser.getGender(),
                                        fbuser.getBirthday(),
                                        false);

                        userService.newUser(user);
                    }

                    syncFacebook(facebookClient, user);

                    response.sendRedirect(request.getContextPath() + "/wall?uid=" + user.getId());
                }
                else {
                    request.setAttribute("error", "Facebook did not provide an email address");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
            else { // Non-facebook login
                request.setAttribute("email", email);
                request.setAttribute("password", password);

                if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                    request.setAttribute("error", "Please provide both an email and a password");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }

                // Check if user exists
                User user = userService.getUserByEmail(email);
                if (user != null) {
                    if (password != null && !password.isEmpty() && password.equals(user.getPassword())) {
                        response.sendRedirect(request.getContextPath() + "/wall?uid=" + user.getId());
                    }
                    else {
                        request.setAttribute("error", "Incorrect email or password");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                }
                else { // User did not exist yet
                    request.setAttribute("error", "Incorrect email or password");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
        }
    }
    
    private void syncFacebook(FacebookClient facebookClient, User user)
    {
        // TODO: Also sync post that you created on someone elses wall
        
        List<com.restfb.types.Post> fbposts = facebookClient.fetchConnection("me/feed", com.restfb.types.Post.class,  Parameter.with("fields","message,from,to,comments,created_time")).getData();
        for (com.restfb.types.Post fbpost : fbposts) {
            if (fbpost.getCreatedTime() == null || fbpost.getMessage() == null || fbpost.getMessage().isEmpty()) {
                continue;
            }
            
            // Don't continue if post was already imported
            boolean postAlreadyExists = false;
            List<Post> existingPosts = postService.getPostsOnWall(user.getId());
            for (Post post : existingPosts) {
                if (post.getTimestamp().equals(fbpost.getCreatedTime())) {
                    postAlreadyExists = true;
                    break;
                }
            }
            if (postAlreadyExists) {
                continue;
            }
            
            // Gather the mentions in the post
            List<User> mentions = new ArrayList<>();
            for (NamedFacebookType fbmention : fbpost.getTo()) {
                try {
                    com.restfb.types.User mentionedUser = facebookClient.fetchObject(fbmention.getId(), com.restfb.types.User.class, Parameter.with("fields","email"));
                    if (mentionedUser.getEmail() != null && !mentionedUser.getEmail().isEmpty()) {
                        User existingUser = userService.getUserByEmail(mentionedUser.getEmail());
                        if (existingUser != null) {
                            mentions.add(existingUser);
                        }
                    }
                }
                catch (FacebookOAuthException e) {
                    // id did not belong to a User, so just ignore this mention
                }
            }

            try {
                com.restfb.types.User fbposter = facebookClient.fetchObject(fbpost.getFrom().getId(), com.restfb.types.User.class, Parameter.with("fields","email"));
                User poster = userService.getUserByEmail(fbposter.getEmail());
                if (poster != null) {
                    // Add the comments
                    List<Post> comments = new ArrayList<>();
                    Comments fbcomments = fbpost.getComments();
                    if (fbcomments != null) {
                        for (Comment fbcomment : fbcomments.getData()) {
                            try {
                                fbposter = facebookClient.fetchObject(fbcomment.getFrom().getId(), com.restfb.types.User.class, Parameter.with("fields","email"));
                                poster = userService.getUserByEmail(fbposter.getEmail());
                                if (poster != null && fbcomment.getMessage() != null && !fbcomment.getMessage().isEmpty()) {
                                    Post comment = new Post(poster, null, null, null, fbcomment.getCreatedTime(), fbcomment.getMessage());
                                    comments.add(comment);
                                    postService.newPost(comment);
                                }
                            }
                            catch (FacebookOAuthException e) {
                                // poster is not a User (could e.g. be a Page), so ignore the post
                            }
                        }
                    }
                    
                    Post post = new Post(poster, user, mentions, comments, fbpost.getCreatedTime(), fbpost.getMessage());
                    postService.newPost(post);
                }
            }
            catch (FacebookOAuthException e) {
                // poster is not a User (could e.g. be a Page), so ignore the post
            }
        }
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
        return "Login Servlet";
    }// </editor-fold>

}

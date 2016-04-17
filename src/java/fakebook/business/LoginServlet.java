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
                com.restfb.types.User fbuser = facebookClient.fetchObject("me", com.restfb.types.User.class, Parameter.with("fields","id,first_name,last_name,email,gender,birthday,picture"));
                
                if (fbuser.getEmail() != null && !fbuser.getEmail().isEmpty()) {
                    email = fbuser.getEmail();
                    
                    String profilePic = null;
                    if (fbuser.getPicture() != null)
                        profilePic = fbuser.getPicture().getUrl();

                    // Check if user exists
                    boolean firstFacebookLogin;
                    User user = userService.getUserByEmail(email);
                    if (user != null) {
                        
                        if (user.getFbId() != null) {
                            firstFacebookLogin = false;
                        }
                        else {
                            firstFacebookLogin = true;
                            user.setFbId(fbuser.getId());
                        }

                        // Update user data
                        if (fbuser.getFirstName() != null && !fbuser.getFirstName().isEmpty())
                            user.setFirstName(fbuser.getFirstName());
                        if (fbuser.getLastName() != null && !fbuser.getLastName().isEmpty())
                            user.setLastName(fbuser.getLastName());
                        if (fbuser.getGender() != null && !fbuser.getGender().isEmpty())
                            user.setGender(fbuser.getGender());
                        if (fbuser.getBirthday() != null && !fbuser.getBirthday().isEmpty())
                            user.setBirthday(fbuser.getBirthday());
                        if (profilePic != null && !fbuser.getPicture().getIsSilhouette())
                            user.setProfilePic(profilePic);
                        
                        userService.updateUser(user);
                    }
                    else { // New account
                        firstFacebookLogin = true;
                        
                        user = new User(fbuser.getEmail(),
                                        fbuser.getId(),
                                        null,
                                        fbuser.getFirstName(),
                                        fbuser.getLastName(),
                                        fbuser.getGender(),
                                        fbuser.getBirthday(),
                                        false,
                                        profilePic);

                        userService.newUser(user);
                    }

                    syncFacebook(facebookClient, user);

                    // If this is the first login then sync the friends
                    if (firstFacebookLogin) {
                        // TODO
                    }

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
        List<com.restfb.types.Post> fbposts = facebookClient.fetchConnection("me/feed", com.restfb.types.Post.class, Parameter.with("fields","message,from,to,comments,created_time")).getData();
        for (com.restfb.types.Post fbpost : fbposts) {
            if (fbpost.getCreatedTime() == null || fbpost.getMessage() == null || fbpost.getMessage().isEmpty()) {
                continue;
            }

            User poster = userService.getUserByFacebookId(fbpost.getFrom().getId());
            if (poster != null) {
                
                // Check if we already have the post (there could still be new comments even if we do)
                Post existingPost = null;
                List<Post> comments = new ArrayList<>();
                for (Post post : postService.getPostsOnWall(user.getId())) {
                    if (post.getTimestamp().equals(fbpost.getCreatedTime())) {
                        existingPost = post;
                        comments = existingPost.getComments();
                        break;
                    }
                }
                
                // Gather the comments
                Comments fbcomments = fbpost.getComments();
                if (fbcomments != null) {
                    for (Comment fbcommentData : fbcomments.getData()) {
                        com.restfb.types.Post fbcomment = facebookClient.fetchObject(fbcommentData.getId(), com.restfb.types.Post.class, Parameter.with("fields","message,from,comments,created_time"));
                        poster = userService.getUserByFacebookId(fbcomment.getFrom().getId());
                        if (poster != null && fbcomment.getMessage() != null && !fbcomment.getMessage().isEmpty()) {
                            
                            // Check if we already have the comment (there could still be new subcomments even if we do)
                            Post existingComment = null;
                            List<Post> subcomments = new ArrayList<>();
                            if (existingPost != null) {
                                for (Post comment : existingPost.getComments()) {
                                    if (comment.getTimestamp().equals(fbcomment.getCreatedTime())) {
                                        existingComment = comment;
                                        subcomments = existingComment.getComments();
                                        break;
                                    }
                                }
                            }
                            
                            // Get subcomments
                            if (fbcomment.getComments() != null) {
                                Comments fbSubComments = fbcomment.getComments();
                                for (Comment fbSubComment : fbSubComments.getData()) {
                                    User subCommentPoster = userService.getUserByFacebookId(fbSubComment.getFrom().getId());
                                    if (subCommentPoster != null && fbSubComment.getMessage() != null && !fbSubComment.getMessage().isEmpty()) {

                                        // Don't duplicate subcomments
                                        Post existingSubComment = null;
                                        if (existingComment != null) {
                                            for (Post subcomment : existingComment.getComments()) {
                                                if (subcomment.getTimestamp().equals(fbSubComment.getCreatedTime())) {
                                                    existingSubComment = subcomment;
                                                    break;
                                                }
                                            }
                                        }
                                        if (existingSubComment != null) {
                                            continue;
                                        }

                                        Post subcomment = new Post(subCommentPoster, null, null, null, fbSubComment.getCreatedTime(), fbSubComment.getMessage());
                                        subcomments.add(subcomment);
                                        postService.newPost(subcomment);
                                    }
                                }
                            }
                            
                            // Don't continue if comment was already imported
                            if (existingComment != null) {
                                postService.updatePost(existingComment);
                                continue;
                            }
                            
                            Post comment = new Post(poster, null, null, subcomments, fbcomment.getCreatedTime(), fbcomment.getMessage());
                            comments.add(comment);
                            postService.newPost(comment);
                        }
                    }
                }

                // Don't continue if post was already imported
                if (existingPost != null) {
                    postService.updatePost(existingPost);
                    continue;
                }
                
                // Gather the mentions in the post
                List<User> mentions = new ArrayList<>();
                for (NamedFacebookType fbmention : fbpost.getTo()) {
                    User existingUser = userService.getUserByFacebookId(fbmention.getId());
                    if (existingUser != null) {
                        mentions.add(existingUser);
                    }
                }
                
                Post post = new Post(poster, user, mentions, comments, fbpost.getCreatedTime(), fbpost.getMessage());
                postService.newPost(post);
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

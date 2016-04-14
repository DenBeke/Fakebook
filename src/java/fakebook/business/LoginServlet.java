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
import fakebook.persistence.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
                    
                    // Check if user exists
                    User user = userService.getUserByEmail(email);
                    if (user != null) {
                        // TODO: Check if merging (fbToken is passed now but null in database)
                        
                        // Update user data
                        if (fbuser.getFirstName() != null && !fbuser.getFirstName().isEmpty())
                            user.setFirstName(fbuser.getFirstName());
                        if (fbuser.getLastName() != null && !fbuser.getLastName().isEmpty())
                            user.setLastName(fbuser.getLastName());
                        if (fbuser.getGender() != null && !fbuser.getGender().isEmpty())
                            user.setGender(fbuser.getGender());
                        if (fbuser.getBirthday() != null && !fbuser.getBirthday().isEmpty())
                            user.setBirthday(fbuser.getBirthday());
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

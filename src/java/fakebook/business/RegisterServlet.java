/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

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
@WebServlet(name = "Register", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

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
        
        if (request.getSession().getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/wall");
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null) {
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
        else {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String gender = request.getParameter("gender");
            String birthday = request.getParameter("birthday");
            
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("gender", gender);
            request.setAttribute("birthday", birthday);
            
            // The email and password values have to be provided
            if (email.isEmpty() || password == null || password.isEmpty() || firstName == null || firstName.isEmpty()) {
                request.setAttribute("error", "Email, password and first name have to be provided");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            // Check if user already exists
            User user = userService.getUserByEmail(email);
            if (user != null) {
                
                // Check if existing user was a facebook account
                if (user.getPassword() == null) {
                    // TODO: Merge acount
                    //       Security issue: shouldn't the user be asked to login to facebook at this point?

                    user.setPassword(password);
                    userService.updateUser(user);
                    
                    request.getRequestDispatcher("login").forward(request, response);
                }
                else { // Account was already registered
                    request.setAttribute("error", "An account has already been created with the email address");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            }
            else { // Account did not exist yet
                user = new User(email, null, password, firstName, lastName, gender, birthday, false, "");
                userService.newUser(user);

                request.getRequestDispatcher("login").forward(request, response);
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

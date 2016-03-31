/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
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
        
        String fbToken = request.getParameter("fbToken");
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
            
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("password", request.getParameter("password"));
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("gender", gender);
            request.setAttribute("birthday", birthday);
            
            // Check if the user is logging in with facebook
            if (fbToken != null) {
                // TODO: Check again whether token is valid?
            }
            else { // User is logging in normally
                // The email and password values have to be provided
                if (email.isEmpty() || password.isEmpty()) {
                    request.setAttribute("error", "Email and password have to be provided");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            }
            
            // TODO: Check if user already exists and handle accordingly
            
            User user = new User(firstName, lastName, email, fbToken, password, false, new Vector<User>());
            userService.newUser(user);
            
            request.getRequestDispatcher("login").forward(request, response);
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

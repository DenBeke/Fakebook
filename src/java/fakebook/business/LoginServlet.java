/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import java.io.IOException;
import java.io.PrintWriter;
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

        // TODO: What is the value of email when facebook user refuses to share it?
        //       An error should be given when this happens

        if (email == null && fbToken == null) { // Accessing page directly
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        else {
            if (fbToken != null) {

                // TODO: Verify that the token is valid by trying to use it to access something
                // TODO: Get all information here instead of by POST to prevent misuse

                // Check if user exists
                if (userService.getUserByEmail(email) != null) {
                    // TODO: Check if merging (fbToken is passed now but null in database)
                    
                    response.sendRedirect(request.getContextPath() + "/temp_welcome.jsp");
                }
                else { // New account
                    System.out.println("register");
                    request.getRequestDispatcher("register").forward(request, response);
                }
            }
            else { // Non-facebook login
                request.setAttribute("email", email);
                request.setAttribute("password", password);

                if (email.isEmpty() || password.isEmpty()) {
                    request.setAttribute("error", "Please provide both an email and a password");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }

                // Check if user exists
                if (userService.getUserByEmail(email) != null) {
                    // TODO: Check if merging (password is passed now but null in database)

                    // TODO
                    // User user = userService.getUserByEmail(email); // TODO: emailUsed check is not needed, just check if this returns null
                    // if (password.equals(user.getPassword())) {
                        response.sendRedirect(request.getContextPath() + "/temp_welcome.jsp");
                    //}
                    //else {
                    //    request.setAttribute("error", "Incorrect email or password");
                    //    request.getRequestDispatcher("login.jsp").forward(request, response);
                    //}
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

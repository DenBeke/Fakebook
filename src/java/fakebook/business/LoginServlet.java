/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

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
        
        String fbtoken = request.getParameter("fbtoken");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // TODO: What is the value of email when facebook user refuses to share it?
        //       An error should be given when this happens

        if (email == null) { // Accessing page directly
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        else {
            // TODO: Make sure all parameters are provided
            // - email must always be provided
            // - if fbtoken is null, password must also be provided
            // - if fbtoken is set, check if user already exists and log him in

            // Check if user exists
            if (userService.emailUsed(email)) {
                response.sendRedirect(request.getContextPath() + "/temp_welcome.jsp");
            }
            else { // User did not exist yet
                // TODO: If fbtoken is set, forward to register
                
                request.setAttribute("email", email);
                request.setAttribute("password", password);
                request.setAttribute("error", "Incorrect username or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
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

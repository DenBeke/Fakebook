/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.web;

import fakebook.business.UserServiceFacadeLocal;
import fakebook.persistence.User;
import java.io.IOException;
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
@WebServlet(name = "Admin", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

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
        
        // Login if requested
        if (request.getParameter("adminLoginEmail") != null && request.getParameter("adminLoginPassword") != null) {
            User user = userService.getUserByEmail(request.getParameter("adminLoginEmail"));
            if (user == null || !user.getPassword().equals(request.getParameter("adminLoginPassword")) && !user.getIsDeleted()) {
                request.setAttribute("error", "Incorrect username or password!");
                request.getRequestDispatcher("admin-login.jsp").forward(request, response);
                return;
            }

            request.getSession().setAttribute("currentUser", user.getId());
        }
        
        // Redirect user to admin login page when not logged in or not an admin
        Long currentUserId = (Long)request.getSession().getAttribute("currentUser");
        if (currentUserId == null || userService.getUser(currentUserId) == null) {
            request.getRequestDispatcher("admin-login.jsp").forward(request, response);
            return;
        }
        else if (!userService.getUser(currentUserId).getIsAdmin()) {
            request.setAttribute("error", "Your account is not an admin!");
            request.getRequestDispatcher("admin-login.jsp").forward(request, response);
            return;
        }
        
        request.getRequestDispatcher("admin.jsp").forward(request, response);
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
        return "Admin Servlet";
    }// </editor-fold>

}

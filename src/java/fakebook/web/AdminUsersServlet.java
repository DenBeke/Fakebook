/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.web;

import fakebook.business.UserServiceFacadeLocal;
import fakebook.persistence.User;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fakebook.business.AdminServiceFacadeLocal;
import java.io.PrintWriter;

/**
 *
 * @author texus
 */
@WebServlet(name = "AdminUsers", urlPatterns = {"/admin-users"})
public class AdminUsersServlet extends HttpServlet {

    @EJB
    private UserServiceFacadeLocal userService;
    
    @EJB
    private AdminServiceFacadeLocal adminService;

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
        
        // Check if creating a new user
        if (request.getParameter("new_user_email") != null) {
            String email = request.getParameter("new_user_email");
            String password = request.getParameter("new_user_password");
            String firstName = request.getParameter("new_user_firstName");
            String lastName = request.getParameter("new_user_lastName");
            String gender = request.getParameter("new_user_gender");
            String birthday = request.getParameter("new_user_birthday");
            String admin = request.getParameter("new_user_admin");
            
            request.setAttribute("newUserEmail", email);
            request.setAttribute("newUserPassword", password);
            request.setAttribute("newUserFirstName", firstName);
            request.setAttribute("newUserLastName", lastName);
            request.setAttribute("newUserGender", gender);
            request.setAttribute("newUserBirthday", birthday);
            request.setAttribute("newUserAdmin", admin);
            
            // The email, password and first name have to be provided
            if (email.isEmpty() || password == null || password.isEmpty() || firstName == null || firstName.isEmpty()) {
                request.setAttribute("error", "Failed to register user: email, password and first name have to be provided");
            }
            else {
                boolean createAdmin = false;
                if (admin != null && !admin.isEmpty()) {
                    createAdmin = true;
                }
        
                request.setAttribute("error", adminService.createUser(email, password, firstName, lastName, gender, birthday, createAdmin));
            }
        }
        
        // Check if downloading data
        if (request.getParameter("download_user_id") != null) {
            Long userId = Long.decode(request.getParameter("download_user_id"));
            String data = adminService.downloadData(userId);
            response.getWriter().write(data);
            return;
        }
        
        // Check if deleting a user
        if (request.getParameter("deleted_user_id") != null) {
            long userId = Long.decode(request.getParameter("deleted_user_id"));
            adminService.deleteUser(userId);
        }
        
        List<User> users = adminService.getAllUsers();
        Set<User> onlineUsers = adminService.getOnlineUsers();

        request.setAttribute("onlineUsers", onlineUsers);
        request.setAttribute("allUsers", users);
        request.getRequestDispatcher("admin-users.jsp").forward(request, response);
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
        return "Admin Users Servlet";
    }// </editor-fold>

}

package test.presentation;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import test.business.UserFacadeLocal;
import test.persistence.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author robin
 */
@WebServlet(name= "UserServlet", urlPatterns = "/UserServlet")
public class UserServlet extends HttpServlet{
    @EJB
    private UserFacadeLocal userBean;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        // Get information from request
        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");
        // Parse the int
        long userId = 0;
        if (userIdStr != null && !userIdStr.equals("")){
            userId = Integer.parseInt(userIdStr);
        }
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        System.out.println(name);
        System.out.println(userIdStr + " -> " + userId);
        // TODO information does not come through from jsp.
        User user = new User(userId, name, password, email);
        
        // Perform action based on information
        if ("Add".equalsIgnoreCase(action)) {
            userBean.addUser(user);
        } else if ("Edit".equalsIgnoreCase(action)) {
            userBean.editUser(user);
        } else if ("Delete".equalsIgnoreCase(action)) {
            userBean.deleteUser(userId);
        } else if ("Search".equalsIgnoreCase(action)) {
            user = userBean.getUser(userId);
        }
        request.setAttribute("user", user);
        request.setAttribute("allUsers", userBean.getAllUsers());
        System.out.println(userBean.getAllUsers().size());
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "User Servlet";
    }
    
    
}

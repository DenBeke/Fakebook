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
import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *
 * @author texus
 */
@WebServlet(name = "FriendRequest", urlPatterns = {"/friend-request"})
public class FriendRequest extends HttpServlet {

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

        User currentUser = (User)(request.getSession().getAttribute("currentUser"));
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        if (request.getParameter("friend_user_id") != null) {
            Long userId = Long.decode(request.getParameter("friend_user_id"));
            
            if (!userId.equals(currentUser.getId())) {
                User user = userService.getUser(userId);

                if (user != null) {
                    List<User> friendRequests = user.getFriendshipRequests();
                    friendRequests.add(currentUser);
                    user.setFriendshipRequests(friendRequests);

                    request.setAttribute("userId", userId);
                    request.setAttribute("userName", user.getName());
                }
                else {
                    request.setAttribute("error", "The user you are trying to become friends with does not exist!");
                }
            }
            else {
                request.setAttribute("error", "You can't send a friend request to yourself!");
            }
        }
        else {
                request.setAttribute("error", "Something is wrong with the friend request!");
            }
        
        request.getRequestDispatcher("friend-request.jsp").forward(request, response);
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
        return "Friend Request Servlet";
    }// </editor-fold>

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.web;

import fakebook.business.UserServiceFacadeLocal;
import fakebook.persistence.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
@WebServlet(name = "Friends", urlPatterns = {"/friends"})
public class FriendsServlet extends HttpServlet {

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
        
        // Check if we are searching for someone
        if (request.getParameter("searched_friend") != null) {
            String searchedFriend = request.getParameter("searched_friend");
            
            List<User> friends = new ArrayList<>();
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                if (Pattern.compile(Pattern.quote(searchedFriend), Pattern.CASE_INSENSITIVE).matcher(user.getName()).find()) {
                    friends.add(user);
                }
            }
            
            request.setAttribute("searchedFriend", searchedFriend);
            request.setAttribute("searchResult", friends);
        }
        
        // Check if we are accepting a friendship request
        if (request.getParameter("accepted_friend_id") != null) {
            Long friendId = Long.decode(request.getParameter("accepted_friend_id"));
            User friend = userService.getUser(friendId);

            if (friend != null) {
                if (currentUser.getFriendshipRequests().contains(friend)) {
                    currentUser.getFriendshipRequests().remove(friend);
                    
                    if (friend.getFriendshipRequests().contains(currentUser)) {
                        friend.getFriendshipRequests().remove(currentUser);
                    }
                    
                    if (!currentUser.getFriends().contains(friend)) {
                        currentUser.getFriends().add(friend);
                    }
                    if (!friend.getFriends().contains(currentUser)) {
                        friend.getFriends().add(currentUser);
                    }
                    
                    userService.updateUser(currentUser);
                    userService.updateUser(friend);
                }
            }
        }
        
        // Check if we are removing a friend
        if (request.getParameter("removed_friend_id") != null) {
            Long friendId = Long.decode(request.getParameter("removed_friend_id"));
            User friend = userService.getUser(friendId);

            if (friend != null) {
                if (currentUser.getFriends().contains(friend)) {
                    currentUser.getFriends().remove(friend);
                }
                if (!friend.getFriends().contains(currentUser)) {
                    friend.getFriends().remove(currentUser);
                }
                
                userService.updateUser(currentUser);
                userService.updateUser(friend);
            }
        }
        
        request.getRequestDispatcher("friends.jsp").forward(request, response);
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
        return "Friends Servlet";
    }// </editor-fold>

}

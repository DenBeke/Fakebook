/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.web;

import fakebook.business.PostServiceFacadeLocal;
import fakebook.business.UserServiceFacadeLocal;
import fakebook.persistence.Post;
import fakebook.persistence.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mathias
 */
public class PostSeen extends HttpServlet {

    @EJB
    private PostServiceFacadeLocal postService;
    
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            if(request.getParameter("post") == null) {
                return;
            }
            
            Long currentUserId = (Long) (request.getSession().getAttribute("currentUser"));
            if (currentUserId == null || userService.getUser(currentUserId) == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            User currentUser = userService.getUser(currentUserId);
            
            Long id = Long.parseLong(request.getParameter("post"));
            Long timestamp = Long.parseLong(request.getParameter("date"));
            
            java.util.Date time = new java.util.Date(timestamp*1000);
            
            Post post = postService.getPost(id);
            
            if(!post.getWall().getId().equals(currentUser.getId())) {
                return;
            }
            
            post.setSeen(time);
            postService.updatePost(post);
            
            out.println("id: " + id + "\ntimestamp: " + timestamp);
            
            /*
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PostSeen</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + request.getParameter("post"); + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
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
        return "Short description";
    }// </editor-fold>

}

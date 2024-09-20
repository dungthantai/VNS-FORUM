/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Servlet;

import Entity.Posts;
import Entity.Users;
import Session.PostsFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class editpost extends HttpServlet {

    @EJB
    private PostsFacadeLocal postsFacade;

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

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession();
            if (null != session.getAttribute("userid")) {
                int userid = (int) session.getAttribute("userid");
                Users u = new Users(userid);
                Integer post_id = Integer.parseInt(request.getPathInfo().substring(1));
                Posts p = postsFacade.find(post_id);

                if (u.issetPermission("edit_post") || (p.getUserId().getId().equals(userid) && u.issetPermission("edit_self_post"))) {
                    if (null == request.getParameter("content")) {
                        request.setAttribute("post_id", p);
                        RequestDispatcher rd = request.getRequestDispatcher("/createpost.jsp");
                        rd.forward(request, response);
                    } else {
                        String content = request.getParameter("content");
                        if (content.isEmpty()) {
                            out.print("Vườn không nhà trống thất bại");
                        } else {
                            postsFacade.editContent(post_id, content, userid);
                            response.sendRedirect("/forum/post/" + post_id);
                        }
                    }
                } else {
                    out.println("Bạn không đủ quyền edit");
                }
            } else {
                out.println("Please Login.");
            }

        } catch (Exception e) {
            out.println("Something error, please report webmaster.");
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

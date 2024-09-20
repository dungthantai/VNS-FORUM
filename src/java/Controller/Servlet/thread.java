/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Servlet;

import Entity.Boxs;
import Entity.Posts;
import Model.Process.Pagination;
import Session.BoxsFacadeLocal;
import Session.PostsFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
public class thread extends HttpServlet {
    @EJB
    private BoxsFacadeLocal boxsFacade;

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
            
            Integer thread_id = Integer.parseInt(request.getPathInfo().substring(1));
            Integer first = 1;
            Integer max = 10;
            if (null != request.getParameter("postperpage")) {
                max = (Integer) session.getAttribute("postperpage");
            }
            if (null != request.getParameter("page")) {
                if (request.getParameter("page").equals("last")) {
                    first = (postsFacade.countByThread(thread_id) + max - 1) / max;
                } else {
                    first = Integer.parseInt(request.getParameter("page"));
                }
            }


            Pagination paged = new Pagination(postsFacade.countByThread(thread_id), max, first);

            first = (first - 1) * max;
            List<Posts> posts = postsFacade.findRangeByThread(thread_id, first, max);

            List<Boxs> treeboxs = boxsFacade.getTreeDirectory(1);
            
            request.setAttribute("treeboxs", treeboxs);
            request.setAttribute("paged", paged);
            request.setAttribute("posts", posts);
            RequestDispatcher rd = request.getRequestDispatcher("/thread.jsp");
            rd.forward(request, response);
        } catch (ServletException | IOException ex) {
            out.print(ex);
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

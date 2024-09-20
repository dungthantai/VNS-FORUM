/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Servlet;

import Entity.Boxs;
import Entity.Threads;
import Entity.Users;
import Model.Process.Pagination;
import Session.BoxsFacadeLocal;
import Session.ThreadsFacadeLocal;
import Session.UsersFacadeLocal;
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
public class box extends HttpServlet {
    @EJB
    private ThreadsFacadeLocal threadsFacade;

    @EJB
    private BoxsFacadeLocal boxsFacade;

    @EJB
    private UsersFacadeLocal usersFacade;

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
            Integer intbigbox = Integer.parseInt(request.getPathInfo().substring(1));
            List<Boxs> boxs = boxsFacade.findAllSubboxOrderByOrOrdinal(intbigbox);
            
            Integer first = 1;
            if (null != request.getParameter("page")) {
                first = Integer.parseInt(request.getParameter("page"));
            }
            Integer max = 25;

            //Pagination paged = new Pagination(boxs.get(0).countPosts(), max, first);
            Pagination paged = new Pagination(threadsFacade.countByBox(intbigbox), max, first);

            first = (first - 1) * max;
            List<Threads> threads = threadsFacade.findRangeByBox(intbigbox, first, max);
            
            
            //List<Threads> threads = boxs.get(0).getRangeThreadsList(first, max);
            
//            for (Threads thread : threads) {
//                out.println(thread.getLastPostofThread().getUserId().getDisplayName());
//            }
            
            List<Boxs> treeboxs = boxsFacade.getTreeDirectory(intbigbox);
            
            request.setAttribute("paged", paged);
            request.setAttribute("boxs", boxs);
            request.setAttribute("treeboxs", treeboxs);
            request.setAttribute("threads", threads);
            RequestDispatcher rd = request.getRequestDispatcher("/box.jsp");
            rd.forward(request, response);
        } catch (NumberFormatException | ServletException | IOException ex) {
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

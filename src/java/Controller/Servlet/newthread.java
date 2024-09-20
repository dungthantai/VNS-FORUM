/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Servlet;

import Entity.Boxs;
import Entity.Posts;
import Entity.Threads;
import Entity.Users;
import Session.PostsFacadeLocal;
import Session.ThreadsFacadeLocal;
import Session.UsersFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
public class newthread extends HttpServlet {
    @EJB
    private UsersFacadeLocal usersFacade;

    @EJB
    private PostsFacadeLocal postsFacade;

    @EJB
    private ThreadsFacadeLocal threadsFacade;

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
                Integer box_id = Integer.parseInt(request.getPathInfo().substring(1));
                if (null == request.getParameter("title")) {

                    Users user = usersFacade.find((int) session.getAttribute("userid"));
                    request.setAttribute("user", user);
                    request.setAttribute("box_id", box_id);
                    RequestDispatcher rd = request.getRequestDispatcher("/createthread.jsp");
                    rd.forward(request, response);
                } else {
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");
                    if (title.isEmpty() || content.isEmpty()) {
                        out.print("Vườn không nhà trống thất bại");
                    } else {
                        Threads thread = new Threads();
                        thread.setTitle(title);
                        thread.setUserId(new Users((Integer) session.getAttribute("userid")));
                        thread.setBoxId(new Boxs(box_id));
                        thread.setSticky(false);
                        thread.setCreated(new Date());
                        threadsFacade.create(thread);

                        Posts post = new Posts();
                        post.setThreadId(thread);
                        post.setContent(content);
                        post.setUserId(new Users((Integer) session.getAttribute("userid")));
                        post.setCreated(new Date());
                        postsFacade.create(post);

                        response.sendRedirect("/forum/thread/" + thread.getId());
                    }
                }
            } else {
                out.println("Please login");
            }
        } catch (Exception e) {
            out.println(e);
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

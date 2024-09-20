/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Servlet;

import Controller.Process.reCaptcha;
import Entity.Groups;
import Entity.Users;
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
 * @author DTT
 */
public class register extends HttpServlet {
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

        if (null == request.getParameter("username")) {
            RequestDispatcher rd = request.getRequestDispatcher("/register.jsp");
            rd.forward(request, response);
        } else {
            Boolean valid = reCaptcha.verify(request.getParameter("g-recaptcha-response"));
            if (!valid) {
                out.println("reCaptcha invalid!");
            } else {
                String us = request.getParameter("username");
                String ps = request.getParameter("password");
                String psc = request.getParameter("passwordconfirm");
                String em = request.getParameter("email");
                String emc = request.getParameter("emailconfirm");
                out.println("get param success!");
                if (us.isEmpty() || ps.isEmpty()) {
                    out.print("Vườn không nhà trống thất bại!");
                } else if (!ps.equals(psc) && !em.equals(emc)) {
                    out.print("Gõ lại cũng sai!");
                } else {
                    Users user = new Users();
                    user.setUsername(us);
                    user.setDisplayName(us);
                    user.setPassword(ps);
                    user.setGroupId(new Groups(3));
                    user.setCreated(new Date());
                    try {
                        usersFacade.create(user);
                        out.println("Create Success!");
                        HttpSession session = request.getSession();
                        session.setAttribute("userid", user.getId());
                        response.sendRedirect("");
                    } catch (Exception e) {
                        out.println("Something error, please report webmaster.");
                        out.println(e);
                    }
                }
            }
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

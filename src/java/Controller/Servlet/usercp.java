/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Servlet;

import Entity.UserRelation;
import Entity.Users;
import Session.UserRelationFacadeLocal;
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
 * @author root
 */
public class usercp extends HttpServlet {

    @EJB
    private UserRelationFacadeLocal userRelationFacade;

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

            HttpSession session = request.getSession();
            if (null != session.getAttribute("userid")) {
                if (session.getAttribute("saveuser") != null) {
                    session.setMaxInactiveInterval(604800);
                } else {
                    session.setMaxInactiveInterval(3600);
                }
                Users user = usersFacade.find((int) session.getAttribute("userid"));
                request.setAttribute("user", user);

                String resp;

                if (null == request.getPathInfo() || request.getPathInfo().equals("/")) {
                    request.setAttribute("cp", "rep");
                } else if (request.getPathInfo().substring(1).equals("rep")) {
                    request.setAttribute("cp", "rep");
                    if (null != request.getParameter("touser")) {
                        resp = usersFacade.transferRep(user.getId(), request.getParameter("touser"), request.getParameter("rep"));
                        request.setAttribute("response", resp);
                    }
                } else if (request.getPathInfo().substring(1).equals("relationship")) {
                    request.setAttribute("cp", "relationship");
                    List<UserRelation> urs = userRelationFacade.controlPanelUserRelation(user.getId());
                    request.setAttribute("urs", urs);
                    if (null != request.getParameter("touser")) {
                        resp = userRelationFacade.woo(user.getId(), request.getParameter("touser"));
                        request.setAttribute("response", resp);
                    }
                    if (null != request.getParameter("marry")) {
                        Boolean b = userRelationFacade.editRelation(user.getId(), Integer.parseInt(request.getParameter("marry")), "marry");
                        resp = b ? "Ket hon thanh cong" : "Loi";

                        request.setAttribute("response", resp);
                    }
                    if (null != request.getParameter("unmarry")) {
                        Boolean b = userRelationFacade.editRelation(user.getId(), Integer.parseInt(request.getParameter("unmarry")), "unmarry");
                        resp = b ? "Da huy hanh dong" : "Loi";
                        request.setAttribute("response", resp);
                    }
                    if (null != request.getParameter("unallowmarry")) {
                        Boolean b = userRelationFacade.editRelation(user.getId(), Integer.parseInt(request.getParameter("unallowmarry")), "unmarry");
                        resp = b ? "Da huy hanh dong" : "Loi";
                        request.setAttribute("response", resp);
                    }
                    
                } else if (request.getPathInfo().substring(1).equals("partner")) {
                    request.setAttribute("cp", "partner");
                } else if (request.getPathInfo().substring(1).equals("buypartner")) {
                    request.setAttribute("cp", "buypartner");
                }

                RequestDispatcher rd = request.getRequestDispatcher("/usercp.jsp");
                rd.forward(request, response);
            } else {
                //response.sendRedirect("/forum/");
                out.println("<h1>Servlet NewServlet at " + request.getPathInfo().substring(1) + "</h1>");
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

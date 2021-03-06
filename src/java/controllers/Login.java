/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.LoginModel;
import models.ConnectionDB;
import models.LoginAttempts;

/**
 *
 * @author user
 */
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     *
     *
     * Login Sevlet Takes username and password from form post validates if
     * password match stored passsword
     *
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean validate = (Boolean) LoginModel.validate(username, password)[0];

        try {
            Connection conn = null;
            PreparedStatement ps = null;

            String query = "SELECT login_attempt FROM users WHERE username=?; ";

            conn = ConnectionDB.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            rs.next();

            boolean checkLock = LoginAttempts.checkLock(username);
            int login_attempt = rs.getInt("login_attempt");

            if (checkLock == true) {
                if (validate) {
                    boolean reset = LoginAttempts.add(username, login_attempt);
                    if (reset == true) {
                        int role_ID = (int) LoginModel.validate(username, password)[1];
                        int can_add = (int) LoginModel.validate(username, password)[2];
                        int can_edit = (int) LoginModel.validate(username, password)[3];
                        int can_delete = (int) LoginModel.validate(username, password)[4];
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        session.setAttribute("role_ID", role_ID);
                        session.setAttribute("can_add", can_add);
                        session.setAttribute("can_edit", can_edit);
                        session.setAttribute("can_delete", can_delete);

                        if (!session.isNew()) {
                            RequestDispatcher rd = request.getRequestDispatcher("/views/includes/dashboard.jsp");
                            rd.include(request, response);
                        }
                    }
                } else {
                    boolean add = LoginAttempts.add(username, login_attempt);
                    if (add == true) {
                        System.out.println("failed to login");
                        request.setAttribute("alertModal", true);
                        RequestDispatcher rd = request.getRequestDispatcher("views/login.jsp");
                        rd.forward(request, response);
                    }

                }
            } else {
                System.out.println("locked");
                request.setAttribute("alertModal", true);
                request.setAttribute("alertMessage", "You've Been locked");
                RequestDispatcher rd = request.getRequestDispatcher("views/login.jsp");
                rd.forward(request, response);
            }
        } catch (SQLException e) {
            System.out.println("validate error " + e);
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
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

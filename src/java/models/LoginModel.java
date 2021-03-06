/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.lambdaworks.crypto.SCryptUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public class LoginModel {

    public static Object[] validate(String username, String password) throws ClassNotFoundException, SQLException {
        Object[] data = new Object[5];

        //Validates if the given value matches the value in the database
        data[0] = false;
        try {
            Connection conn = null;
            PreparedStatement ps = null;

            String query = "SELECT * FROM users INNER join user_roles on users.role_ID = user_roles.role_ID "
                    + "WHERE username =?; ";

            conn = ConnectionDB.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean matched = SCryptUtil.check(password, rs.getString("password"));
            if (matched) {
                data[0] = true;
                data[1] = rs.getInt("role_ID");
                data[2] = rs.getInt("can_add");
                data[3] = rs.getInt("can_edit");
                data[4] = rs.getInt("can_delete");
            } else {

                rs.next();
                if (rs.isAfterLast()) {
                    data[0] = false;
                    
                }
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("validate error " + e);
        }
        return data;
    }

}

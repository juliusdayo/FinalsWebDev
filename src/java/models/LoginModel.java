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
    
    public static boolean validate(String username,String password) throws ClassNotFoundException, SQLException{
        boolean status= false;
        try{
            Connection conn =null;
            PreparedStatement ps = null;
            
            String query = "SELECT username,password FROM users "
                    + "WHERE username =?; ";
            
            conn = ConnectionDB.getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1,username);
            
            ResultSet rs=ps.executeQuery();
            rs.next();
            boolean matched = SCryptUtil.check(password, rs.getString("password"));
            if(matched){
                status = true;
            }else{
            rs.next();
            }
            
            conn.close();
        }catch(SQLException e){
            System.out.println("validate error " +e);
        }
        return status;
    }
    
}
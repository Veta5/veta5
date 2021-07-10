/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream_gift;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConeccion {
    public static final String URL = "jdbc:mysql://localhost:3306/DreamGift_bdd";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "paulo1234";
    
    public static Connection getConnection(){
        Connection con = null;
        JOptionPane.showMessageDialog(null, "Conexion exitosa");
        return con;
    }
}

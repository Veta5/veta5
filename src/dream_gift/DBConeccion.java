/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dream_gift;

import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;



public class DBConeccion{
    
    public static void main(String args[]){}
    
    Connection enlazar = null;
   
    public Connection conectar(){
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            enlazar = DriverManager.getConnection("jdbc:mysql://localhost:3306/DREAM_GIFT_DB","root","paulo1234");
            JOptionPane.showMessageDialog(null, "Conexión establecida");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No fue posible establecer la conexión");
        }
    return enlazar;
    
    }
    
}
//public class DBConeccion {
    
    
    
    
    /*private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/Dream_GIFT_DB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "paulo1234";
    
    static{
        try {
            Class.forName(DRIVER);
        }
        catch (ClassNotFoundException e){
        }
    }*/
    
    /*public static Connection getConnection(){
        Connection con = null;
        try{
            con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            JOptionPane.showMessageDialog(null, "Conexion exitosa");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, "Conexion erronea");
        }
        return con;
    }*/ 
    
    /*Connection  conect = null;
            public Connection conexion(){
                try {
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Conexion erronea");
                }
            return conect;
            }*/
//}



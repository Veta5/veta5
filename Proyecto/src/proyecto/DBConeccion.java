/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import java.sql.Connection;
import java.sql.*;
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.mysql.cj.result.DefaultValueFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//import javax.swing.DefaultListModel;
import java.sql.*;
//import java.util.Date;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import com.toedter.calendar.JDateChooser;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
import javax.swing.JTable;
//import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
//import javax.swing.table.TableCellRenderer;
//import java.text.SimpleDateFormat;
//import java.time.Clock;
//import java.util.Calendar;
//import javax.swing.JComboBox;
import javax.swing.JTextField;
//import javax.swing.SpinnerNumberModel;
//import javax.swing.text.Document;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import java.awt.Font;
//import java.awt.Desktop;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
//import java.io.IOException;
import java.io.OutputStream;
//import java.net.URL;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ResourceBundle;


/**
 *
 * @author Usuario
 */
public class Ventas extends javax.swing.JFrame {

    /**
     * Creates new form probando
     */
    DBConeccion con = new DBConeccion();
    Connection conect = con.conectar();
    
    
    DefaultTableModel tabla_ventas_pendientes=new DefaultTableModel();
    DefaultTableModel tabla_lista_destinos=new DefaultTableModel();
    DefaultTableModel tabla_actualizacion_despacho=new DefaultTableModel();
    
    
    public Ventas() {
        initComponents();
        
        tabla_ventas_pendientes.addColumn("Número Pedido");
        tabla_ventas_pendientes.addColumn("Fecha Pedido");
        tabla_ventas_pendientes.addColumn("Nombre Cliente");
        tabla_ventas_pendientes.addColumn("Rut Cliente");
        tabla_ventas_pendientes.addColumn("Número Teléfono");
        tabla_ventas_pendientes.addColumn("Monto");
        tabla_ventas_pendientes.addColumn("Pack");
        tabla_ventas_pendientes.addColumn("Selección");
        this.table_Ventas_Pendientes.setModel(tabla_ventas_pendientes);
        tabla_lista_destinos.addColumn("Número Pedido");
        tabla_lista_destinos.addColumn("Pack");
        tabla_lista_destinos.addColumn("Destinatario");
        tabla_lista_destinos.addColumn("Fecha Entrega");
        tabla_lista_destinos.addColumn("Comuna");
        tabla_lista_destinos.addColumn("Dirección");
        tabla_lista_destinos.addColumn("Hora Entrega");
        this.table_Lista_Destinos.setModel(tabla_lista_destinos);
        tabla_actualizacion_despacho.addColumn("Número Pedido");
        tabla_actualizacion_despacho.addColumn("Pack");
        tabla_actualizacion_despacho.addColumn("Destinatario");
        tabla_actualizacion_despacho.addColumn("Fecha Entrega");
        tabla_actualizacion_despacho.addColumn("Comuna");
        tabla_actualizacion_despacho.addColumn("Hora Entrega");
        tabla_actualizacion_despacho.addColumn("Estado Entrega");
        tabla_actualizacion_despacho.addColumn("Acción");
        this.table_Actualizacion_Despacho.setModel(tabla_actualizacion_despacho);
        
        
        Consultar_RRSSS();
        Consultar_Comuna();
        Consultar_Pack();
        Consultar_Banco();
        Consultar_Estado_Venta();
        Mostrar_Lista_Ventas_Pendientes();
        Mostrar_Lista_Destinos();
        Mostrar_Actualizacion_Despacho();
        bloquear_estadoentrega();
        bloquear_guardarestadoentrega();
        txt_Nombre_Cliente.setEnabled(false);
        txt_Email_Cliente.setEnabled(false);
        txt_Telefono_Cliente.setEnabled(false);
        
        txt_Numero_Pedido_Confirmacion.setEnabled(false);
        txt_Nombre_Cliente_Confirmacion.setEnabled(false);
        txt_Rut_Cliente_Confirmacion.setEnabled(false);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    
    private void Imprimir_Lista_Destinos(String buscar){
        String sql = "select 	EST_VEN_NOMBRE, VTA_ID_VENTA, VTA_NOMBRE_DESTINATARIO, VTA_FECHA_ENTREGA, COM_NOMBRE, VTA_DIRECCION_DESTINATARIO, VTA_HORA_ENTREGA_INICIAL, VTA_HORA_ENTREGA_FINAL, PCK_NOMBRE\n" +
                     "from venta inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK inner join comunas on venta.COM_ID_COMUNA = comunas.COM_ID_COMUNA\n" +
                     "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN\n" +
                     "WHERE  EST_VEN_NOMBRE = 'Despacho Pendiente'  AND  VTA_FECHA_ENTREGA LIKE '%"+buscar+"%'    order by VTA_ID_VENTA;";
        PreparedStatement pst = null;
        ResultSet rs = null;
        String aux;
        String aux2;
        
        
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            
            OutputStream file = new FileOutputStream(new File("C:\\Users\\paul1\\OneDrive\\Escritorio\\Dream Gift PDF\\Lista_Destinos.pdf"));
            Document document = new Document();
            
            PdfWriter.getInstance((com.itextpdf.text.Document) document, file);
            document.open();
            PdfPTable tabla = new PdfPTable (7);
            Paragraph p = new Paragraph ("Lista de destinos \n\n", FontFactory.getFont("Arial",16,Font.ITALIC,BaseColor.BLACK));
            
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            
            document.add(new Paragraph(""));
            
            
            float [] mediaceldas = {12.0f, 12.0f, 12.0f, 12.0f, 12.0f, 12.0f, 12.0f}; 
            
            tabla.setWidths(mediaceldas);
            tabla.addCell(new Paragraph("Número Pedido", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Pack", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Destinatario", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Fecha Entrega", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Comuna", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Dirección", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Hora Entrega", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            
            
            while (rs.next()){
                tabla.addCell(new Paragraph(rs.getString("VTA_ID_VENTA"), FontFactory.getFont("Calibri",9)));                  
                tabla.addCell(new Paragraph(rs.getString("PCK_NOMBRE"), FontFactory.getFont("Calibri",9)));
                tabla.addCell(new Paragraph(rs.getString("VTA_NOMBRE_DESTINATARIO"), FontFactory.getFont("Calibri",9)));
                tabla.addCell(new Paragraph(rs.getString("VTA_FECHA_ENTREGA"), FontFactory.getFont("Calibri",9)));
                tabla.addCell(new Paragraph(rs.getString("COM_NOMBRE"), FontFactory.getFont("Calibri",9)));
                tabla.addCell(new Paragraph(rs.getString("VTA_DIRECCION_DESTINATARIO"), FontFactory.getFont("Calibri",9)));
                aux = rs.getString("VTA_HORA_ENTREGA_INICIAL");
                aux2 = rs.getString("VTA_HORA_ENTREGA_FINAL");
                tabla.addCell(new Paragraph(aux + " a " + aux2, FontFactory.getFont("Calibri",9)));      
            }

             document.add(tabla);              
             document.close();
             file.close(); 
             

        } 
        catch (Exception e) {
        }
        
        try{
          File file = new File("C:\\Users\\paul1\\OneDrive\\Escritorio\\Dream Gift PDF\\Lista_Destinos.pdf");
          Desktop.getDesktop().open(file);
       }   
      catch (Exception e){
        e.printStackTrace();
         }
        
    }
    
    private void Imprimir_Actualizacion_Despacho(String buscar){
        String sql = "select 	EST_VEN_NOMBRE, VTA_ID_VENTA, VTA_NOMBRE_DESTINATARIO, VTA_FECHA_ENTREGA, COM_NOMBRE, VTA_HORA_ENTREGA_INICIAL, VTA_HORA_ENTREGA_FINAL, PCK_NOMBRE\n" +
                     "from venta \n" +
                     "inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK \n" +
                     "inner join comunas on venta.COM_ID_COMUNA = comunas.COM_ID_COMUNA\n" +
                     "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN\n" +
                     " WHERE  EST_VEN_NOMBRE = 'Despacho Pendiente' " +
                     "OR EST_VEN_NOMBRE = 'Recibido' OR EST_VEN_NOMBRE = 'No Encontrado' OR EST_VEN_NOMBRE = 'Anulado' OR EST_VEN_NOMBRE = 'Rechazado'  OR EST_VEN_NOMBRE = 'Despacho Pendiente'" +
                     "AND VTA_FECHA_ENTREGA LIKE '%"+buscar+"%' order by VTA_ID_VENTA;";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        String aux;
        String aux2;
        
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            
            OutputStream file = new FileOutputStream(new File("C:\\Users\\paul1\\OneDrive\\Escritorio\\Dream Gift PDF\\Despachos.pdf"));
            Document document = new Document();
            
            PdfWriter.getInstance((com.itextpdf.text.Document) document, file);
            document.open();
            PdfPTable tabla = new PdfPTable (7);
            Paragraph p = new Paragraph ("Lista Estados de Despacho \n\n", FontFactory.getFont("Arial",16,Font.ITALIC,BaseColor.BLACK));
            
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            
            document.add(new Paragraph(""));
            
            
            float [] mediaceldas = {12.0f, 12.0f, 12.0f, 12.0f, 12.0f, 12.0f, 12.0f}; 
            
            tabla.setWidths(mediaceldas);
            tabla.addCell(new Paragraph("Número Pedido", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Pack", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Destinatario", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Fecha Entrega", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Comuna", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Hora Entrega", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            tabla.addCell(new Paragraph("Estado Entrega", FontFactory.getFont("Calibri",10,BaseColor.BLUE)));
            
            
            while (rs.next()){
                tabla.addCell(new Paragraph(rs.getString("VTA_ID_VENTA"), FontFactory.getFont("Calibri",9)));                  
                tabla.addCell(new Paragraph(rs.getString("PCK_NOMBRE"), FontFactory.getFont("Calibri",9)));
                tabla.addCell(new Paragraph(rs.getString("VTA_NOMBRE_DESTINATARIO"), FontFactory.getFont("Calibri",9)));
                tabla.addCell(new Paragraph(rs.getString("VTA_FECHA_ENTREGA"), FontFactory.getFont("Calibri",9)));
                tabla.addCell(new Paragraph(rs.getString("COM_NOMBRE"), FontFactory.getFont("Calibri",9))); 
                aux = rs.getString("VTA_HORA_ENTREGA_INICIAL");
                aux2 = rs.getString("VTA_HORA_ENTREGA_FINAL");
                tabla.addCell(new Paragraph(aux + " a " + aux2, FontFactory.getFont("Calibri",9)));  
                tabla.addCell(new Paragraph(rs.getString("EST_VEN_NOMBRE"), FontFactory.getFont("Calibri",9)));
            }
            
            
             document.add(tabla);              
             document.close();
             file.close(); 
             

        } 
        catch (Exception e) {
        }
        
        try{
          File file = new File("C:\\Users\\paul1\\OneDrive\\Escritorio\\Dream Gift PDF\\Despachos.pdf");
          Desktop.getDesktop().open(file);
       }   
      catch (Exception e){
        e.printStackTrace();
         }
        
        
    }
    
    
    
    
    
    
    public void Consultar_RRSSS (){
        String SQL = "SELECT RRS_NOMBRE FROM rrss ORDER BY RRS_ID_RRSS";
        String SQL2 = "SELECT RRS_ESTADO FROM rrss ORDER BY RRS_ID_RRSS";
        combobox_redes_sociales.removeAllItems();
        try {
            PreparedStatement pst = conect.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2 = conect.prepareStatement(SQL2);
            ResultSet rs2 = pst2.executeQuery();
            combobox_redes_sociales.addItem("Seleccione una opción"); 
            while(rs.next() && rs2.next()){
                if(rs2.getString("RRS_ESTADO").contentEquals("Activo")){
                    combobox_redes_sociales.addItem(rs.getString("RRS_NOMBRE"));
                }
            }
        } catch (Exception e) {}
    }
    public void Consultar_Comuna(){
        String SQL = "SELECT COM_NOMBRE FROM comunas ORDER BY COM_ID_COMUNA";
        String SQL2 = "SELECT COM_ESTADO FROM comunas ORDER BY COM_ID_COMUNA";
        combobox_Comuna.removeAllItems();
        try {
            PreparedStatement pst = conect.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2 = conect.prepareStatement(SQL2);
            ResultSet rs2 = pst2.executeQuery();
            combobox_Comuna.addItem("Seleccione una opción"); 
            while(rs.next() && rs2.next()){
                if(rs2.getString("COM_ESTADO").contentEquals("Activo")){
                    combobox_Comuna.addItem(rs.getString("COM_NOMBRE"));
                }
            }
        } catch (Exception e) {}
    }
    public void Consultar_Pack(){
        String SQL = "SELECT PCK_NOMBRE FROM pack ORDER BY PCK_ID_PACK";
        String SQL2 = "SELECT PCK_ESTADO FROM pack ORDER BY PCK_ID_PACK";
        String SQL3 = "SELECT PCK_STOCK FROM pack ORDER BY PCK_ID_PACK";
        combobox_Pack.removeAllItems();
        try {
            PreparedStatement pst = conect.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2 = conect.prepareStatement(SQL2);
            ResultSet rs2 = pst2.executeQuery();
            PreparedStatement pst3 = conect.prepareStatement(SQL3);
            ResultSet rs3 = pst3.executeQuery();
            
            combobox_Pack.addItem("Seleccione una opción"); 
            while(rs.next() && rs2.next() && rs3.next()){
                if(rs2.getString("PCK_ESTADO").contentEquals("Activo") && rs3.getInt("PCK_STOCK")>0){
                    combobox_Pack.addItem(rs.getString("PCK_NOMBRE"));
                }
            }
        } catch (Exception e) {}
    }
    public void Consultar_Banco(){
        String SQL = "SELECT BAN_NOMBRE FROM bancos ORDER BY BAN_ID_BANCO";
        String SQL2 = "SELECT BAN_ESTADO FROM bancos ORDER BY BAN_ID_BANCO";
        combobox_Banco.removeAllItems();
        try {
            PreparedStatement pst = conect.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2 = conect.prepareStatement(SQL2);
            ResultSet rs2 = pst2.executeQuery();
            combobox_Banco.addItem("Seleccione una opción"); 
            while(rs.next() && rs2.next()){
                if(rs2.getString("BAN_ESTADO").contentEquals("Activo")){
                    combobox_Banco.addItem(rs.getString("BAN_NOMBRE"));
                }
            }
        } catch (Exception e) {}
    }
    public void Consultar_Estado_Venta(){
        String SQL = "SELECT EST_VEN_NOMBRE FROM estados_Venta ORDER BY EST_ID_EST_VEN";
        String SQL2 = "SELECT EST_VEN_ESTADO FROM estados_venta ORDER BY EST_ID_EST_VEN";
        combobox_Estado_Entrega.removeAllItems();
        try {
            PreparedStatement pst = conect.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2 = conect.prepareStatement(SQL2);
            ResultSet rs2 = pst2.executeQuery();
            combobox_Estado_Entrega.addItem("Seleccione una opción"); 
            while(rs.next() && rs2.next()){
                if(rs2.getString("EST_VEN_ESTADO").contentEquals("Activo")){
                    if(rs.getString("EST_VEN_NOMBRE").contentEquals("Recibido") || rs.getString("EST_VEN_NOMBRE").contentEquals("No Encontrado") || rs.getString("EST_VEN_NOMBRE").contentEquals("Anulado") || rs.getString("EST_VEN_NOMBRE").contentEquals("Rechazado") || rs.getString("EST_VEN_NOMBRE").contentEquals("Despacho Pendiente")){
                        combobox_Estado_Entrega.addItem(rs.getString("EST_VEN_NOMBRE"));
                    }
                }
            }
        } catch (Exception e) {}
    }
    
    
    
    
    public DefaultTableModel Mostrar_Lista_Ventas_Pendientes(){

        String [] registros = new String[7];
        tabla_ventas_pendientes.setRowCount(0);
        String sql = "select VTA_ID_VENTA, EST_VEN_NOMBRE, VTA_FECHA_VENTA, CLI_NOMBRE, fk_CLI_ID_RUT_CLIENTE, CLI_TELEFONO, VTA_TOTAL, PCK_NOMBRE\n" +
                     "from venta inner join cliente on venta.fk_CLI_ID_RUT_CLIENTE = cliente.CLI_ID_RUT_CLIENTE\n" +
                     "inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK \n" +
                     "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN\n" +
                     "WHERE EST_VEN_NOMBRE = 'Pago Pendiente'   order by VTA_ID_VENTA;";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
     
            while(rs.next()){

                if(rs.getString("EST_VEN_NOMBRE").contentEquals("Pago Pendiente")){
                    registros[0] = rs.getString("VTA_ID_VENTA");
                    registros[1] = rs.getString("VTA_FECHA_VENTA");                   
                    registros[2] = rs.getString("CLI_NOMBRE");
                    registros[3] = rs.getString("fk_CLI_ID_RUT_CLIENTE");
                    registros[4] = rs.getString("CLI_TELEFONO");
                    registros[5] = rs.getString("VTA_TOTAL");
                    registros[6] = rs.getString("PCK_NOMBRE");    
                }
                tabla_ventas_pendientes.addRow(registros);    
                
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        }
        
        addCheckBox_VentasPendientes(7, table_Ventas_Pendientes);
 

        return tabla_ventas_pendientes;
    }
    public DefaultTableModel Buscar_Lista_Ventas_Pendientes(String buscar){

        String [] registros = new String[7];
        tabla_ventas_pendientes.setRowCount(0);
        String sql = "select EST_VEN_NOMBRE, VTA_ID_VENTA, EST_VEN_EST_ID_ESTADO, VTA_FECHA_VENTA, CLI_NOMBRE, fk_CLI_ID_RUT_CLIENTE, CLI_TELEFONO, VTA_TOTAL, PCK_NOMBRE\n" +
                    "from venta \n" +
                    "inner join cliente on venta.fk_CLI_ID_RUT_CLIENTE = cliente.CLI_ID_RUT_CLIENTE\n" +
                    "inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK  \n" +
                    "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN   \n" +
                    "WHERE  EST_VEN_NOMBRE = 'Pago Pendiente' AND VTA_ID_VENTA LIKE '%"+buscar+"%' \n" +
                    "order by VTA_ID_VENTA;";
        
        
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
     
            while(rs.next()){

                if(!rs.getString("EST_VEN_EST_ID_ESTADO").contentEquals("Completado")){
 
                    registros[0] = rs.getString("VTA_ID_VENTA");
                    registros[1] = rs.getString("VTA_FECHA_VENTA");                   
                    registros[2] = rs.getString("CLI_NOMBRE");
                    registros[3] = rs.getString("fk_CLI_ID_RUT_CLIENTE");
                    registros[4] = rs.getString("CLI_TELEFONO");
                    registros[5] = rs.getString("VTA_TOTAL");
                    registros[6] = rs.getString("PCK_NOMBRE");
                }
                tabla_ventas_pendientes.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        }
        
        addCheckBox_VentasPendientes(7, table_Ventas_Pendientes);
        return tabla_ventas_pendientes;
    }

    
    public DefaultTableModel Mostrar_Lista_Destinos(){
        
        
        String [] registros = new String[7];
        tabla_lista_destinos.setRowCount(0);
        String sql = "select 	EST_VEN_NOMBRE, VTA_ID_VENTA, VTA_NOMBRE_DESTINATARIO, VTA_FECHA_ENTREGA, COM_NOMBRE, VTA_DIRECCION_DESTINATARIO, VTA_HORA_ENTREGA_INICIAL, VTA_HORA_ENTREGA_FINAL, PCK_NOMBRE\n" +
                     "from venta inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK inner join comunas on venta.COM_ID_COMUNA = comunas.COM_ID_COMUNA\n" +
                     "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN\n" +
                     "WHERE  EST_VEN_NOMBRE = 'Despacho Pendiente'      order by VTA_ID_VENTA;";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        String aux;
        String aux2;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
     
            while(rs.next()){

                if(rs.getString("EST_VEN_NOMBRE").contentEquals("Despacho Pendiente")){
 
                    registros[0] = rs.getString("VTA_ID_VENTA");
                    registros[1] = rs.getString("PCK_NOMBRE");                   
                    registros[2] = rs.getString("VTA_NOMBRE_DESTINATARIO");
                    registros[3] = rs.getString("VTA_FECHA_ENTREGA");
                    registros[4] = rs.getString("COM_NOMBRE");
                    registros[5] = rs.getString("VTA_DIRECCION_DESTINATARIO");
                    aux = rs.getString("VTA_HORA_ENTREGA_INICIAL");
                    aux2 = rs.getString("VTA_HORA_ENTREGA_FINAL");
                    registros[6] = aux + " a " +aux2;
                }
                else{
                    tabla_lista_destinos.setRowCount(0);
                }
                tabla_lista_destinos.addRow(registros); 
                
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        }
        
        //addCheckBox_VentasPendientes(7, table_Lista_Destinos);
        return tabla_lista_destinos;
    }
    public DefaultTableModel Buscar_Lista_Destinos(String buscar){
        
        String [] registros = new String[7];
        tabla_lista_destinos.setRowCount(0);
        String sql = "select 	EST_VEN_NOMBRE, VTA_ID_VENTA, VTA_NOMBRE_DESTINATARIO, VTA_FECHA_ENTREGA, COM_NOMBRE, VTA_DIRECCION_DESTINATARIO, VTA_HORA_ENTREGA_INICIAL, VTA_HORA_ENTREGA_FINAL, PCK_NOMBRE\n" +
                     "from venta inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK inner join comunas on venta.COM_ID_COMUNA = comunas.COM_ID_COMUNA\n" +
                     "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN\n" +
                     "WHERE  EST_VEN_NOMBRE = 'Despacho Pendiente'   AND  VTA_FECHA_ENTREGA LIKE '%"+buscar+"%'    order by VTA_ID_VENTA;";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        String aux;
        String aux2;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
     
            while(rs.next()){

                if(rs.getString("EST_VEN_NOMBRE").contentEquals("Despacho Pendiente")){
 
                    registros[0] = rs.getString("VTA_ID_VENTA");
                    registros[1] = rs.getString("PCK_NOMBRE");                   
                    registros[2] = rs.getString("VTA_NOMBRE_DESTINATARIO");
                    registros[3] = rs.getString("VTA_FECHA_ENTREGA");
                    registros[4] = rs.getString("COM_NOMBRE");
                    registros[5] = rs.getString("VTA_DIRECCION_DESTINATARIO");
                    aux = rs.getString("VTA_HORA_ENTREGA_INICIAL");
                    aux2 = rs.getString("VTA_HORA_ENTREGA_FINAL");
                    registros[6] = aux + " a " +aux2;
                }
              
                tabla_lista_destinos.addRow(registros); 
                
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        }
        
        //addCheckBox_VentasPendientes(7, table_Lista_Destinos);
        return tabla_lista_destinos;
    }
    
    
    public DefaultTableModel Mostrar_Actualizacion_Despacho(){
           
        String [] registros = new String[7];
        tabla_actualizacion_despacho.setRowCount(0);
        String sql = "select 	EST_VEN_NOMBRE, VTA_ID_VENTA, VTA_NOMBRE_DESTINATARIO, VTA_FECHA_ENTREGA, COM_NOMBRE, VTA_HORA_ENTREGA_INICIAL, VTA_HORA_ENTREGA_FINAL, PCK_NOMBRE\n" +
                     "from venta \n" +
                     "inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK \n" +
                     "inner join comunas on venta.COM_ID_COMUNA = comunas.COM_ID_COMUNA\n" +
                     "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN\n" +
                     " WHERE  EST_VEN_NOMBRE = 'Despacho Pendiente' " +
                     "OR EST_VEN_NOMBRE = 'Recibido' OR EST_VEN_NOMBRE = 'No Encontrado' OR EST_VEN_NOMBRE = 'Anulado' OR EST_VEN_NOMBRE = 'Rechazado' " +
                     "order by VTA_ID_VENTA;";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        String aux;
        String aux2;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
     
            while(rs.next()){

                    registros[0] = rs.getString("VTA_ID_VENTA");
                    registros[1] = rs.getString("PCK_NOMBRE");                   
                    registros[2] = rs.getString("VTA_NOMBRE_DESTINATARIO");
                    registros[3] = rs.getString("VTA_FECHA_ENTREGA");
                    registros[4] = rs.getString("COM_NOMBRE");     
                    aux = rs.getString("VTA_HORA_ENTREGA_INICIAL");
                    aux2 = rs.getString("VTA_HORA_ENTREGA_FINAL");
                    registros[5] = aux + " a " +aux2;
                    registros[6] = rs.getString("EST_VEN_NOMBRE");
               
                tabla_actualizacion_despacho.addRow(registros); 
                
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        }
        
        addCheckBox_ActualizacionDespacho(7, table_Actualizacion_Despacho);
        return tabla_actualizacion_despacho;
    }
    public DefaultTableModel Buscar_Actualizacion_Despacho(String buscar){
        String [] registros = new String[7];
        tabla_actualizacion_despacho.setRowCount(0);
        String sql = "select 	EST_VEN_NOMBRE, VTA_ID_VENTA, VTA_NOMBRE_DESTINATARIO, VTA_FECHA_ENTREGA, COM_NOMBRE, VTA_HORA_ENTREGA_INICIAL, VTA_HORA_ENTREGA_FINAL, PCK_NOMBRE\n" +
                     "from venta \n" +
                     "inner join pack on venta.PCK_ID_PACK = pack.PCK_ID_PACK \n" +
                     "inner join comunas on venta.COM_ID_COMUNA = comunas.COM_ID_COMUNA\n" +
                     "inner join estados_venta on venta.EST_VEN_EST_ID_ESTADO = estados_venta.EST_ID_EST_VEN\n" +
                     " WHERE VTA_FECHA_ENTREGA LIKE '%"+buscar+"%' AND (EST_VEN_NOMBRE = 'Despacho Pendiente' " +
                     "OR EST_VEN_NOMBRE = 'Recibido' OR EST_VEN_NOMBRE = 'No Encontrado' OR EST_VEN_NOMBRE = 'Anulado' OR EST_VEN_NOMBRE = 'Rechazado'  OR EST_VEN_NOMBRE = 'Despacho Pendiente')" +
                     " order by VTA_ID_VENTA;";
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        String aux;
        String aux2;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
     
            while(rs.next()){

                    registros[0] = rs.getString("VTA_ID_VENTA");
                    registros[1] = rs.getString("PCK_NOMBRE");                   
                    registros[2] = rs.getString("VTA_NOMBRE_DESTINATARIO");
                    registros[3] = rs.getString("VTA_FECHA_ENTREGA");
                    registros[4] = rs.getString("COM_NOMBRE");     
                    aux = rs.getString("VTA_HORA_ENTREGA_INICIAL");
                    aux2 = rs.getString("VTA_HORA_ENTREGA_FINAL");
                    registros[5] = aux + " a " +aux2;
                    registros[6] = rs.getString("EST_VEN_NOMBRE");
               
                tabla_actualizacion_despacho.addRow(registros); 
                
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        }
        
        addCheckBox_ActualizacionDespacho(7, table_Actualizacion_Despacho);
        return tabla_actualizacion_despacho;
    }
    
    
    
    
    
    public void addCheckBox_VentasPendientes(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_ActualizacionDespacho(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public boolean IsSelected(int row, int column, JTable table){
        return table.getValueAt(row, column) != null;
    }
    
   
    
    
    
    private void Confirmar_Venta(){
        
        String SQL_banco = "SELECT BAN_NOMBRE FROM bancos ORDER BY BAN_ID_BANCO";
        String SQL2_banco = "SELECT BAN_ID_BANCO FROM bancos ORDER BY BAN_ID_BANCO";
         
        String SQL_ven = "SELECT EST_VEN_NOMBRE FROM estados_venta ORDER BY EST_ID_EST_VEN";
        String SQL2_ven = "SELECT EST_ID_EST_VEN FROM estados_venta ORDER BY EST_ID_EST_VEN";
        
        String aux2;
        try {
            
            PreparedStatement pst_nom_ban = conect.prepareStatement(SQL_banco);
            ResultSet rs_nom_ban = pst_nom_ban.executeQuery();
            PreparedStatement pst_id_ban = conect.prepareStatement(SQL2_banco);
            ResultSet rs_id_ban = pst_id_ban.executeQuery();
            
            PreparedStatement pst_nom_ven = conect.prepareStatement(SQL_ven);
            ResultSet rs_nom_ven = pst_nom_ven.executeQuery();
            PreparedStatement pst_id_ven = conect.prepareStatement(SQL2_ven);
            ResultSet rs_id_ven = pst_id_ven.executeQuery();
            
            
            PreparedStatement editar = conect.prepareStatement("UPDATE venta SET VTA_FECHA_TRANSFERENCIA =?, VTA_CODIGO_TRANSFERENCIA =?,BAN_ID_BANCO =?,EST_VEN_EST_ID_ESTADO =? WHERE  VTA_ID_VENTA =?");
            
            editar.setString(1, ((JTextField) date_Fecha_Pago_Confirmacion.getDateEditor().getUiComponent()).getText());
            editar.setString(2, txt_Codigo_Transaccion_Confirmacion.getText());
            
            while(rs_nom_ban.next() && rs_id_ban.next()){
                aux2 = (String)combobox_Banco.getSelectedItem();
                if(rs_nom_ban.getString("BAN_NOMBRE").contentEquals(aux2)){
                    //aux2 = rs_id_rrss.getString("RRS_ID_RRSS");
                    editar.setString(3, rs_id_ban.getString("BAN_ID_BANCO"));
                }   
            }
            
            while(rs_nom_ven.next() && rs_id_ven.next()){
                //aux4 = txt_Nombre_Cliente.getText();
                if(rs_nom_ven.getString("EST_VEN_NOMBRE").contentEquals("Despacho Pendiente")){
                    //aux2 = rs_id_rrss.getString("RRS_ID_RRSS");
                    editar.setString(4, rs_id_ven.getString("EST_ID_EST_VEN"));
                }   
            }
            
         
            
            editar.setString(5, txt_Rut_Cliente_Confirmacion.getText());
            //editar.setString(5, ((JTextField)date_F_Nacimiento_Cliente.getDateEditor().getUiComponent()).getText());                 
            //editar.setString(6, (String) combobox_Comuna_Cliente.getSelectedItem());                
            //editar.setString(7, txt_Celular_Cliente.getText());
            //editar.setString(8, (String) combobox_Estado_Cliente.getSelectedItem());      
            //editar.setString(9, txt_Rut_Cliente.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Rut_Cliente_Confirmacion.setText("");
        txt_Nombre_Cliente_Confirmacion.setText("");
        txt_Codigo_Transaccion_Confirmacion.setText("");
        date_Fecha_Pago_Confirmacion.setDateFormatString("");
        txt_Numero_Pedido_Confirmacion.setText("");
        Mostrar_Lista_Ventas_Pendientes();      
    } 
    
    private void Modificar_Confirmar_Venta(){
        int i =0;
        while( i<tabla_ventas_pendientes.getRowCount()){
            if(IsSelected(i, 7, table_Ventas_Pendientes)){
                txt_Numero_Pedido_Confirmacion.setText((String) table_Ventas_Pendientes.getValueAt(i, 0));
                txt_Rut_Cliente_Confirmacion.setText((String) table_Ventas_Pendientes.getValueAt(i, 3));
                txt_Nombre_Cliente_Confirmacion.setText((String) table_Ventas_Pendientes.getValueAt(i, 2));
            }
            i++;
        }
    }
    
    
    
    
    
    
    private void Editar_Actualizacion_Despacho(){
        
        String SQL_ven = "SELECT EST_VEN_NOMBRE FROM estados_venta ORDER BY EST_ID_EST_VEN";
        String SQL2_ven = "SELECT EST_ID_EST_VEN FROM estados_venta ORDER BY EST_ID_EST_VEN";
        String aux;
        String aux2;
        
        try {
            
            PreparedStatement pst_nom_ven = conect.prepareStatement(SQL_ven);
            ResultSet rs_nom_ven = pst_nom_ven.executeQuery();
            PreparedStatement pst_id_ven = conect.prepareStatement(SQL2_ven);
            ResultSet rs_id_ven = pst_id_ven.executeQuery();
            
            PreparedStatement editar = conect.prepareStatement("UPDATE venta SET VTA_ID_VENTA =?, EST_VEN_EST_ID_ESTADO =? WHERE VTA_ID_VENTA =?");
  
            editar.setString(1, txt_ttt.getText());
            while(rs_id_ven.next() && rs_nom_ven.next()){
                aux = (String)combobox_Estado_Entrega.getSelectedItem();
                if(rs_nom_ven.getString("EST_VEN_NOMBRE").contentEquals(aux)){
                    aux2 = rs_id_ven.getString("EST_ID_EST_VEN");
                    editar.setString(2, aux2);
                }   
            }
 
            editar.setString(3, txt_ttt.getText());

            
            editar.executeUpdate(); 
            
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        
        Mostrar_Actualizacion_Despacho();
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        //txt_Codigo_Categoria_Articulo.setText("");
        //txt_Nombre_Categoria_Articulo.setText("");
        //Mostrar_Lista_Categoria_Articulo(); 
    }
     
    private void Modificar_Actualizacion_Despacho(){
        int i =0;
        //String numeropedido;
        while( i<tabla_actualizacion_despacho.getRowCount()){
            if(IsSelected(i, 7, table_Actualizacion_Despacho)){
                txt_ttt.setText((String) table_Actualizacion_Despacho.getValueAt(i, 0));
                combobox_Estado_Entrega.setSelectedItem(table_Actualizacion_Despacho.getValueAt(i, 6));
            }                
            i++;
        }
    }
    
    
    
    
     
     
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        menuBar2 = new java.awt.MenuBar();
        menu3 = new java.awt.Menu();
        menu4 = new java.awt.Menu();
        menuBar3 = new java.awt.MenuBar();
        menu5 = new java.awt.Menu();
        menu6 = new java.awt.Menu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jLabel17 = new javax.swing.JLabel();
        TabbedPane_VENTAS = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_Nombre_Cliente = new javax.swing.JTextField();
        txt_Email_Cliente = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_Rut_Cliente = new javax.swing.JTextField();
        txt_Telefono_Cliente = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        combobox_redes_sociales = new javax.swing.JComboBox<>();
        date_Fecha_Venta = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        btn_Completar_Datos = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txt_Nombre_Destinatario = new javax.swing.JTextField();
        txt_Direccion_Destinatario = new javax.swing.JTextField();
        combobox_Comuna = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_Saludo = new javax.swing.JTextArea();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        combobox_Pack = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        txt_Costo_Total = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txt_Telefono_Destinatario = new javax.swing.JTextField();
        combobox_Hora_Inicio = new javax.swing.JComboBox<>();
        combobox_Hora_Final = new javax.swing.JComboBox<>();
        date_Fecha_Entrega = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        btn_Guardar_Venta = new javax.swing.JButton();
        btn_Cancelar_Venta = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_Numero_Pedido_Confirmacion = new javax.swing.JTextField();
        txt_Rut_Cliente_Confirmacion = new javax.swing.JTextField();
        combobox_Banco = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_Nombre_Cliente_Confirmacion = new javax.swing.JTextField();
        txt_Codigo_Transaccion_Confirmacion = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        btn_Confirmación = new javax.swing.JButton();
        date_Fecha_Pago_Confirmacion = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        txt_Buscar_Ventas_Pendientes_Pago = new javax.swing.JTextField();
        btn_Seleccionar_Confirmación = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_Ventas_Pendientes = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_Buscador_Lista_Destinos = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_Lista_Destinos = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt_Buscar_Actualizacion_Despacho = new javax.swing.JTextField();
        btn_Descargar_Actualizacion_Despacho = new javax.swing.JButton();
        combobox_Estado_Entrega = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_Actualizacion_Despacho = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        btn_Editar_Actualizacion_Despacho = new javax.swing.JButton();
        btn_Guardar_Cambio = new javax.swing.JButton();
        txt_ttt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        MENU_VENTAS = new javax.swing.JMenu();
        MENU_COMPRAS = new javax.swing.JMenu();
        MENU_INFORMES = new javax.swing.JMenu();
        MENU_MAESTROS = new javax.swing.JMenu();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        menu3.setLabel("File");
        menuBar2.add(menu3);

        menu4.setLabel("Edit");
        menuBar2.add(menu4);

        menu5.setLabel("File");
        menuBar3.add(menu5);

        menu6.setLabel("Edit");
        menuBar3.add(menu6);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jMenu3.setText("jMenu3");

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenu4.setText("jMenu4");

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        jCheckBoxMenuItem2.setSelected(true);
        jCheckBoxMenuItem2.setText("jCheckBoxMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText("jRadioButtonMenuItem2");

        jMenuItem4.setText("jMenuItem4");

        jMenu7.setText("jMenu7");

        jLabel17.setText("jLabel17");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 153, 255));

        TabbedPane_VENTAS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabbedPane_VENTASMouseClicked(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setText("Nombre Cliente:");

        jLabel14.setText("Email:");

        jLabel15.setText("Rut:");

        jLabel16.setText("Teléfono:");

        jLabel34.setText("Red Social:");

        date_Fecha_Venta.setDateFormatString("yyyy-MM-dd");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setText("Fecha Venta:");

        btn_Completar_Datos.setText("Completar Datos");
        btn_Completar_Datos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Completar_DatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel30))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_Nombre_Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addComponent(txt_Email_Cliente))
                    .addComponent(date_Fecha_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Telefono_Cliente)
                    .addComponent(txt_Rut_Cliente)
                    .addComponent(combobox_redes_sociales, 0, 135, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btn_Completar_Datos)
                .addGap(54, 54, 54))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txt_Rut_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Completar_Datos)
                    .addComponent(jLabel13)
                    .addComponent(txt_Nombre_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txt_Telefono_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txt_Email_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(combobox_redes_sociales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(date_Fecha_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("Nombre Destinatario:");

        jLabel21.setText("Fecha Entrega:");

        jLabel22.setText("Dirección:");

        jLabel23.setText("Comuna:");

        jLabel24.setText("Saludo:");

        txt_Saludo.setColumns(20);
        txt_Saludo.setRows(5);
        jScrollPane4.setViewportView(txt_Saludo);

        jLabel25.setText("Pack:");

        jLabel26.setText("Hora Inicio Entrega:");

        jLabel27.setText("Hora Fin Entrega:");

        jLabel28.setText("Costo total:");

        txt_Costo_Total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Costo_TotalActionPerformed(evt);
            }
        });

        jLabel29.setText("Teléfono:");

        txt_Telefono_Destinatario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Telefono_DestinatarioActionPerformed(evt);
            }
        });

        combobox_Hora_Inicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:00 am", "09:00 am", "10:00 am", "11:00 am", "12:00 am", "13:00 pm", "14:00 pm", "15:00 pm", "16:00 pm", "17:00 pm", "18:00 pm", "19:00 pm", "20:00 pm" }));

        combobox_Hora_Final.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:00 am", "09:00 am", "10:00 am", "11:00 am", "12:00 am", "13:00 pm", "14:00 pm", "15:00 pm", "16:00 pm", "17:00 pm", "18:00 pm", "19:00 pm", "20:00 pm" }));

        date_Fecha_Entrega.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txt_Direccion_Destinatario, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20)
                                .addComponent(jLabel21))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(txt_Nombre_Destinatario, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(date_Fecha_Entrega, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txt_Telefono_Destinatario, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addGap(97, 97, 97)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel28))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(combobox_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txt_Costo_Total, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(combobox_Hora_Final, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(combobox_Hora_Inicio, javax.swing.GroupLayout.Alignment.LEADING, 0, 100, Short.MAX_VALUE))))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(combobox_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(113, 113, 113))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txt_Nombre_Destinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(combobox_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel26)
                        .addComponent(combobox_Hora_Inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(date_Fecha_Entrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txt_Direccion_Destinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(combobox_Hora_Final, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_Costo_Total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(txt_Telefono_Destinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(combobox_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jLabel18.setText("Datos Cliente Solicitante");

        jLabel19.setText("Datos Destinatario");

        btn_Guardar_Venta.setText("Guardar");
        btn_Guardar_Venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_VentaActionPerformed(evt);
            }
        });

        btn_Cancelar_Venta.setText("Cancelar");
        btn_Cancelar_Venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_VentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Cancelar_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancelar_Venta)
                    .addComponent(btn_Guardar_Venta))
                .addContainerGap())
        );

        TabbedPane_VENTAS.addTab("Venta", jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("Número Pedido:");

        jLabel6.setText("Rut:");

        jLabel7.setText("Banco Cliente:");

        jLabel8.setText("Nombre Cliente:");

        jLabel9.setText("Fehca Pago:");

        jLabel10.setText("Código Transacción:");

        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btn_Confirmación.setText("Confirmación");
        btn_Confirmación.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ConfirmaciónActionPerformed(evt);
            }
        });

        date_Fecha_Pago_Confirmacion.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_Numero_Pedido_Confirmacion, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                    .addComponent(txt_Rut_Cliente_Confirmacion)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(combobox_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(75, 75, 75)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_Nombre_Cliente_Confirmacion)
                                .addComponent(date_Fecha_Pago_Confirmacion, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                            .addComponent(txt_Codigo_Transaccion_Confirmacion)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5)))
                .addGap(28, 28, 28)
                .addComponent(btn_Confirmación)
                .addGap(29, 29, 29))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_Numero_Pedido_Confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txt_Nombre_Cliente_Confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txt_Rut_Cliente_Confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addComponent(date_Fecha_Pago_Confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(combobox_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txt_Codigo_Transaccion_Confirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(btn_Confirmación))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Ventas pendiente de pago");

        txt_Buscar_Ventas_Pendientes_Pago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Buscar_Ventas_Pendientes_PagoKeyReleased(evt);
            }
        });

        btn_Seleccionar_Confirmación.setText("Seleccionar");
        btn_Seleccionar_Confirmación.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Seleccionar_ConfirmaciónActionPerformed(evt);
            }
        });

        jLabel31.setText("Confirma Pago Cliente");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText(" Buscar:");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        table_Ventas_Pendientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Número Pedido", "Fecha Pedido", "Nombre Cliente", "Rut Cliente", "Número Teléfono", "Monto", "Pack", "Selección"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(table_Ventas_Pendientes);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txt_Buscar_Ventas_Pendientes_Pago, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 730, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addGap(0, 638, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Seleccionar_Confirmación, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txt_Buscar_Ventas_Pendientes_Pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Seleccionar_Confirmación)
                .addContainerGap(379, Short.MAX_VALUE))
        );

        TabbedPane_VENTAS.addTab("Confirmación", jPanel1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Lista destinos para despacho por día");

        txt_Buscador_Lista_Destinos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Buscador_Lista_DestinosKeyReleased(evt);
            }
        });

        jButton1.setText("Descargar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel32.setText("Despacho (Busqueda por fecha)");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Destinos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Número Pedido", "Pack", "Destinatario", "Fecha Entrega", "Comuna", "Dirección", "Hora Entrega"
            }
        ));
        jScrollPane1.setViewportView(table_Lista_Destinos);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel35.setText("Buscar:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel35)
                        .addGap(18, 18, 18)
                        .addComponent(txt_Buscador_Lista_Destinos, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_Buscador_Lista_Destinos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel35))
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(442, Short.MAX_VALUE))
        );

        TabbedPane_VENTAS.addTab("Lista Destinos", jPanel4);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Actualización Estado Despacho");

        txt_Buscar_Actualizacion_Despacho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_Buscar_Actualizacion_DespachoKeyReleased(evt);
            }
        });

        btn_Descargar_Actualizacion_Despacho.setText("Descargar");
        btn_Descargar_Actualizacion_Despacho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Descargar_Actualizacion_DespachoActionPerformed(evt);
            }
        });

        combobox_Estado_Entrega.setToolTipText("");
        combobox_Estado_Entrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combobox_Estado_EntregaActionPerformed(evt);
            }
        });

        jLabel33.setText("Despacho (Busqueda por fecha)");

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        table_Actualizacion_Despacho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Numero Pedido", "Pack", "Destinatario", "Fecha Entrega", "Comuna", "Hora entrega", "Estado Entrega", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table_Actualizacion_Despacho.setName(""); // NOI18N
        jScrollPane2.setViewportView(table_Actualizacion_Despacho);
        table_Actualizacion_Despacho.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel36.setText("Buscar:");

        btn_Editar_Actualizacion_Despacho.setText("Editar");
        btn_Editar_Actualizacion_Despacho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_Actualizacion_DespachoActionPerformed(evt);
            }
        });

        btn_Guardar_Cambio.setText("Guardar Cambio");
        btn_Guardar_Cambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_CambioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(311, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(175, 175, 175))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel36)
                        .addGap(18, 18, 18)
                        .addComponent(txt_Buscar_Actualizacion_Despacho, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 291, Short.MAX_VALUE)
                        .addComponent(txt_ttt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btn_Guardar_Cambio)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Editar_Actualizacion_Despacho)
                        .addGap(18, 18, 18)
                        .addComponent(combobox_Estado_Entrega, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Descargar_Actualizacion_Despacho, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel36)
                    .addComponent(txt_Buscar_Actualizacion_Despacho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combobox_Estado_Entrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Editar_Actualizacion_Despacho)
                    .addComponent(btn_Guardar_Cambio)
                    .addComponent(txt_ttt, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(btn_Descargar_Actualizacion_Despacho)
                .addContainerGap(345, Short.MAX_VALUE))
        );

        TabbedPane_VENTAS.addTab("Actualización Despacho", jPanel5);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        jLabel2.setText("Dream Gift");

        MENU_VENTAS.setText("Ventas");
        jMenuBar1.add(MENU_VENTAS);

        MENU_COMPRAS.setText("Compras");
        jMenuBar1.add(MENU_COMPRAS);

        MENU_INFORMES.setText("Informes");
        jMenuBar1.add(MENU_INFORMES);

        MENU_MAESTROS.setText("Maestros");
        MENU_MAESTROS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MENU_MAESTROSMouseClicked(evt);
            }
        });
        jMenuBar1.add(MENU_MAESTROS);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabbedPane_VENTAS)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(233, 233, 233)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TabbedPane_VENTAS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void combobox_Estado_EntregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_Estado_EntregaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combobox_Estado_EntregaActionPerformed

    private void btn_Cancelar_VentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_VentaActionPerformed
        //txt_Numero_Pedido.setText("");
        txt_Rut_Cliente.setText("");
        txt_Nombre_Cliente.setText("");
        txt_Telefono_Cliente.setText("");
        txt_Email_Cliente.setText("");
        date_Fecha_Venta.setDateFormatString("");
        
        txt_Nombre_Destinatario.setText("");
        date_Fecha_Entrega.setDateFormatString("");
        txt_Direccion_Destinatario.setText("");
        txt_Telefono_Destinatario.setText("");
        txt_Saludo.setText("");
        txt_Costo_Total.setText("");
        
    }//GEN-LAST:event_btn_Cancelar_VentaActionPerformed

    private void txt_Costo_TotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Costo_TotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Costo_TotalActionPerformed

    private void MENU_MAESTROSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MENU_MAESTROSMouseClicked
        // TODO add your handling code here:
        Maestros maestros = new Maestros();
        maestros.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_MENU_MAESTROSMouseClicked
    
    private void bloquear_estadoentrega(){
        combobox_Estado_Entrega.setEnabled(false);}
    private void desbloquear_Estadoentrega(){
        combobox_Estado_Entrega.setEnabled(true);}
    private void bloquear_guardarestadoentrega(){
        btn_Guardar_Cambio.setEnabled(false);}
    private void desbloquear_guardarestadoentrega(){
        btn_Guardar_Cambio.setEnabled(true);}
    
    private void TabbedPane_VENTASMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabbedPane_VENTASMouseClicked
        //Consultar_RRSSS();
        //Consultar_Comuna();
        //Consultar_Pack();
        //Consultar_Banco();
        //Consultar_Estado_Venta();
        //Mostrar_Lista_Ventas_Pendientes();
        //Mostrar_Lista_Destinos();
        //Mostrar_Actualizacion_Despacho();
        //bloquear_estadoentrega();
        //bloquear_guardarestadoentrega();
        //txt_Nombre_Cliente.setEnabled(false);
        //txt_Email_Cliente.setEnabled(false);
        //txt_Telefono_Cliente.setEnabled(false);
        //txt_Numero_Pedido_Confirmacion.setEnabled(false);
        //txt_Nombre_Cliente_Confirmacion.setEnabled(false);
        //txt_Rut_Cliente_Confirmacion.setEnabled(false);
        
    }//GEN-LAST:event_TabbedPane_VENTASMouseClicked

    private void btn_Guardar_VentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_VentaActionPerformed
        
        
        String SQL_cliente = "SELECT CLI_NOMBRE FROM cliente ORDER BY CLI_ID_RUT_CLIENTE";
        String SQL2_cliente = "SELECT CLI_ID_RUT_CLIENTE FROM cliente ORDER BY CLI_ID_RUT_CLIENTE";
        String SQL_rrss = "SELECT RRS_NOMBRE FROM rrss ORDER BY RRS_ID_RRSS";
        String SQL2_rrss = "SELECT RRS_ID_RRSS FROM rrss ORDER BY RRS_ID_RRSS";
        String SQL_comuna = "SELECT COM_NOMBRE FROM comunas ORDER BY COM_ID_COMUNA";
        String SQL2_comuna = "SELECT COM_ID_COMUNA FROM comunas ORDER BY COM_ID_COMUNA";
        String SQL_pack = "SELECT PCK_NOMBRE FROM pack ORDER BY PCK_ID_PACK";
        String SQL2_pack = "SELECT PCK_ID_PACK FROM pack ORDER BY PCK_ID_PACK";
        String SQL3_pack = "SELECT PCK_STOCK FROM pack ORDER BY PCK_ID_PACK";
        String SQL_ven = "SELECT EST_VEN_NOMBRE FROM estados_venta ORDER BY EST_ID_EST_VEN";
        String SQL2_ven = "SELECT EST_ID_EST_VEN FROM estados_venta ORDER BY EST_ID_EST_VEN";
        
        
        String aux;
        String aux2;
        String aux3;
        String aux4;
        
        try {
            
            PreparedStatement pst_nom_cli = conect.prepareStatement(SQL_cliente);
            ResultSet rs_nom_cli = pst_nom_cli.executeQuery();
            PreparedStatement pst_id_cli = conect.prepareStatement(SQL2_cliente);
            ResultSet rs_id_cli = pst_id_cli.executeQuery();
            
            PreparedStatement pst_nom_rrss = conect.prepareStatement(SQL_rrss);
            ResultSet rs_nom_rrss = pst_nom_rrss.executeQuery();
            PreparedStatement pst_id_rrss = conect.prepareStatement(SQL2_rrss);
            ResultSet rs_id_rrss = pst_id_rrss.executeQuery();
            
            PreparedStatement pst_nom_com = conect.prepareStatement(SQL_comuna);
            ResultSet rs_nom_com = pst_nom_com.executeQuery();
            PreparedStatement pst_id_com = conect.prepareStatement(SQL2_comuna);
            ResultSet rs_id_com = pst_id_com.executeQuery();
            
            PreparedStatement pst_nom_pack = conect.prepareStatement(SQL_pack);
            ResultSet rs_nom_pack = pst_nom_pack.executeQuery();
            PreparedStatement pst_id_pack = conect.prepareStatement(SQL2_pack);
            ResultSet rs_id_pack = pst_id_pack.executeQuery();
            PreparedStatement pst_stock_pack = conect.prepareStatement(SQL3_pack);
            ResultSet rs_stock_pack = pst_stock_pack.executeQuery();
            
            PreparedStatement pst_nom_ven = conect.prepareStatement(SQL_ven);
            ResultSet rs_nom_ven = pst_nom_ven.executeQuery();
            PreparedStatement pst_id_ven = conect.prepareStatement(SQL2_ven);
            ResultSet rs_id_ven = pst_id_ven.executeQuery();
            
            
            
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO venta (VTA_FECHA_VENTA, VTA_NOMBRE_DESTINATARIO, VTA_FECHA_ENTREGA, VTA_DIRECCION_DESTINATARIO, VTA_TELEFONO_DESTINATARIO, VTA_SALUDO, VTA_TOTAL, VTA_HORA_ENTREGA_INICIAL, VTA_HORA_ENTREGA_FINAL, RRSS_RRS_ID_RRSS, COM_ID_COMUNA, PCK_ID_PACK, fk_CLI_ID_RUT_CLIENTE, EST_VEN_EST_ID_ESTADO) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            //guardar.setString(1, txt_Numero_Pedido.getText());
            guardar.setString(1, ((JTextField)date_Fecha_Venta.getDateEditor().getUiComponent()).getText());
            guardar.setString(2, txt_Nombre_Destinatario.getText());
            guardar.setString(3, ((JTextField)date_Fecha_Entrega.getDateEditor().getUiComponent()).getText());
            guardar.setString(4, txt_Direccion_Destinatario.getText());
            guardar.setString(5, txt_Telefono_Destinatario.getText());
            guardar.setString(6, txt_Saludo.getText());
            guardar.setString(7, txt_Costo_Total.getText());
            guardar.setString(8, (String) combobox_Hora_Inicio.getSelectedItem());
            guardar.setString(9, (String) combobox_Hora_Final.getSelectedItem());

            
            while(rs_nom_rrss.next() && rs_id_rrss.next()){
                aux = (String)combobox_redes_sociales.getSelectedItem();
                if(rs_nom_rrss.getString("RRS_NOMBRE").contentEquals(aux)){
                    //aux2 = rs_id_rrss.getString("RRS_ID_RRSS");
                    guardar.setString(10, rs_id_rrss.getString("RRS_ID_RRSS"));
                }   
            }

            
            while(rs_nom_com.next() && rs_id_com.next()){
                aux2 = (String)combobox_Comuna.getSelectedItem();
                if(rs_nom_com.getString("COM_NOMBRE").contentEquals(aux2)){
                    //aux2 = rs_id_rrss.getString("RRS_ID_RRSS");
                    guardar.setString(11, rs_id_com.getString("COM_ID_COMUNA"));
                }   
            }

            
            
            PreparedStatement editar = conect.prepareStatement("UPDATE pack SET PCK_ID_PACK=?, PCK_STOCK=? WHERE PCK_ID_PACK=?");
            
            
            while(rs_nom_pack.next() && rs_id_pack.next() && rs_stock_pack.next()){
                aux3 = (String)combobox_Pack.getSelectedItem();
                if(rs_nom_pack.getString("PCK_NOMBRE").contentEquals(aux3)){
                    //aux2 = rs_id_rrss.getString("RRS_ID_RRSS");
                    guardar.setString(12, rs_id_pack.getString("PCK_ID_PACK"));
                    
                    int x = 0;
                    String y = "";
                    x = rs_stock_pack.getInt("PCK_STOCK")-1;
                    editar.setString(1, rs_id_pack.getString("PCK_ID_PACK"));
                    editar.setInt(2, x);
                    editar.setString(3, rs_id_pack.getString("PCK_ID_PACK"));
                }   
            }
            
            
            while(rs_nom_cli.next() && rs_id_cli.next()){
                aux4 = txt_Nombre_Cliente.getText();
                if(rs_nom_cli.getString("CLI_NOMBRE").contentEquals(aux4)){
                    //aux2 = rs_id_rrss.getString("RRS_ID_RRSS");
                    guardar.setString(13, rs_id_cli.getString("CLI_ID_RUT_CLIENTE"));
                }   
            }
            
            while(rs_nom_ven.next() && rs_id_ven.next()){
                //aux4 = txt_Nombre_Cliente.getText();
                if(rs_nom_ven.getString("EST_VEN_NOMBRE").contentEquals("Pago Pendiente")){
                    //aux2 = rs_id_rrss.getString("RRS_ID_RRSS");
                    guardar.setString(14, rs_id_ven.getString("EST_ID_EST_VEN"));
                }   
            }
           
            //guardar.setString(11, (String) combobox_redes_sociales.getSelectedItem());
            //guardar.setString(12, (String) combobox_Comuna.getSelectedItem());
            //guardar.setString(13, (String) combobox_Pack.getSelectedItem());
            //guardar.setString(14, txt_Rut_Cliente.getText());
            
            guardar.executeUpdate();
            editar.executeUpdate();
            
            
            
            
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }
        
        //txt_Numero_Pedido.setText("");
        txt_Rut_Cliente.setText("");
        txt_Nombre_Cliente.setText("");
        txt_Telefono_Cliente.setText("");
        txt_Email_Cliente.setText("");
        //date_Fecha_Venta.setDateFormatString("");
        
        txt_Nombre_Destinatario.setText("");
        //date_Fecha_Entrega.setDateFormatString("");
        txt_Direccion_Destinatario.setText("");
        txt_Telefono_Destinatario.setText("");
        txt_Saludo.setText("");
        txt_Costo_Total.setText("");
            
        
        
        
        
        
        
    }//GEN-LAST:event_btn_Guardar_VentaActionPerformed

    private void btn_Completar_DatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Completar_DatosActionPerformed
         
        String SQL_cliente = "SELECT CLI_NOMBRE FROM cliente ORDER BY CLI_ID_RUT_CLIENTE";
        String SQL2_cliente = "SELECT CLI_ID_RUT_CLIENTE FROM cliente ORDER BY CLI_ID_RUT_CLIENTE";
        String SQL3_cliente = "SELECT CLI_TELEFONO FROM cliente ORDER BY CLI_ID_RUT_CLIENTE";            
        String SQL4_cliente = "SELECT CLI_CORREO FROM cliente ORDER BY CLI_ID_RUT_CLIENTE";
        try {
            PreparedStatement pst_nom_cli = conect.prepareStatement(SQL_cliente);
            ResultSet rs_nom_cli = pst_nom_cli.executeQuery();
            PreparedStatement pst_id_cli = conect.prepareStatement(SQL2_cliente);
            ResultSet rs_id_cli = pst_id_cli.executeQuery();
            PreparedStatement pst_tel_cli = conect.prepareStatement(SQL3_cliente);
            ResultSet rs_tel_cli = pst_tel_cli.executeQuery();
            PreparedStatement pst_ema_cli = conect.prepareStatement(SQL4_cliente);
            ResultSet rs_ema_cli = pst_ema_cli.executeQuery();
            
            while(rs_id_cli.next() && rs_nom_cli.next() && rs_tel_cli.next() && rs_ema_cli.next()){
                if(rs_id_cli.getString("CLI_ID_RUT_CLIENTE").contentEquals(txt_Rut_Cliente.getText())){
                    txt_Nombre_Cliente.setText(rs_nom_cli.getString("CLI_NOMBRE"));
                    txt_Email_Cliente.setText(rs_ema_cli.getString("CLI_CORREO"));
                    txt_Telefono_Cliente.setText(rs_tel_cli.getString("CLI_TELEFONO"));
                }
                    
            }
            
            txt_Nombre_Cliente.setEnabled(false);
            txt_Email_Cliente.setEnabled(false);
            txt_Telefono_Cliente.setEnabled(false);
            
        } 
        catch (Exception e) {
        }
            
        
        
        
    }//GEN-LAST:event_btn_Completar_DatosActionPerformed

    private void txt_Telefono_DestinatarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Telefono_DestinatarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Telefono_DestinatarioActionPerformed

    private void btn_ConfirmaciónActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ConfirmaciónActionPerformed
        Confirmar_Venta();
    }//GEN-LAST:event_btn_ConfirmaciónActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btn_Seleccionar_ConfirmaciónActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Seleccionar_ConfirmaciónActionPerformed
        Modificar_Confirmar_Venta();
        txt_Numero_Pedido_Confirmacion.setEnabled(false);
        txt_Nombre_Cliente_Confirmacion.setEnabled(false);
        txt_Rut_Cliente_Confirmacion.setEnabled(false);
    }//GEN-LAST:event_btn_Seleccionar_ConfirmaciónActionPerformed

    private void txt_Buscador_Lista_DestinosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Buscador_Lista_DestinosKeyReleased
        Buscar_Lista_Destinos(txt_Buscador_Lista_Destinos.getText());
    }//GEN-LAST:event_txt_Buscador_Lista_DestinosKeyReleased

    private void btn_Editar_Actualizacion_DespachoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_Actualizacion_DespachoActionPerformed
        desbloquear_Estadoentrega();
        desbloquear_guardarestadoentrega();
        Modificar_Actualizacion_Despacho();
        
    }//GEN-LAST:event_btn_Editar_Actualizacion_DespachoActionPerformed

    private void btn_Guardar_CambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_CambioActionPerformed
        Editar_Actualizacion_Despacho();
    }//GEN-LAST:event_btn_Guardar_CambioActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Imprimir_Lista_Destinos(txt_Buscador_Lista_Destinos.getText());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_Buscar_Actualizacion_DespachoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Buscar_Actualizacion_DespachoKeyReleased
        Buscar_Actualizacion_Despacho(txt_Buscar_Actualizacion_Despacho.getText());
    }//GEN-LAST:event_txt_Buscar_Actualizacion_DespachoKeyReleased

    private void btn_Descargar_Actualizacion_DespachoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Descargar_Actualizacion_DespachoActionPerformed
        Imprimir_Actualizacion_Despacho(txt_Buscar_Actualizacion_Despacho.getText());
    }//GEN-LAST:event_btn_Descargar_Actualizacion_DespachoActionPerformed

    private void txt_Buscar_Ventas_Pendientes_PagoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Buscar_Ventas_Pendientes_PagoKeyReleased
        Buscar_Lista_Ventas_Pendientes(txt_Buscar_Ventas_Pendientes_Pago.getText());
        
    }//GEN-LAST:event_txt_Buscar_Ventas_Pendientes_PagoKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MENU_COMPRAS;
    private javax.swing.JMenu MENU_INFORMES;
    private javax.swing.JMenu MENU_MAESTROS;
    private javax.swing.JMenu MENU_VENTAS;
    private javax.swing.JTabbedPane TabbedPane_VENTAS;
    private javax.swing.JButton btn_Cancelar_Venta;
    private javax.swing.JButton btn_Completar_Datos;
    private javax.swing.JButton btn_Confirmación;
    private javax.swing.JButton btn_Descargar_Actualizacion_Despacho;
    private javax.swing.JButton btn_Editar_Actualizacion_Despacho;
    private javax.swing.JButton btn_Guardar_Cambio;
    private javax.swing.JButton btn_Guardar_Venta;
    private javax.swing.JButton btn_Seleccionar_Confirmación;
    private javax.swing.JComboBox<String> combobox_Banco;
    private javax.swing.JComboBox<String> combobox_Comuna;
    private javax.swing.JComboBox<String> combobox_Estado_Entrega;
    private javax.swing.JComboBox<String> combobox_Hora_Final;
    private javax.swing.JComboBox<String> combobox_Hora_Inicio;
    private javax.swing.JComboBox<String> combobox_Pack;
    private javax.swing.JComboBox<String> combobox_redes_sociales;
    private com.toedter.calendar.JDateChooser date_Fecha_Entrega;
    private com.toedter.calendar.JDateChooser date_Fecha_Pago_Confirmacion;
    private com.toedter.calendar.JDateChooser date_Fecha_Venta;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.Menu menu3;
    private java.awt.Menu menu4;
    private java.awt.Menu menu5;
    private java.awt.Menu menu6;
    private java.awt.MenuBar menuBar1;
    private java.awt.MenuBar menuBar2;
    private java.awt.MenuBar menuBar3;
    private javax.swing.JTable table_Actualizacion_Despacho;
    private javax.swing.JTable table_Lista_Destinos;
    private javax.swing.JTable table_Ventas_Pendientes;
    private javax.swing.JTextField txt_Buscador_Lista_Destinos;
    private javax.swing.JTextField txt_Buscar_Actualizacion_Despacho;
    private javax.swing.JTextField txt_Buscar_Ventas_Pendientes_Pago;
    private javax.swing.JTextField txt_Codigo_Transaccion_Confirmacion;
    private javax.swing.JTextField txt_Costo_Total;
    private javax.swing.JTextField txt_Direccion_Destinatario;
    private javax.swing.JTextField txt_Email_Cliente;
    private javax.swing.JTextField txt_Nombre_Cliente;
    private javax.swing.JTextField txt_Nombre_Cliente_Confirmacion;
    private javax.swing.JTextField txt_Nombre_Destinatario;
    private javax.swing.JTextField txt_Numero_Pedido_Confirmacion;
    private javax.swing.JTextField txt_Rut_Cliente;
    private javax.swing.JTextField txt_Rut_Cliente_Confirmacion;
    private javax.swing.JTextArea txt_Saludo;
    private javax.swing.JTextField txt_Telefono_Cliente;
    private javax.swing.JTextField txt_Telefono_Destinatario;
    private javax.swing.JTextField txt_ttt;
    // End of variables declaration//GEN-END:variables
}

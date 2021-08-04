/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;

import com.mysql.cj.result.DefaultValueFactory;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import java.text.SimpleDateFormat;
import java.time.Clock;
import javax.swing.JComboBox;
import javax.swing.JTextField;
/**
 *
 * @author Usuario
 */
public class Maestros extends javax.swing.JFrame {

    /**
     * Creates new form Maestros
     * @param args
     */
    
    //PARA MOSTRAR INFO DE BASE DE DATOS EN LA TABLA
    DBConeccion con = new DBConeccion();
    Connection conect = con.conectar();
   
    //////////////////////////
    
    DefaultTableModel tabla_clientes=new DefaultTableModel();
    DefaultTableModel tabla_comunas=new DefaultTableModel();
    DefaultTableModel tabla_bancos=new DefaultTableModel();
    DefaultTableModel tabla_redes_sociales=new DefaultTableModel();
    DefaultTableModel tabla_usuarios=new DefaultTableModel();
    DefaultTableModel tabla_proveedores=new DefaultTableModel();
    DefaultTableModel tabla_categoria_articulo=new DefaultTableModel();
    DefaultTableModel tabla_categoria_venta=new DefaultTableModel();
    DefaultTableModel tabla_articulos=new DefaultTableModel();
    DefaultTableModel tabla_pack=new DefaultTableModel();
    //String [] auxcomuna = new String[100];
    ////////////////////////////////////////////////////////////////////////////
    DefaultListModel lista_articulo_pack=new DefaultListModel();
    
    
    public Maestros() {
        initComponents();
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA CLIENTES"
        tabla_clientes.addColumn("Rut");
        tabla_clientes.addColumn("Nombre Cliente");
        tabla_clientes.addColumn("Telefono");
        tabla_clientes.addColumn("Email");
        tabla_clientes.addColumn("Fecha Nacimiento");
        tabla_clientes.addColumn("Comuna");
        tabla_clientes.addColumn("Celular");
        tabla_clientes.addColumn("Estado");
        tabla_clientes.addColumn("Acción");
        this.table_Lista_Clientes.setModel(tabla_clientes);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA PROVEEDORES"
        tabla_proveedores.addColumn("Rut");
        tabla_proveedores.addColumn("Nombre Proveedor");
        tabla_proveedores.addColumn("Telefono");
        tabla_proveedores.addColumn("Email");
        tabla_proveedores.addColumn("Dirección");
        tabla_proveedores.addColumn("Estado");
        tabla_proveedores.addColumn("Acción");
        this.table_Lista_Proveedores.setModel(tabla_proveedores);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA CATEGORIA ARTICULOS"
        tabla_categoria_articulo.addColumn("Código Categoría");
        tabla_categoria_articulo.addColumn("Nombre Categoría");
        tabla_categoria_articulo.addColumn("Estado");
        tabla_categoria_articulo.addColumn("Acción");
        this.table_Lista_Categoria_Articulo.setModel(tabla_categoria_articulo);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA COMUNAS"
        tabla_comunas.addColumn("Código Comuna");
        tabla_comunas.addColumn("Nombre Comuna");
        tabla_comunas.addColumn("Estado");
        tabla_comunas.addColumn("Acción");
        this.table_Lista_Comunas.setModel(tabla_comunas);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA BANCOS"
        tabla_bancos.addColumn("Codigo Banco");
        tabla_bancos.addColumn("Nombre Banco");
        tabla_bancos.addColumn("Estado");
        tabla_bancos.addColumn("Acción");
        this.table_Lista_Bancos.setModel(tabla_bancos);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA REDES SOCIALES"
        tabla_redes_sociales.addColumn("Codigo RRSS");
        tabla_redes_sociales.addColumn("Nombre RRSS");
        tabla_redes_sociales.addColumn("Estado");
        tabla_redes_sociales.addColumn("Acción");
        this.table_Lista_RRSS.setModel(tabla_redes_sociales);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA USUARIOS"
        tabla_usuarios.addColumn("Nombre Usuario");
        tabla_usuarios.addColumn("Acción");
        this.table_Lista_Usuarios.setModel(tabla_usuarios);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA CATEGORIA VENTA"
        tabla_categoria_venta.addColumn("Codigo Categoría Venta");
        tabla_categoria_venta.addColumn("categoría Venta");
        tabla_categoria_venta.addColumn("Estado");
        tabla_categoria_venta.addColumn("Acción");
        this.table_Lista_Categoria_Venta.setModel(tabla_categoria_venta);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA ARTICULOS"
        tabla_articulos.addColumn("Código");
        tabla_articulos.addColumn("Nombre Artículo");
        tabla_articulos.addColumn("Descripción");
        tabla_articulos.addColumn("Categoría");
        tabla_articulos.addColumn("Marca");
        tabla_articulos.addColumn("F. Vencimiento");
        tabla_articulos.addColumn("Estado");
        tabla_articulos.addColumn("Acción");
        this.table_Lista_Articulos.setModel(tabla_articulos);
        //SE CREAN LAS COLUMNAS DE LA TABLA "LISTA PACK"
        tabla_pack.addColumn("Código");
        tabla_pack.addColumn("Nombre Pack");
        tabla_pack.addColumn("Stock");
        tabla_pack.addColumn("Precio");
        tabla_pack.addColumn("Estado");
        tabla_pack.addColumn("Acción");
        this.table_Lista_Pack.setModel(tabla_pack);
        
        
        
        lista_articulo_pack = new DefaultListModel();
        list_Articulos_Pack.setModel(lista_articulo_pack);
        
        
        bloquear_Actualizar_Cliente();
        bloquear_Actualizar_Proveedor();
        bloquear_Actualizar_RRSS();
        bloquear_Actualizar_Categoria_Articulo();
        bloquear_Actualizar_Comuna();
        bloquear_Actualizar_Banco();
        bloquear_Actualizar_Categoria_Venta();
        bloquear_Actualizar_Articulo();
        
        
        
    }

        
        
    
    public DefaultTableModel Mostrar_Lista_Clientes(){
       
        //String [] nombre_columnas = {"Rut","Nombre","Telefono","Email","Fecha Nacimiento","Comuna","Celular"};
        String [] registros = new String[8];
        
        tabla_clientes.setRowCount(0);
        
        String sql = "SELECT * FROM cliente";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("CLI_ID_RUT_CLIENTE");
                registros[1] = rs.getString("CLI_NOMBRE");
                registros[2] = rs.getString("CLI_TELEFONO");
                registros[3] = rs.getString("CLI_CORREO");
                registros[4] = rs.getString("CLI_F_NACIMIENTO");
                registros[5] = rs.getString("CLI_COMUNA");
                registros[6] = rs.getString("CLI_CELULAR");
                registros[7] = rs.getString("CLI_ESTADO");
                tabla_clientes.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        
        }
        
        addCheckBox_Cliente(8, table_Lista_Clientes);
        return tabla_clientes;
    }
    
    public DefaultTableModel Mostrar_Lista_Proveedores(){
        String [] registros = new String[6];
        
        tabla_proveedores.setRowCount(0);
        
        String sql = "SELECT * FROM proveedor";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("PRO_ID_RUT_PROVEEDOR");
                registros[1] = rs.getString("PRO_NOMBRE");
                registros[2] = rs.getString("PRO_TELEFONO");
                registros[3] = rs.getString("PRO_CORREO");
                registros[4] = rs.getString("PRO_DIRECCION");
                registros[5] = rs.getString("PRO_ESTADO");
                tabla_proveedores.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        
        addCheckBox_Proveedor(6, table_Lista_Proveedores);
        return tabla_proveedores;
    }
    
    public DefaultTableModel Mostrar_Lista_Categoria_Articulo(){
        String [] registros = new String[3];
        tabla_categoria_articulo.setRowCount(0);
        String sql = "SELECT * FROM categoria_articulo";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("CAT_ID_CATEGORIA");
                registros[1] = rs.getString("CAT_NOMBRE");
                registros[2] = rs.getString("CAT_ESTADO");
                tabla_categoria_articulo.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        addCheckBox_Categoria_Articulo(3, table_Lista_Categoria_Articulo);
        return tabla_categoria_articulo;
    }
    
    public DefaultTableModel Mostrar_Lista_Comunas(){
        String [] registros = new String[3];
        tabla_comunas.setRowCount(0);
        String sql = "SELECT * FROM comunas";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("COM_ID_COMUNA");
                registros[1] = rs.getString("COM_NOMBRE");
                registros[2] = rs.getString("COM_ESTADO");
                tabla_comunas.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        addCheckBox_Proveedor(3, table_Lista_Comunas);
        return tabla_comunas;
    }
    
    public DefaultTableModel Mostrar_Lista_Bancos(){
        String [] registros = new String[3];
        tabla_bancos.setRowCount(0);
        String sql = "SELECT * FROM bancos";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("BAN_ID_BANCO");
                registros[1] = rs.getString("BAN_NOMBRE");
                registros[2] = rs.getString("BAN_ESTADO");
                tabla_bancos.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        addCheckBox_Banco(3, table_Lista_Bancos);
        return tabla_bancos;
    }
    
    public DefaultTableModel Mostrar_Lista_RRSS(){
        String [] registros = new String[3];
        tabla_redes_sociales.setRowCount(0);
        String sql = "SELECT * FROM RRSS";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("RRS_ID_RRSS");
                registros[1] = rs.getString("RRS_NOMBRE");
                registros[2] = rs.getString("RRS_ESTADO");
                tabla_redes_sociales.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        addCheckBox_RRSS(3, table_Lista_RRSS);
        return tabla_redes_sociales;
    }
    
    public DefaultTableModel Mostrar_Lista_Usuarios(){
        String [] registros = new String[1];
        tabla_usuarios.setRowCount(0);
        String sql = "SELECT * FROM usuarios";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("USU_NOMBRE");
                tabla_usuarios.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        addCheckBox_RRSS(1, table_Lista_Usuarios);
        return tabla_usuarios;
    }
    
    public DefaultTableModel Mostrar_Lista_Categoria_Venta(){
        String [] registros = new String[3];
        tabla_categoria_venta.setRowCount(0);
        String sql = "SELECT * FROM estados_venta";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("EST_ID_EST_VEN");
                registros[1] = rs.getString("EST_VEN_NOMBRE");
                registros[2] = rs.getString("EST_VEN_ESTADO");
                tabla_categoria_venta.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        addCheckBox_Categoria_Venta(3, table_Lista_Categoria_Venta);
        return tabla_categoria_venta;
    }
    
    public DefaultTableModel Mostrar_Lista_Articulo(){
        String [] registros = new String[7];
        tabla_articulos.setRowCount(0);
        //String SQL1 = "SELECT CAT_NOMBRE FROM categoria_articulo";
        //String SQL2 = "SELECT CAT_ID_CATEGORIA FROM categoria_articulo";
        //String sql = "SELECT * FROM articulo";
        String sql = "select ART_ID_ARTICULO, ART_NOMBRE, ART_DESCRPCION, CAT_NOMBRE, ART_FECHA_VENCIMIENTO, ART_ESTADO, ART_MARCA\n" +
        "\n" + "from articulo inner join categoria_articulo\n" +
        "\n" + "on articulo.CAT_ART_CAT_ID_CATEGORIA = categoria_articulo.CAT_ID_CATEGORIA;";
        PreparedStatement pst = null;
        ResultSet rs = null;
        String aux = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            
            //PreparedStatement pst_nom = conect.prepareStatement(SQL1);
            //ResultSet rs_nom = pst_nom.executeQuery();     
            //PreparedStatement pst_id = conect.prepareStatement(SQL2);
            //ResultSet rs_id = pst_id.executeQuery();
            
            while(rs.next()){
                registros[0] = rs.getString("ART_ID_ARTICULO");
                registros[1] = rs.getString("ART_NOMBRE");
                registros[2] = rs.getString("ART_DESCRPCION");
                
                /*while (rs_id.next() && rs_nom.next()){
                    if(rs.getString("CAT_ART_CAT_ID_CATEGORIA").equals(rs_id.getString("CAT_ID_CATEGORIA"))){
                        System.out.println("goku 1");
                        aux = rs_nom.getString("CAT_NOMBRE");
                        System.out.println("goku 2");
                    }
                }*/
                registros[3] = rs.getString("CAT_NOMBRE");
                registros[4] = rs.getString("ART_MARCA");
                registros[5] = rs.getString("ART_FECHA_VENCIMIENTO");
                registros[6] = rs.getString("ART_ESTADO");
                tabla_articulos.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla");
        
        }
        
        addCheckBox_Articulo(7, table_Lista_Articulos);
        return tabla_articulos;
    }
    
    public DefaultTableModel Mostrar_Lista_Pack(){
        String [] registros = new String[5];
        tabla_pack.setRowCount(0);
        String sql = "SELECT * FROM pack";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conect.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                registros[0] = rs.getString("PCK_ID_PACK");
                registros[1] = rs.getString("PCK_NOMBRE");
                registros[2] = rs.getString("PCK_STOCK");
                registros[3] = rs.getString("PCK_COSTO");
                registros[4] = rs.getString("PCK_ESTADO");
                tabla_pack.addRow(registros); 
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "No se pudo mostrar tabla"); 
        }
        addCheckBox_Pack(5, table_Lista_Pack);
        return tabla_pack;    
    }
    
    
    
    private void Agregar_Articulos_Lista_pack(){
        lista_articulo_pack.setSize(0);
        lista_articulo_pack.addElement("SENTENCIA SQL");
    }
    
    
    
    
    
    public void addCheckBox_Cliente(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Proveedor(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Comuna(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Categoria_Articulo(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Banco(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_RRSS(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Usuario(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Categoria_Venta(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Articulo(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    public void addCheckBox_Pack(int column, JTable table){
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    ////////////////////////////////////////////////////////////////////////////
    public boolean IsSelected(int row, int column, JTable table){
        return table.getValueAt(row, column) != null;
    }
    ////////////////////////////////////////////////////////////////////////////
     
    
    
    
    
    private void bloquear_Actualizar_Cliente(){
        btn_Actualizar_Cliente.setEnabled(false);}
    private void bloquear_Actualizar_Proveedor(){
        btn_Actualizar_Proveedor.setEnabled(false);}
    private void bloquear_Actualizar_RRSS(){
        btn_Actualizar_RRSS.setEnabled(false);}
    private void bloquear_Actualizar_Categoria_Articulo(){
        btn_Actualiizar_Categoria_Articulo.setEnabled(false);}
    private void bloquear_Actualizar_Comuna(){
        btn_Actualizar_Comuna.setEnabled(false);}
    private void bloquear_Actualizar_Banco(){
         btn_Actualizar_Banco.setEnabled(false);}
    private void bloquear_Actualizar_Categoria_Venta(){
         btn_Actualizar_Categoría_Venta.setEnabled(false);}
    private void bloquear_Actualizar_Articulo(){
        btn_Actualizar_Articulo.setEnabled(false);
    }
    
    
    
    
    private void bloquear_Guardar_Cliente(){
        btn_Guardar_Cliente.setEnabled(false);}
    private void bloquear_Guardar_Proveedor(){
        btn_Guardar_Proveedor.setEnabled(false);}
    private void bloquear_Guardar_RRSS(){
        btn_Guardar_RRSS.setEnabled(false);}
    private void bloquear_Guardar_Categoria_Articulo(){
        btn_Guardar_Categoria_Articulo.setEnabled(false);}
    private void bloquear_Guardar_Comuna(){
        btn_Guardar_Comuna.setEnabled(false);}
    private void bloquear_Guardar_Banco(){
         btn_Guardar_Banco.setEnabled(false);}
    private void bloquear_Guardar_Categoria_Venta(){
         btn_Guardar_Categoria_Venta.setEnabled(false);}
    private void bloquear_Guardar_Usuario(){
         btn_Guardar_Usuario.setEnabled(false);}
    
    
    
    
    
    
    
    /////////////////////////////////////////////////////////////////////
    private void Desbloquear_Actualizar_Cliente(){
        btn_Actualizar_Cliente.setEnabled(true);}
    private void Desbloquear_Actualizar_Proveedor(){
        btn_Actualizar_Proveedor.setEnabled(true);}
    private void Desbloquear_Actualizar_RRSS(){
        btn_Actualizar_RRSS.setEnabled(true);}
    private void Desbloquear_Actualizar_Categoria_Articulo(){
        btn_Actualiizar_Categoria_Articulo.setEnabled(true);}
    private void Desbloquear_Actualizar_Comuna(){
        btn_Actualizar_Comuna.setEnabled(true);}
    private void Desbloquear_Actualizar_Banco(){
        btn_Actualizar_Banco.setEnabled(true);}
    private void Desbloquear_Actualizar_Categoria_Venta(){
        btn_Actualizar_Categoría_Venta.setEnabled(true);}
    private void Desbloquear_Actualizar_Articulo(){
        btn_Actualizar_Articulo.setEnabled(true);}
    
    
    
    
    public void Consultar_Categoria_Articulo (){
        String SQL = "SELECT CAT_NOMBRE FROM categoria_articulo";
        String SQL2 = "SELECT CAT_ESTADO FROM categoria_articulo";
        combobox_Categoria_Articulo_ART.removeAllItems();
        try {
            PreparedStatement pst = conect.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2 = conect.prepareStatement(SQL2);
            ResultSet rs2 = pst2.executeQuery();
            combobox_Categoria_Articulo_ART.addItem("Seleccione una opción"); 
            while(rs.next() && rs2.next()){
                if(rs2.getString("CAT_ESTADO").contentEquals("Activo")){
                    combobox_Categoria_Articulo_ART.addItem(rs.getString("CAT_NOMBRE"));
                }
            }
        } 
        catch (Exception e) {}
    }
    
    /*public void Consultar_Proveedor_Articulo(){
        String SQL = "SELECT PRO_NOMBRE FROM proveedor";
        String SQL2 = "SELECT PRO_ESTADO FROM proveedor";
        combobox_Proveedor_Articulo.removeAllItems();
        try {
            PreparedStatement pst = conect.prepareStatement(SQL);
            ResultSet rs = pst.executeQuery();
            PreparedStatement pst2 = conect.prepareStatement(SQL2);
            ResultSet rs2 = pst2.executeQuery();
            combobox_Proveedor_Articulo.addItem("Seleccione una opción");            
            while(rs.next() && rs2.next()){
                if(rs2.getString("PRO_ESTADO").contentEquals("Activo")){
                    combobox_Proveedor_Articulo.addItem(rs.getString("PRO_NOMBRE"));
                }
            }
        } 
        catch (Exception e) {}
    }*/
    
    
 
    
    
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuBar4 = new javax.swing.JMenuBar();
        jMenu7 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenuBar5 = new javax.swing.JMenuBar();
        jMenu9 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        tabbedPane_MAESTROS = new javax.swing.JTabbedPane();
        Panel_Clientes = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btn_Guardar_Cliente = new javax.swing.JButton();
        btn_Cancelar_Cliente = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_Nombre_Cliente = new javax.swing.JTextField();
        txt_Telefono_Cliente = new javax.swing.JTextField();
        txt_Email_Cliente = new javax.swing.JTextField();
        txt_Rut_Cliente = new javax.swing.JTextField();
        txt_Celular_Cliente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        combobox_Estado_Cliente = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        date_F_Nacimiento_Cliente = new com.toedter.calendar.JDateChooser();
        combobox_Comuna_Cliente = new javax.swing.JComboBox<>();
        btn_Actualizar_Cliente = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_Lista_Clientes = new javax.swing.JTable();
        Label_Lista_Clientes = new javax.swing.JLabel();
        txt_Buscar_Lista_Cliente = new javax.swing.JTextField();
        btn_Buscar_Lista_Cliente = new javax.swing.JButton();
        btn_Venta_Cliente = new javax.swing.JButton();
        btn_Editar_Cliente = new javax.swing.JButton();
        Label_Clientes = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        btn_Guardar_Proveedor = new javax.swing.JButton();
        btn_Cancelar_Proveedor = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txt_Nombre_Proveedor = new javax.swing.JTextField();
        txt_Direccion_Proveedor = new javax.swing.JTextField();
        txt_Rut_Proveedor = new javax.swing.JTextField();
        txt_Telefono_Proveedor = new javax.swing.JTextField();
        txt_Email_Proveedor = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        combobox_Estado_Proveedor = new javax.swing.JComboBox<>();
        btn_Actualizar_Proveedor = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        table_Lista_Proveedores = new javax.swing.JTable();
        btn_Editar_Proveedor = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jTextField38 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_Nombre_Articulo = new javax.swing.JTextField();
        txt_Descripcion_Articulo = new javax.swing.JTextField();
        txt_Marca_Articulo = new javax.swing.JTextField();
        combobox_Categoria_Articulo_ART = new javax.swing.JComboBox<>();
        btn_Cancelar_Articulo = new javax.swing.JButton();
        btn_Guardar_Articulo = new javax.swing.JButton();
        date_Fecha_Vencimiento_Articulo = new com.toedter.calendar.JDateChooser();
        jLabel50 = new javax.swing.JLabel();
        txt_Codigo_Articulo = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        combobox_Estado_Articulo = new javax.swing.JComboBox<>();
        btn_Actualizar_Articulo = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_Lista_Articulos = new javax.swing.JTable();
        btn_editar_Articulo = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        txt_Nombre_Pack = new javax.swing.JTextField();
        btn_Cancelar_Pack = new javax.swing.JButton();
        btn_Crear_Pack = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        list_Articulos_Pack = new javax.swing.JList<>();
        btn_Agregar_Articulo = new javax.swing.JButton();
        spinner_Pack = new javax.swing.JSpinner();
        btn_Quitar_Articulo = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        btn_Actualizar_Pack = new javax.swing.JButton();
        txt_Costo_Pack = new javax.swing.JTextField();
        txt_Codigo_Pack = new javax.swing.JTextField();
        combobox_Estado_Pack = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txt_Stock_Pack = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        table_Lista_Pack = new javax.swing.JTable();
        Label_Lista_Clientes1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        btn_Editar_Pack = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txt_Nombre_Red_Social = new javax.swing.JTextField();
        btn_Guardar_RRSS = new javax.swing.JButton();
        btn_Cancelar_RRSS = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        txt_Codigo_Red_Social = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        combobox_Estado_Red_Social = new javax.swing.JComboBox<>();
        btn_Actualizar_RRSS = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_Lista_RRSS = new javax.swing.JTable();
        btn_Editar_RRSS = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txt_Nombre_Categoria_Articulo = new javax.swing.JTextField();
        btn_Cancelar_Categoria_Articulo = new javax.swing.JButton();
        btn_Guardar_Categoria_Articulo = new javax.swing.JButton();
        txtlabel = new javax.swing.JLabel();
        txt_Codigo_Categoria_Articulo = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        combobox_Estado_Categoria_Articulo = new javax.swing.JComboBox<>();
        btn_Actualiizar_Categoria_Articulo = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_Lista_Categoria_Articulo = new javax.swing.JTable();
        jTextField4 = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        btn_Editar_Categoria_Articulo = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        btn_Editar_Comunas = new javax.swing.JButton();
        jTextField22 = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        table_Lista_Comunas = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        txt_Nombre_Comuna = new javax.swing.JTextField();
        btn_Cancelar_Comuna = new javax.swing.JButton();
        btn_Guardar_Comuna = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txt_Codigo_Comuna = new javax.swing.JTextField();
        combobox_Estado_Comuna = new javax.swing.JComboBox<>();
        btn_Actualizar_Comuna = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        table_Lista_Bancos = new javax.swing.JTable();
        jTextField23 = new javax.swing.JTextField();
        jButton30 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        txt_Nombre_Banco = new javax.swing.JTextField();
        btn_Cancelar_Banco = new javax.swing.JButton();
        btn_Guardar_Banco = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txt_Codigo_Banco = new javax.swing.JTextField();
        combobox_Estado_Banco = new javax.swing.JComboBox<>();
        btn_Actualizar_Banco = new javax.swing.JButton();
        btn_Editar_Banco = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        btn_Editar_Categoria_Venta = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jTextField27 = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        txt_Nombre_Categoria_Venta = new javax.swing.JTextField();
        btn_Cancelar_Categoria_Venta = new javax.swing.JButton();
        btn_Guardar_Categoria_Venta = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        txt_Codigo_Categoria_Venta = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        combobox_Estado_Categoria_Venta = new javax.swing.JComboBox<>();
        btn_Actualizar_Categoría_Venta = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        table_Lista_Categoria_Venta = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        btn_Guardar_Usuario = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txt_Nombre_Usuario = new javax.swing.JTextField();
        pass_Ingreso_Clave = new javax.swing.JPasswordField();
        pass_Verificar_Clave = new javax.swing.JPasswordField();
        btn_Cancelar_Usuario = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        btn_Eliminar_Usuario = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jTextField30 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        table_Lista_Usuarios = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        MENU_VENTAS = new javax.swing.JMenu();
        MENU_COMPRAS = new javax.swing.JMenu();
        MENU_INFORMES = new javax.swing.JMenu();
        MENU_MAESTROS = new javax.swing.JMenu();

        jMenu5.setText("File");
        jMenuBar3.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar3.add(jMenu6);

        jMenu7.setText("File");
        jMenuBar4.add(jMenu7);

        jMenu8.setText("Edit");
        jMenuBar4.add(jMenu8);

        jMenu9.setText("File");
        jMenuBar5.add(jMenu9);

        jMenu10.setText("Edit");
        jMenuBar5.add(jMenu10);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(1);

        tabbedPane_MAESTROS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabbedPane_MAESTROSMouseClicked(evt);
            }
        });

        Panel_Clientes.setPreferredSize(new java.awt.Dimension(600, 300));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Telefono:");

        btn_Guardar_Cliente.setText("Guardar");
        btn_Guardar_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_ClienteActionPerformed(evt);
            }
        });

        btn_Cancelar_Cliente.setText("Cancelar");
        btn_Cancelar_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_ClienteActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nombre Cliente:");

        jLabel6.setText("Email:");

        jLabel7.setText("Rut:");

        jLabel10.setText("F. Nacimiento:");

        jLabel11.setText("Celular:");

        txt_Telefono_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Telefono_ClienteActionPerformed(evt);
            }
        });

        txt_Rut_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Rut_ClienteActionPerformed(evt);
            }
        });

        txt_Celular_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Celular_ClienteActionPerformed(evt);
            }
        });

        jLabel9.setText("(Ej: 12345678-9)");

        jLabel26.setText("Estado Cliente:");

        combobox_Estado_Cliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        jLabel24.setText("Comuna:");

        date_F_Nacimiento_Cliente.setDateFormatString("yyyy-MM-dd");

        combobox_Comuna_Cliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central", "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja", "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén", "Providencia", "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Joaquín", "San Miguel", "San Ramón", "Santiago", "Vitacura" }));
        combobox_Comuna_Cliente.setToolTipText("");
        combobox_Comuna_Cliente.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btn_Actualizar_Cliente.setText("Actualizar");
        btn_Actualizar_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_ClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Telefono_Cliente)
                    .addComponent(txt_Nombre_Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(txt_Email_Cliente)
                    .addComponent(combobox_Comuna_Cliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(100, 100, 100)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_Rut_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(combobox_Estado_Cliente, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_Celular_Cliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addComponent(date_F_Nacimiento_Cliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                .addGap(124, 124, 124))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Cancelar_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btn_Actualizar_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(txt_Nombre_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Rut_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(txt_Telefono_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(date_F_Nacimiento_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel11)
                    .addComponent(txt_Email_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Celular_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(combobox_Comuna_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(combobox_Estado_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24)))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Guardar_Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Actualizar_Cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Cancelar_Cliente))
                .addGap(15, 15, 15))
        );

        combobox_Comuna_Cliente.getAccessibleContext().setAccessibleName("");

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Rut", "Nombre Cliente", "Teléfono", "Email", "Fecha Nacimiento", "Comuna", "Celular", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table_Lista_Clientes.setFocusable(false);
        jScrollPane1.setViewportView(table_Lista_Clientes);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );

        Label_Lista_Clientes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Label_Lista_Clientes.setText("Lista Clientes");

        btn_Buscar_Lista_Cliente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_Buscar_Lista_Cliente.setText("Buscar");

        btn_Venta_Cliente.setText("Venta");

        btn_Editar_Cliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Editar_Cliente.setText("Editar");
        btn_Editar_Cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_ClienteActionPerformed(evt);
            }
        });

        Label_Clientes.setText("Cliente");

        javax.swing.GroupLayout Panel_ClientesLayout = new javax.swing.GroupLayout(Panel_Clientes);
        Panel_Clientes.setLayout(Panel_ClientesLayout);
        Panel_ClientesLayout.setHorizontalGroup(
            Panel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Panel_ClientesLayout.createSequentialGroup()
                        .addComponent(Label_Clientes)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ClientesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(Panel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ClientesLayout.createSequentialGroup()
                                .addComponent(btn_Venta_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_Editar_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_ClientesLayout.createSequentialGroup()
                                .addComponent(Label_Lista_Clientes)
                                .addGap(122, 122, 122)
                                .addComponent(txt_Buscar_Lista_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_Buscar_Lista_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        Panel_ClientesLayout.setVerticalGroup(
            Panel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_Clientes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Buscar_Lista_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Panel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Label_Lista_Clientes)
                        .addComponent(txt_Buscar_Lista_Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Panel_ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Venta_Cliente)
                    .addComponent(btn_Editar_Cliente))
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Clientes", Panel_Clientes);

        jLabel45.setText("Proveedor");

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        jLabel46.setText("Nombre Proveedor:");

        btn_Guardar_Proveedor.setText("Guardar");
        btn_Guardar_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_ProveedorActionPerformed(evt);
            }
        });

        btn_Cancelar_Proveedor.setText("Cancelar");
        btn_Cancelar_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_ProveedorActionPerformed(evt);
            }
        });

        jLabel47.setText("Rut Proveedor:");

        jLabel48.setText("Dirección:");

        jLabel52.setText("Teléfono:");

        jLabel53.setText("Email:");

        txt_Rut_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Rut_ProveedorActionPerformed(evt);
            }
        });

        txt_Email_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Email_ProveedorActionPerformed(evt);
            }
        });

        jLabel12.setText("(Ej: 12345678-9)");

        jLabel1.setText("Estado Proveedor:");

        combobox_Estado_Proveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btn_Actualizar_Proveedor.setText("Actualizar");
        btn_Actualizar_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_ProveedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_Cancelar_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Actualizar_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Guardar_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_Nombre_Proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(txt_Rut_Proveedor)
                            .addComponent(txt_Direccion_Proveedor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(100, 100, 100)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(combobox_Estado_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_Telefono_Proveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_Email_Proveedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel47)
                        .addComponent(txt_Rut_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel52)
                        .addComponent(txt_Telefono_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_Nombre_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel53)
                    .addComponent(txt_Email_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel48)
                        .addComponent(jLabel1)
                        .addComponent(combobox_Estado_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_Direccion_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Guardar_Proveedor)
                            .addComponent(btn_Actualizar_Proveedor)
                            .addComponent(btn_Cancelar_Proveedor))))
                .addGap(15, 15, 15))
        );

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel51.setText("Lista Proveedores");

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Proveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Rut", "Nombre Proveedor", "Teléfono", "Email", "Dirección", "Estado", "Accion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane9.setViewportView(table_Lista_Proveedores);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Editar_Proveedor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Editar_Proveedor.setText("Editar");
        btn_Editar_Proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_ProveedorActionPerformed(evt);
            }
        });

        jButton45.setText("Compra");

        jButton46.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton46.setText("Buscar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addGap(80, 80, 80)
                                .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton46))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_Editar_Proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel51)
                        .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Editar_Proveedor)
                    .addComponent(jButton45))
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Proveedores", jPanel3);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Descripción:");

        jLabel13.setText("Nombre Artículo:");

        jLabel14.setText("Marca:");

        jLabel15.setText("Categoría Artículo:");

        jLabel16.setText("F. Vencimiento:");

        combobox_Categoria_Articulo_ART.setToolTipText("");
        combobox_Categoria_Articulo_ART.setName(""); // NOI18N

        btn_Cancelar_Articulo.setText("Cancelar");
        btn_Cancelar_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_ArticuloActionPerformed(evt);
            }
        });

        btn_Guardar_Articulo.setText("Guardar");
        btn_Guardar_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_ArticuloActionPerformed(evt);
            }
        });

        date_Fecha_Vencimiento_Articulo.setDateFormatString("yyyy-MM-dd");

        jLabel50.setText("Código Artículo:");

        jLabel55.setText("Estado:");

        combobox_Estado_Articulo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btn_Actualizar_Articulo.setText("Actualizar");
        btn_Actualizar_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_ArticuloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_Descripcion_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Nombre_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Marca_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Codigo_Articulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(119, 119, 119)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(date_Fecha_Vencimiento_Articulo, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addComponent(combobox_Estado_Articulo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(combobox_Categoria_Articulo_ART, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_Cancelar_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Actualizar_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Guardar_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(combobox_Categoria_Articulo_ART, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel50)
                        .addComponent(txt_Codigo_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(date_Fecha_Vencimiento_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txt_Nombre_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Descripcion_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55)
                    .addComponent(combobox_Estado_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_Marca_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Cancelar_Articulo)
                    .addComponent(btn_Guardar_Articulo)
                    .addComponent(btn_Actualizar_Articulo))
                .addGap(15, 15, 15))
        );

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("Artículos");

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Articulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Descripción", "Categoría", "Marca", "F. Vencimiento", "Estado", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(table_Lista_Articulos);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_editar_Articulo.setText("Editar");
        btn_editar_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_ArticuloActionPerformed(evt);
            }
        });

        jLabel19.setText("Artículos");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_editar_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_editar_Articulo)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Artículos", jPanel4);

        jLabel49.setText("Pack");

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel57.setText("Nombre Pack:");

        btn_Cancelar_Pack.setText("Cancelar");
        btn_Cancelar_Pack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_PackActionPerformed(evt);
            }
        });

        btn_Crear_Pack.setText("Crear Pack");
        btn_Crear_Pack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Crear_PackActionPerformed(evt);
            }
        });

        jScrollPane10.setViewportView(list_Articulos_Pack);

        btn_Agregar_Articulo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_Agregar_Articulo.setText(">");
        btn_Agregar_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Agregar_ArticuloActionPerformed(evt);
            }
        });

        spinner_Pack.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        btn_Quitar_Articulo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_Quitar_Articulo.setText("<");
        btn_Quitar_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Quitar_ArticuloActionPerformed(evt);
            }
        });

        jScrollPane11.setViewportView(jList2);

        jLabel59.setText("Costo $:");

        jLabel60.setText("Estado Pack: ");

        jLabel61.setText("Código Pack:");

        btn_Actualizar_Pack.setText("Actualizar Pack");
        btn_Actualizar_Pack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_PackActionPerformed(evt);
            }
        });

        combobox_Estado_Pack.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        jLabel8.setText("Stock:");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jLabel57)
                                .addGap(18, 18, 18)
                                .addComponent(txt_Nombre_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(spinner_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_Agregar_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_Quitar_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(54, 54, 54)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_Costo_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                                .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                .addComponent(combobox_Estado_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_Codigo_Pack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Stock_Pack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(76, 76, 76)
                        .addComponent(btn_Cancelar_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Actualizar_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Crear_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(txt_Nombre_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Costo_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(btn_Agregar_Articulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinner_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_Quitar_Articulo)))
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Cancelar_Pack)
                            .addComponent(btn_Actualizar_Pack)
                            .addComponent(btn_Crear_Pack))
                        .addGap(37, 37, 37))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel61)
                            .addComponent(txt_Codigo_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txt_Stock_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel60)
                            .addComponent(combobox_Estado_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))))
        );

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Pack.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Nombre Pack", "Stock", "Precio", "Estado", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table_Lista_Pack.setFocusable(false);
        jScrollPane12.setViewportView(table_Lista_Pack);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Label_Lista_Clientes1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Label_Lista_Clientes1.setText("Lista Pack");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Buscar");

        btn_Editar_Pack.setText("Editar");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel49)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(292, 292, 292)
                        .addComponent(Label_Lista_Clientes1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Editar_Pack, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Label_Lista_Clientes1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Editar_Pack)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Packs", jPanel5);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setText("Nombre de la Red Social:");

        txt_Nombre_Red_Social.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Nombre_Red_SocialActionPerformed(evt);
            }
        });

        btn_Guardar_RRSS.setText("Guardar");

        btn_Cancelar_RRSS.setText("Cancelar");
        btn_Cancelar_RRSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_RRSSActionPerformed(evt);
            }
        });

        jLabel37.setText("Código Red Social:");

        jLabel44.setText("Estado Red Social:");

        combobox_Estado_Red_Social.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btn_Actualizar_RRSS.setText("Actualizar");
        btn_Actualizar_RRSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_RRSSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Nombre_Red_Social)
                    .addComponent(txt_Codigo_Red_Social)
                    .addComponent(combobox_Estado_Red_Social, 0, 125, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Cancelar_RRSS, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Actualizar_RRSS, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar_RRSS, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txt_Nombre_Red_Social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txt_Codigo_Red_Social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(combobox_Estado_Red_Social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Cancelar_RRSS)
                    .addComponent(btn_Guardar_RRSS)
                    .addComponent(btn_Actualizar_RRSS))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel22.setText("Redes Sociales");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setText("Redes Sociales");

        jButton13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton13.setText("Buscar");

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_RRSS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código RRSS", "Nombre RRSS", "Estado", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(table_Lista_RRSS);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_Editar_RRSS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Editar_RRSS.setText("Editar");
        btn_Editar_RRSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_RRSSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(109, 109, 109)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_Editar_RRSS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_Editar_RRSS)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("RRSS", jPanel6);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setText("Categoria Artículo:");

        txt_Nombre_Categoria_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Nombre_Categoria_ArticuloActionPerformed(evt);
            }
        });

        btn_Cancelar_Categoria_Articulo.setText("Cancelar");
        btn_Cancelar_Categoria_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_Categoria_ArticuloActionPerformed(evt);
            }
        });

        btn_Guardar_Categoria_Articulo.setText("Guardar");
        btn_Guardar_Categoria_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_Categoria_ArticuloActionPerformed(evt);
            }
        });

        txtlabel.setText("Código Categoria:");

        jLabel28.setText("Estado Categoría");

        combobox_Estado_Categoria_Articulo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btn_Actualiizar_Categoria_Articulo.setText("Actualizar");
        btn_Actualiizar_Categoria_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualiizar_Categoria_ArticuloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(txtlabel)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(combobox_Estado_Categoria_Articulo, 0, 125, Short.MAX_VALUE)
                            .addComponent(txt_Codigo_Categoria_Articulo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Cancelar_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Actualiizar_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Guardar_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(txt_Nombre_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txt_Nombre_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtlabel)
                            .addComponent(txt_Codigo_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(combobox_Estado_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_Cancelar_Categoria_Articulo)
                        .addComponent(btn_Actualiizar_Categoria_Articulo)
                        .addComponent(btn_Guardar_Categoria_Articulo)))
                .addGap(15, 15, 15))
        );

        jLabel25.setText("Categorías Artículos");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel27.setText("Categoría Artículo");

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Categoria_Articulo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código Categoría", "Código Artículo", "Estado", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(table_Lista_Categoria_Articulo);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton20.setText("Buscar");

        btn_Editar_Categoria_Articulo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Editar_Categoria_Articulo.setText("Editar");
        btn_Editar_Categoria_Articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_Categoria_ArticuloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton20)
                        .addGap(8, 8, 8))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_Editar_Categoria_Articulo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel27)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_Editar_Categoria_Articulo)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Categorías Artículos", jPanel7);

        jLabel33.setText("Comunas");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setText("Comunas Registradas");

        btn_Editar_Comunas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Editar_Comunas.setText("Editar");
        btn_Editar_Comunas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_ComunasActionPerformed(evt);
            }
        });

        jButton29.setText("Buscar");

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Comunas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código Comuna", "Nombre Comuna", "Estado", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(table_Lista_Comunas);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel54.setText("Nombre Communa:");

        txt_Nombre_Comuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Nombre_ComunaActionPerformed(evt);
            }
        });

        btn_Cancelar_Comuna.setText("Cancelar");
        btn_Cancelar_Comuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_ComunaActionPerformed(evt);
            }
        });

        btn_Guardar_Comuna.setText("Guardar");
        btn_Guardar_Comuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_ComunaActionPerformed(evt);
            }
        });

        jLabel17.setText("Código Comuna:");

        jLabel31.setText("Estado Comuna:");

        combobox_Estado_Comuna.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));
        combobox_Estado_Comuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combobox_Estado_ComunaActionPerformed(evt);
            }
        });

        btn_Actualizar_Comuna.setText("Actualizar");
        btn_Actualizar_Comuna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_ComunaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(combobox_Estado_Comuna, 0, 125, Short.MAX_VALUE)
                    .addComponent(txt_Nombre_Comuna, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(txt_Codigo_Comuna))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Cancelar_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Actualizar_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(txt_Nombre_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Guardar_Comuna)
                            .addComponent(btn_Actualizar_Comuna)
                            .addComponent(btn_Cancelar_Comuna)))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txt_Codigo_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(combobox_Estado_Comuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(249, 249, 249)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                        .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel33)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_Editar_Comunas, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29))
                .addGap(18, 18, 18)
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_Editar_Comunas)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Comunas", jPanel8);

        jLabel29.setText("Bancos");

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel30.setText("Bancos Registrados");

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Bancos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código Banco", "Nombre Banco", "Estado", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(table_Lista_Bancos);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton30.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton30.setText("Buscar");

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel56.setText("Nombre Banco:");

        txt_Nombre_Banco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Nombre_BancoActionPerformed(evt);
            }
        });

        btn_Cancelar_Banco.setText("Cancelar");
        btn_Cancelar_Banco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_BancoActionPerformed(evt);
            }
        });

        btn_Guardar_Banco.setText("Guardar");
        btn_Guardar_Banco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_BancoActionPerformed(evt);
            }
        });

        jLabel32.setText("Código Banco:");

        jLabel36.setText("Estado Banco:");

        combobox_Estado_Banco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btn_Actualizar_Banco.setText("Actualizar");
        btn_Actualizar_Banco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_BancoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(combobox_Estado_Banco, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_Codigo_Banco, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(txt_Nombre_Banco))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Cancelar_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Actualizar_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(txt_Nombre_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txt_Codigo_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Cancelar_Banco)
                            .addComponent(btn_Guardar_Banco)
                            .addComponent(btn_Actualizar_Banco)))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(combobox_Estado_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btn_Editar_Banco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Editar_Banco.setText("Editar");
        btn_Editar_Banco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_BancoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel29)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(245, 245, 245)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton30))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_Editar_Banco, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel30)
                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_Editar_Banco)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Bancos", jPanel9);

        jLabel35.setText("Categoría Ventas");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel38.setText("Categorías Ventas Registradas");

        btn_Editar_Categoria_Venta.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_Editar_Categoria_Venta.setText("Editar");
        btn_Editar_Categoria_Venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Editar_Categoria_VentaActionPerformed(evt);
            }
        });

        jButton39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton39.setText("Buscar");

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel58.setText("Categoría Estado Venta:");

        txt_Nombre_Categoria_Venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Nombre_Categoria_VentaActionPerformed(evt);
            }
        });

        btn_Cancelar_Categoria_Venta.setText("Cancelar");
        btn_Cancelar_Categoria_Venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_Categoria_VentaActionPerformed(evt);
            }
        });

        btn_Guardar_Categoria_Venta.setText("Guardar");
        btn_Guardar_Categoria_Venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_Categoria_VentaActionPerformed(evt);
            }
        });

        jLabel62.setText("Código Estado Venta:");

        jLabel63.setText("Estado de Venta:");

        combobox_Estado_Categoria_Venta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        btn_Actualizar_Categoría_Venta.setText("Actualizar");
        btn_Actualizar_Categoría_Venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Actualizar_Categoría_VentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(combobox_Estado_Categoria_Venta, 0, 125, Short.MAX_VALUE)
                    .addComponent(txt_Nombre_Categoria_Venta, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                    .addComponent(txt_Codigo_Categoria_Venta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Cancelar_Categoria_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Actualizar_Categoría_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar_Categoria_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(txt_Nombre_Categoria_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(txt_Codigo_Categoria_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Guardar_Categoria_Venta)
                            .addComponent(btn_Actualizar_Categoría_Venta)
                            .addComponent(btn_Cancelar_Categoria_Venta)))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel63)
                            .addComponent(combobox_Estado_Categoria_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Categoria_Venta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código Categoría Venta", "Categoría Venta", "Estado", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(table_Lista_Categoria_Venta);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton39))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btn_Editar_Categoria_Venta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel38)
                        .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btn_Editar_Categoria_Venta)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Catergoría Ventas", jPanel10);

        jLabel39.setText("Usuarios");

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        btn_Guardar_Usuario.setText("Guardar");
        btn_Guardar_Usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Guardar_UsuarioActionPerformed(evt);
            }
        });

        jLabel40.setText("Nombre Usuario:");

        jLabel42.setText("Ingrese Clave:");

        jLabel43.setText("Verefique Clave:");

        pass_Verificar_Clave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pass_Verificar_ClaveActionPerformed(evt);
            }
        });

        btn_Cancelar_Usuario.setText("Cancelar");
        btn_Cancelar_Usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Cancelar_UsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addGap(18, 18, 18)
                        .addComponent(pass_Verificar_Clave, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40)
                            .addComponent(jLabel42))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_Nombre_Usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(pass_Ingreso_Clave))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Cancelar_Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar_Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txt_Nombre_Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(pass_Ingreso_Clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Guardar_Usuario)
                            .addComponent(btn_Cancelar_Usuario)))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(pass_Verificar_Clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel41.setText("Usuarios Registrados");

        btn_Eliminar_Usuario.setText("Eliminar");
        btn_Eliminar_Usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Eliminar_UsuarioActionPerformed(evt);
            }
        });

        jButton40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton40.setText("Buscar");

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        table_Lista_Usuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre Usuario", "Acción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane8.setViewportView(table_Lista_Usuarios);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton40)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_Eliminar_Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton40)))
                .addGap(23, 23, 23)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Eliminar_Usuario)
                .addContainerGap())
        );

        tabbedPane_MAESTROS.addTab("Usuarios", jPanel11);

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 36)); // NOI18N
        jLabel2.setText("Dream Gift");

        MENU_VENTAS.setText("Ventas");
        MENU_VENTAS.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jMenuBar2.add(MENU_VENTAS);

        MENU_COMPRAS.setText("Compras");
        jMenuBar2.add(MENU_COMPRAS);

        MENU_INFORMES.setText("Informes");
        jMenuBar2.add(MENU_INFORMES);

        MENU_MAESTROS.setText("Maestros");
        MENU_MAESTROS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MENU_MAESTROSMouseClicked(evt);
            }
        });
        jMenuBar2.add(MENU_MAESTROS);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane_MAESTROS)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabbedPane_MAESTROS)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    
   /*
    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {                                             

      Connection con = null;

        try{

            con = getConection();
            ps = con.prepareStatement("UPDATE clientes SET rut=?, nombre=?, apellido=?, direccion=?, fono=?, fecha_nac=?, email=? WHERE rut=?");

            ps.setString(1, txtRut.getText());
            ps.setString(2, txtNombre.getText());
            ps.setString(3, txtApellido.getText());
            ps.setString(4, txtDireccion.getText());
            ps.setString(5, txtTelefono.getText());
            ps.setDate(6, new java.sql.Date(jdcFechaNac.getDate().getTime()));
            ps.setString(7, txtEmail.getText());

          

            ps.setString(8, txtRut.getText());

            int res = ps.executeUpdate();

            if(res > 0){
                JOptionPane.showMessageDialog(null, "Cliente Modificado");
                limpiarCajas();
                ActualizarAutomaticamente ();

    */ 

    
    
    private void Editar_Cliente(){
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE cliente SET CLI_ID_RUT_CLIENTE =?, CLI_NOMBRE =?,CLI_TELEFONO =?,CLI_CORREO =?,CLI_F_NACIMIENTO =?, CLI_COMUNA = ?,CLI_CELULAR =?,CLI_ESTADO =? WHERE  CLI_ID_RUT_CLIENTE=?");
            editar.setString(1, txt_Rut_Cliente.getText());
            editar.setString(2, txt_Nombre_Cliente.getText());
            editar.setString(3, txt_Telefono_Cliente.getText());
            editar.setString(4, txt_Email_Cliente.getText());    
            editar.setString(5, ((JTextField)date_F_Nacimiento_Cliente.getDateEditor().getUiComponent()).getText());                 
            editar.setString(6, (String) combobox_Comuna_Cliente.getSelectedItem());                
            editar.setString(7, txt_Celular_Cliente.getText());
            editar.setString(8, (String) combobox_Estado_Cliente.getSelectedItem());      
            editar.setString(9, txt_Rut_Cliente.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Rut_Cliente.setText("");
        txt_Nombre_Cliente.setText("");
        txt_Telefono_Cliente.setText("");
        txt_Email_Cliente.setText("");
        //date_F_Nacimiento_Cliente.setText("");
        txt_Celular_Cliente.setText("");
        Mostrar_Lista_Clientes();      
    } 
    private void Modificar_Cliente(){
        int i =0;
        while( i<tabla_clientes.getRowCount()){
            if(IsSelected(i, 8, table_Lista_Clientes)){
                txt_Rut_Cliente.setText((String) table_Lista_Clientes.getValueAt(i, 0));
                txt_Nombre_Cliente.setText((String) table_Lista_Clientes.getValueAt(i, 1));
                txt_Telefono_Cliente.setText((String) table_Lista_Clientes.getValueAt(i, 2));
                txt_Email_Cliente.setText((String) table_Lista_Clientes.getValueAt(i, 3));
                combobox_Comuna_Cliente.setSelectedItem(table_Lista_Clientes.getValueAt(i, 5)) ;
                txt_Celular_Cliente.setText((String) table_Lista_Clientes.getValueAt(i, 6));
                combobox_Estado_Cliente.setSelectedItem(table_Lista_Clientes.getValueAt(i, 7));
                }
            i++;
        }
    }
 
    private void Editar_Proveedor(){
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE proveedor SET PRO_ID_RUT_PROVEEDOR =?, PRO_NOMBRE =?,PRO_TELEFONO =?,PRO_CORREO =?,PRO_DIRECCION =?,PRO_ESTADO =? WHERE  PRO_ID_RUT_PROVEEDOR=?");
            editar.setString(1, txt_Rut_Proveedor.getText());
            editar.setString(2, txt_Nombre_Proveedor.getText());
            editar.setString(3, txt_Telefono_Proveedor.getText());
            editar.setString(4, txt_Email_Proveedor.getText());   
            editar.setString(5, txt_Direccion_Proveedor.getText()); 
            editar.setString(6, (String) combobox_Estado_Proveedor.getSelectedItem());                    
            editar.setString(7, txt_Rut_Proveedor.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Rut_Proveedor.setText("");
        txt_Nombre_Proveedor.setText("");
        txt_Telefono_Proveedor.setText("");
        txt_Email_Proveedor.setText("");
        txt_Direccion_Proveedor.setText("");
        Mostrar_Lista_Proveedores();      
    }
    private void Modificar_Proveedor(){
        int i =0;
        while( i<tabla_proveedores.getRowCount()){
            if(IsSelected(i, 6, table_Lista_Proveedores)){
                txt_Rut_Proveedor.setText((String) table_Lista_Proveedores.getValueAt(i, 0));
                txt_Nombre_Proveedor.setText((String) table_Lista_Proveedores.getValueAt(i, 1));
                txt_Telefono_Proveedor.setText((String) table_Lista_Proveedores.getValueAt(i, 2));
                txt_Email_Proveedor.setText((String) table_Lista_Proveedores.getValueAt(i, 3));
                txt_Direccion_Proveedor.setText((String) table_Lista_Proveedores.getValueAt(i, 4));
                combobox_Estado_Proveedor.setSelectedItem(table_Lista_Proveedores.getValueAt(i, 5));
            }          
            i++;
        }
    }
    
    private void Editar_Categoria_Articulo(){
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE categoria_articulo SET CAT_ID_CATEGORIA =?, CAT_NOMBRE =?,CAT_ESTADO =? WHERE  CAT_ID_CATEGORIA=?");
            editar.setString(1, txt_Codigo_Categoria_Articulo.getText());
            editar.setString(2, txt_Nombre_Categoria_Articulo.getText()); 
            editar.setString(3, (String) combobox_Estado_Categoria_Articulo.getSelectedItem());          
            editar.setString(4, txt_Codigo_Categoria_Articulo.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Categoria_Articulo.setText("");
        txt_Nombre_Categoria_Articulo.setText("");
        Mostrar_Lista_Categoria_Articulo(); 
    }
    private void Modificar_Categoria_Articulo(){
        int i =0;
        while( i<tabla_categoria_articulo.getRowCount()){
            if(IsSelected(i, 3, table_Lista_Categoria_Articulo)){
                txt_Codigo_Categoria_Articulo.setText((String) table_Lista_Categoria_Articulo.getValueAt(i, 0));
                txt_Nombre_Categoria_Articulo.setText((String) table_Lista_Categoria_Articulo.getValueAt(i, 1));
                combobox_Estado_Categoria_Articulo.setSelectedItem(table_Lista_Categoria_Articulo.getValueAt(i, 2));
            }                
            i++;
        }
    }
    
    private void Editar_Comuna(){
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE comunas SET COM_ID_COMUNA =?, COM_NOMBRE =?,COM_ESTADO =? WHERE  COM_ID_COMUNA=?");
            editar.setString(1, txt_Codigo_Comuna.getText());
            editar.setString(2, txt_Nombre_Comuna.getText()); 
            editar.setString(3, (String) combobox_Estado_Comuna.getSelectedItem());           
            editar.setString(4, txt_Codigo_Comuna.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Comuna.setText("");
        txt_Nombre_Comuna.setText("");
        Mostrar_Lista_Comunas(); 
    }
    private void Modificar_Comuna(){
        int i =0;
        while( i<tabla_comunas.getRowCount()){
            if(IsSelected(i, 3, table_Lista_Comunas)){
                txt_Codigo_Comuna.setText((String) table_Lista_Comunas.getValueAt(i, 0));
                txt_Nombre_Comuna.setText((String) table_Lista_Comunas.getValueAt(i, 1));
                combobox_Estado_Comuna.setSelectedItem(table_Lista_Comunas.getValueAt(i, 2));
            }                
            i++;
        }
    }
    
    private void Editar_Banco(){
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE bancos SET BAN_ID_BANCO =?, BAN_NOMBRE =?,BAN_ESTADO =? WHERE  BAN_ID_BANCO=?");
            editar.setString(1, txt_Codigo_Banco.getText());
            editar.setString(2, txt_Nombre_Banco.getText()); 
            editar.setString(3, (String) combobox_Estado_Banco.getSelectedItem());   
            editar.setString(4, txt_Codigo_Banco.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Banco.setText("");
        txt_Nombre_Banco.setText("");
        Mostrar_Lista_Bancos(); 
    }
    private void Modificar_Banco(){
        int i =0;
        while( i<tabla_bancos.getRowCount()){
            if(IsSelected(i, 3, table_Lista_Bancos)){
                txt_Codigo_Banco.setText((String) table_Lista_Bancos.getValueAt(i, 0));
                txt_Nombre_Banco.setText((String) table_Lista_Bancos.getValueAt(i, 1));
                combobox_Estado_Banco.setSelectedItem(table_Lista_Bancos.getValueAt(i, 2));
            }                
            i++;
        }
    }
    
    private void Editar_RRSS(){
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE RRSS SET RRS_ID_RRSS =?, RRS_NOMBRE =?,RRS_ESTADO =? WHERE  RRS_ID_RRSS=?");
            editar.setString(1, txt_Codigo_Red_Social.getText());
            editar.setString(2, txt_Nombre_Red_Social.getText()); 
            editar.setString(3, (String) combobox_Estado_Red_Social.getSelectedItem());   
            editar.setString(4, txt_Codigo_Red_Social.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Red_Social.setText("");
        txt_Nombre_Red_Social.setText("");
        Mostrar_Lista_RRSS(); 
    }
    private void Modificar_RRSS(){
        int i =0;
        while( i<tabla_redes_sociales.getRowCount()){
            if(IsSelected(i, 3, table_Lista_RRSS)){
                txt_Codigo_Red_Social.setText((String) table_Lista_RRSS.getValueAt(i, 0));
                txt_Nombre_Red_Social.setText((String) table_Lista_RRSS.getValueAt(i, 1));
                combobox_Estado_Red_Social.setSelectedItem(table_Lista_RRSS.getValueAt(i, 2));
            }                
            i++;
        }
    }
    
    private void Eliminar_Usuario(){
        int i =0;
        while( i<tabla_usuarios.getRowCount()){
            if(IsSelected(i, 1, table_Lista_Usuarios)){
                try {
                    PreparedStatement eliminar = conect.prepareStatement("DELETE FROM usuarios where USU_NOMBRE=?");
                    txt_Nombre_Usuario.setText((String) table_Lista_Usuarios.getValueAt(i, 0));
                    eliminar.setString(i, txt_Nombre_Usuario.getText());
                    eliminar.executeUpdate();                
                    JOptionPane.showMessageDialog(null,"Registro eliminado con exito");
                } 
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
                }
            }                
            i++;
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Nombre_Usuario.setText("");
        pass_Ingreso_Clave.setText("");
        pass_Verificar_Clave.setText("");
        Mostrar_Lista_Usuarios(); 
    }
    private void Modificar_Usuario(){
        int i =0;
        while( i<tabla_usuarios.getRowCount()){
            if(IsSelected(i, 1, table_Lista_Usuarios)){
                txt_Nombre_Usuario.setText((String) table_Lista_Usuarios.getValueAt(i, 0));
            }                
            i++;
        }
    }
    /*private void Editar_Usuario(){  
        if( pass_Ingreso_Clave.getText().contentEquals(pass_Verificar_Clave.getText())){
            try {
                PreparedStatement editar = conect.prepareStatement("UPDATE usuarios SET USU_NOMBRE =?,USU_CLAVE=? WHERE  USU_NOMBRE=?");
                editar.setString(1, txt_Nombre_Red_Social.getText()); 
                editar.setString(2, pass_Ingreso_Clave.getText());
                editar.setString(3, txt_Nombre_Usuario.getText());
                editar.executeUpdate();   
                JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
            } 
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
            }
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Red_Social.setText("");
        txt_Nombre_Red_Social.setText("");
        Mostrar_Lista_Usuarios();
    }*/
    
    private void Editar_Categoria_Venta(){
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE estados_venta SET EST_ID_EST_VEN =?, EST_VEN_NOMBRE =?,EST_VEN_ESTADO =? WHERE  EST_ID_EST_VEN=?");
            editar.setString(1, txt_Codigo_Categoria_Venta.getText());
            editar.setString(2, txt_Nombre_Categoria_Venta.getText()); 
            editar.setString(3, (String) combobox_Estado_Categoria_Venta.getSelectedItem());
            editar.setString(4, txt_Codigo_Categoria_Venta.getText());
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Categoria_Venta.setText("");
        txt_Nombre_Categoria_Venta.setText("");
        Mostrar_Lista_Categoria_Venta(); 
    }
    private void Modificar_Categoria_Venta(){
        int i =0;
        while( i<tabla_categoria_venta.getRowCount()){
            if(IsSelected(i, 3, table_Lista_Categoria_Venta)){
                txt_Codigo_Categoria_Venta.setText((String) table_Lista_Categoria_Venta.getValueAt(i, 0));
                txt_Nombre_Categoria_Venta.setText((String) table_Lista_Categoria_Venta.getValueAt(i, 1));
                combobox_Estado_Categoria_Venta.setSelectedItem(table_Lista_Categoria_Venta.getValueAt(i, 2));
            }                
            i++;
        }
    }
    
    private void Editar_Articulo(){
        
        String SQL = "SELECT CAT_NOMBRE FROM categoria_articulo";
        String SQL2 = "SELECT CAT_ID_CATEGORIA FROM categoria_articulo";
        String aux;
        String aux2;
        
        try {
            PreparedStatement editar = conect.prepareStatement("UPDATE articulo SET  ART_ID_ARTICULO=?,  ART_NOMBRE=?,  ART_DESCRPCION=?,  ART_FECHA_VENCIMIENTO=?,  ART_MARCA=?,  ART_ESTADO=?, CAT_ART_CAT_ID_CATEGORIA=?  WHERE  ART_ID_ARTICULO=?");
            editar.setString(1, txt_Codigo_Articulo.getText());
            editar.setString(2, txt_Nombre_Articulo.getText());
            editar.setString(3, txt_Descripcion_Articulo.getText());
            editar.setString(4, ((JTextField)date_Fecha_Vencimiento_Articulo.getDateEditor().getUiComponent()).getText());
            editar.setString(5, txt_Marca_Articulo.getText());
            editar.setString(6, (String) combobox_Estado_Articulo.getSelectedItem());
            
            PreparedStatement pst_nom = conect.prepareStatement(SQL);
            ResultSet rs_nom = pst_nom.executeQuery();
            
            PreparedStatement pst_id = conect.prepareStatement(SQL2);
            ResultSet rs_id = pst_id.executeQuery();
            
            while(rs_nom.next() && rs_id.next()){
                aux = (String)combobox_Categoria_Articulo_ART.getSelectedItem();
                if(rs_nom.getString("CAT_NOMBRE").contentEquals(aux)){
                    aux2 = rs_id.getString("CAT_ID_CATEGORIA");
                    editar.setString(7,aux2);
                }   
            }
            
            editar.setString(8, txt_Codigo_Articulo.getText());
            
            editar.executeUpdate();                
            JOptionPane.showMessageDialog(null,"Registro actualizado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se actualizó");
        }
        
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Articulo.setText("");
        txt_Nombre_Articulo.setText("");
        txt_Descripcion_Articulo.setText("");
        txt_Marca_Articulo.setText("");
        Mostrar_Lista_Articulo(); 
    }
    private void Modificar_Articulo(){
        int i =0;
        while( i<tabla_articulos.getRowCount()){
            if(IsSelected(i, 7, table_Lista_Articulos)){
                txt_Codigo_Articulo.setText((String) table_Lista_Articulos.getValueAt(i, 0));
                txt_Nombre_Articulo.setText((String) table_Lista_Articulos.getValueAt(i, 1));
                txt_Descripcion_Articulo.setText((String) table_Lista_Articulos.getValueAt(i, 2));
                combobox_Categoria_Articulo_ART.setSelectedItem(table_Lista_Articulos.getValueAt(i, 3)) ;
                txt_Marca_Articulo.setText((String) table_Lista_Articulos.getValueAt(i, 4));
                combobox_Estado_Articulo.setSelectedItem(table_Lista_Articulos.getValueAt(i, 6));
                }
            i++;
        }
    }

    
    
    
    
    
    
    
    
    
    
    private void MENU_MAESTROSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MENU_MAESTROSMouseClicked
    // Mostrar_Lista_Clientes();        
    }//GEN-LAST:event_MENU_MAESTROSMouseClicked

    private void btn_Eliminar_UsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Eliminar_UsuarioActionPerformed
        Eliminar_Usuario();
    }//GEN-LAST:event_btn_Eliminar_UsuarioActionPerformed

    private void btn_Cancelar_UsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_UsuarioActionPerformed

    }//GEN-LAST:event_btn_Cancelar_UsuarioActionPerformed

    private void pass_Verificar_ClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pass_Verificar_ClaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pass_Verificar_ClaveActionPerformed

    private void btn_Guardar_UsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_UsuarioActionPerformed
        String Ing_Clave = pass_Ingreso_Clave.getText();
        String Ver_Clave = pass_Verificar_Clave.getText();

        if( Ing_Clave.contentEquals(Ver_Clave)){
            try {

                PreparedStatement guardar = conect.prepareStatement("INSERT INTO USUARIOS(USU_NOMBRE,USU_CLAVE) VALUES (?,?)");
                guardar.setString(1, txt_Nombre_Usuario.getText());
                guardar.setString(2, pass_Ingreso_Clave.getText());
                guardar.executeUpdate();
                JOptionPane.showMessageDialog(null,"Registro guardado con exito");
            }

            catch (Exception e) {
                JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Contraseñas no coinciden");
        }

        Mostrar_Lista_Usuarios();

        String [] info = new String[1];
        info[0]= txt_Nombre_Usuario.getText();
        table_Lista_Usuarios.setModel(tabla_usuarios);

        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Nombre_Usuario.setText("");
        pass_Ingreso_Clave.setText("");
        pass_Verificar_Clave.setText("");
    }//GEN-LAST:event_btn_Guardar_UsuarioActionPerformed

    private void btn_Actualizar_Categoría_VentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_Categoría_VentaActionPerformed
        Editar_Categoria_Venta();
        bloquear_Actualizar_Categoria_Venta();
    }//GEN-LAST:event_btn_Actualizar_Categoría_VentaActionPerformed

    private void btn_Guardar_Categoria_VentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_Categoria_VentaActionPerformed
        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO estados_venta(EST_ID_EST_VEN,EST_VEN_NOMBRE,EST_VEN_ESTADO) VALUES (?,?,?)");
            guardar.setString(1, txt_Codigo_Categoria_Venta.getText());
            guardar.setString(2, txt_Nombre_Categoria_Venta.getText());
            guardar.setString(3, (String) combobox_Estado_Categoria_Venta.getSelectedItem());
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }
        Mostrar_Lista_Categoria_Venta();

        String [] info = new String[3];
        info[0]= txt_Codigo_Categoria_Venta.getText();
        info[1]= txt_Nombre_Categoria_Venta.getText();
        info[2]= (String) combobox_Estado_Categoria_Venta.getSelectedItem();
        table_Lista_Categoria_Venta.setModel(tabla_categoria_venta);

        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Categoria_Venta.setText("");
        txt_Nombre_Categoria_Venta.setText("");
    }//GEN-LAST:event_btn_Guardar_Categoria_VentaActionPerformed

    private void btn_Cancelar_Categoria_VentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_Categoria_VentaActionPerformed
        txt_Codigo_Categoria_Venta.setText("");
        txt_Nombre_Categoria_Venta.setText("");
    }//GEN-LAST:event_btn_Cancelar_Categoria_VentaActionPerformed

    private void txt_Nombre_Categoria_VentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Nombre_Categoria_VentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Nombre_Categoria_VentaActionPerformed

    private void btn_Editar_Categoria_VentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_Categoria_VentaActionPerformed
        Modificar_Categoria_Venta();
        Desbloquear_Actualizar_Categoria_Venta();
        //bloquear_Guardar_Categoria_Venta();
    }//GEN-LAST:event_btn_Editar_Categoria_VentaActionPerformed

    private void btn_Editar_BancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_BancoActionPerformed
        Modificar_Banco();
        Desbloquear_Actualizar_Banco();
        //bloquear_Guardar_Banco();
    }//GEN-LAST:event_btn_Editar_BancoActionPerformed

    private void btn_Actualizar_BancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_BancoActionPerformed
        Editar_Banco();
        bloquear_Actualizar_Banco();
    }//GEN-LAST:event_btn_Actualizar_BancoActionPerformed

    private void btn_Guardar_BancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_BancoActionPerformed
        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO bancos(BAN_ID_BANCO,BAN_NOMBRE,BAN_ESTADO) VALUES (?,?,?)");
            guardar.setString(1, txt_Codigo_Banco.getText());
            guardar.setString(2, txt_Nombre_Banco.getText());
            guardar.setString(3, (String) combobox_Estado_Banco.getSelectedItem());
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }

        Mostrar_Lista_Bancos();

        String [] info = new String[3];
        info[0]= txt_Codigo_Banco.getText();
        info[1]= txt_Nombre_Banco.getText();
        info[2]= (String) combobox_Estado_Banco.getSelectedItem();
        table_Lista_Bancos.setModel(tabla_bancos);

        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Banco.setText("");
        txt_Nombre_Banco.setText("");
    }//GEN-LAST:event_btn_Guardar_BancoActionPerformed

    private void btn_Cancelar_BancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_BancoActionPerformed
        // TODO add your handling code here:
        txt_Codigo_Banco.setText("");
        txt_Nombre_Banco.setText("");
    }//GEN-LAST:event_btn_Cancelar_BancoActionPerformed

    private void txt_Nombre_BancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Nombre_BancoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Nombre_BancoActionPerformed

    private void btn_Actualizar_ComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_ComunaActionPerformed
        Editar_Comuna();
        bloquear_Actualizar_Comuna();
    }//GEN-LAST:event_btn_Actualizar_ComunaActionPerformed

    private void combobox_Estado_ComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_Estado_ComunaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combobox_Estado_ComunaActionPerformed

             /*    */
    private void btn_Guardar_ComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_ComunaActionPerformed
        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO comunas(COM_ID_COMUNA,COM_NOMBRE,COM_ESTADO) VALUES (?,?,?)");
            guardar.setString(1, txt_Codigo_Comuna.getText());
            guardar.setString(2, txt_Nombre_Comuna.getText());
            guardar.setString(3, (String) combobox_Estado_Comuna.getSelectedItem());
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }
        Mostrar_Lista_Comunas();
        String [] info = new String[3];
        info[0]= txt_Codigo_Comuna.getText();
        info[1]= txt_Nombre_Comuna.getText();
        info[2]= (String) combobox_Estado_Comuna.getSelectedItem();
        table_Lista_Comunas.setModel(tabla_comunas);
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Comuna.setText("");
        txt_Nombre_Comuna.setText("");

    }//GEN-LAST:event_btn_Guardar_ComunaActionPerformed

    private void btn_Cancelar_ComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_ComunaActionPerformed
        // TODO add your handling code here:
        txt_Nombre_Comuna.setText("");
        txt_Codigo_Comuna.setText("");

    }//GEN-LAST:event_btn_Cancelar_ComunaActionPerformed

    private void txt_Nombre_ComunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Nombre_ComunaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Nombre_ComunaActionPerformed

    /*public boolean Editar_Comuna(String Nombre_Comuna){
        
    }*/
    private void btn_Editar_ComunasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_ComunasActionPerformed
        Modificar_Comuna();
        Desbloquear_Actualizar_Comuna();
        //bloquear_Guardar_Comuna();
    }//GEN-LAST:event_btn_Editar_ComunasActionPerformed

    private void btn_Editar_Categoria_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_Categoria_ArticuloActionPerformed
        Modificar_Categoria_Articulo();
        Desbloquear_Actualizar_Categoria_Articulo();
        //bloquear_Guardar_Categoria_Articulo();
    }//GEN-LAST:event_btn_Editar_Categoria_ArticuloActionPerformed

    private void btn_Actualiizar_Categoria_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualiizar_Categoria_ArticuloActionPerformed
        Editar_Categoria_Articulo();
        bloquear_Actualizar_Categoria_Articulo();
    }//GEN-LAST:event_btn_Actualiizar_Categoria_ArticuloActionPerformed

    private void btn_Guardar_Categoria_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_Categoria_ArticuloActionPerformed
        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO categoria_articulo(CAT_ID_CATEGORIA,CAT_NOMBRE,CAT_ESTADO) VALUES (?,?,?)");
            guardar.setString(1, txt_Codigo_Categoria_Articulo.getText());
            guardar.setString(2, txt_Nombre_Categoria_Articulo.getText());
            guardar.setString(3, (String) combobox_Estado_Categoria_Articulo.getSelectedItem());
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }
        Mostrar_Lista_Categoria_Articulo();
        String [] info = new String[3];
        info[0]= txt_Codigo_Categoria_Articulo.getText();
        info[1]= txt_Nombre_Categoria_Articulo.getText();
        info[2]= (String) combobox_Estado_Categoria_Articulo.getSelectedItem();
        table_Lista_Categoria_Articulo.setModel(tabla_categoria_articulo);

        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Codigo_Categoria_Articulo.setText("");
        txt_Nombre_Categoria_Articulo.setText("");
    }//GEN-LAST:event_btn_Guardar_Categoria_ArticuloActionPerformed

    private void btn_Cancelar_Categoria_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_Categoria_ArticuloActionPerformed
        // TODO add your handling code here:
        txt_Codigo_Categoria_Articulo.setText("");
        txt_Nombre_Categoria_Articulo.setText("");
    }//GEN-LAST:event_btn_Cancelar_Categoria_ArticuloActionPerformed

    private void txt_Nombre_Categoria_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Nombre_Categoria_ArticuloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Nombre_Categoria_ArticuloActionPerformed

    private void btn_Editar_RRSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_RRSSActionPerformed
        Modificar_RRSS();
        Desbloquear_Actualizar_RRSS();
        //bloquear_Guardar_RRSS();
    }//GEN-LAST:event_btn_Editar_RRSSActionPerformed

    private void btn_Actualizar_RRSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_RRSSActionPerformed
        Editar_RRSS();
        bloquear_Actualizar_RRSS();
    }//GEN-LAST:event_btn_Actualizar_RRSSActionPerformed

    private void btn_Cancelar_RRSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_RRSSActionPerformed
        // TODO add your handling code here:
        txt_Codigo_Red_Social.setText("");
        txt_Nombre_Red_Social.setText("");
    }//GEN-LAST:event_btn_Cancelar_RRSSActionPerformed

    private void txt_Nombre_Red_SocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Nombre_Red_SocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Nombre_Red_SocialActionPerformed

    private void btn_Editar_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_ProveedorActionPerformed
        Modificar_Proveedor();
        Desbloquear_Actualizar_Proveedor();
        //bloquear_Guardar_Proveedor();
    }//GEN-LAST:event_btn_Editar_ProveedorActionPerformed

    private void btn_Actualizar_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_ProveedorActionPerformed
        Editar_Proveedor();
        bloquear_Actualizar_Proveedor();
    }//GEN-LAST:event_btn_Actualizar_ProveedorActionPerformed

    private void txt_Email_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Email_ProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Email_ProveedorActionPerformed

    private void txt_Rut_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Rut_ProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Rut_ProveedorActionPerformed

    private void btn_Cancelar_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_ProveedorActionPerformed
        txt_Rut_Proveedor.setText("");
        txt_Nombre_Proveedor.setText("");
        txt_Telefono_Proveedor.setText("");
        txt_Email_Proveedor.setText("");
        txt_Direccion_Proveedor.setText("");
    }//GEN-LAST:event_btn_Cancelar_ProveedorActionPerformed

    private void btn_Guardar_ProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_ProveedorActionPerformed
        // TODO add your handling code here:

        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO proveedor(PRO_ID_RUT_PROVEEDOR,PRO_NOMBRE,PRO_TELEFONO,PRO_CORREO,PRO_DIRECCION,PRO_ESTADO) VALUES (?,?,?,?,?,?)");
            guardar.setString(1, txt_Rut_Proveedor.getText());
            guardar.setString(2, txt_Nombre_Proveedor.getText());
            guardar.setString(3, txt_Telefono_Proveedor.getText());
            guardar.setString(4, txt_Email_Proveedor.getText());
            guardar.setString(5, txt_Direccion_Proveedor.getText());
            guardar.setString(6, (String) combobox_Estado_Proveedor.getSelectedItem());
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }

        Mostrar_Lista_Proveedores();
        /*
        String [] info = new String[6];
        info[0]= txt_Rut_Proveedor.getText();
        info[1]= txt_Nombre_Proveedor.getText();
        info[2]= txt_Telefono_Proveedor.getText();
        info[3]= txt_Direccion_Proveedor.getText();
        info[4]= txt_Email_Proveedor.getText();
        info[5]= (String) combobox_Estado_Proveedor.getSelectedItem();
        table_Lista_Proveedores.setModel(tabla_proveedores);
        */
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Rut_Proveedor.setText("");
        txt_Nombre_Proveedor.setText("");
        txt_Telefono_Proveedor.setText("");
        txt_Email_Proveedor.setText("");
        txt_Direccion_Proveedor.setText("");
        }

        {
    }//GEN-LAST:event_btn_Guardar_ProveedorActionPerformed

    private void btn_Editar_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Editar_ClienteActionPerformed
        Modificar_Cliente();
        Desbloquear_Actualizar_Cliente();
        //bloquear_Guardar_Cliente();
    }//GEN-LAST:event_btn_Editar_ClienteActionPerformed

 /*   */
    private void btn_Actualizar_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_ClienteActionPerformed
        // TODO add your handling code here:
        Editar_Cliente();
        bloquear_Actualizar_Cliente();
    }//GEN-LAST:event_btn_Actualizar_ClienteActionPerformed

    private void txt_Celular_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Celular_ClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Celular_ClienteActionPerformed

    private void txt_Rut_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Rut_ClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Rut_ClienteActionPerformed

    private void txt_Telefono_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Telefono_ClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Telefono_ClienteActionPerformed

    private void btn_Cancelar_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_ClienteActionPerformed
        // TODO add your handling code here:
        txt_Nombre_Cliente.setText("");
        txt_Celular_Cliente.setText("");
        txt_Telefono_Cliente.setText("");
        txt_Email_Cliente.setText("");
        txt_Rut_Cliente.setText("");
        //txtfecha_Nacimiento_Cliente.setText("");

    }//GEN-LAST:event_btn_Cancelar_ClienteActionPerformed

    private void btn_Guardar_ClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_ClienteActionPerformed

        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO cliente(CLI_ID_RUT_CLIENTE,CLI_NOMBRE,CLI_TELEFONO,CLI_CORREO,CLI_F_NACIMIENTO,CLI_COMUNA,CLI_CELULAR,CLI_ESTADO) VALUES (?,?,?,?,?,?,?,?)");
            guardar.setString(1, txt_Rut_Cliente.getText());
            guardar.setString(2, txt_Nombre_Cliente.getText());
            guardar.setString(3, txt_Telefono_Cliente.getText());
            guardar.setString(4, txt_Email_Cliente.getText());
            guardar.setString(5, ((JTextField)date_F_Nacimiento_Cliente.getDateEditor().getUiComponent()).getText());
            guardar.setString(6, (String) combobox_Comuna_Cliente.getSelectedItem());
            guardar.setString(7, txt_Celular_Cliente.getText());
            guardar.setString(8, (String) combobox_Estado_Cliente.getSelectedItem());
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }

        Mostrar_Lista_Clientes();
        /*
        String [] info = new String[8];
        info[0]= txt_Rut_Cliente.getText();
        info[1]= txt_Nombre_Cliente.getText();
        info[2]= txt_Telefono_Cliente.getText();
        info[3]= txt_Email_Cliente.getText();
        info[4]= ((JTextField)date_F_Nacimiento_Cliente.getDateEditor().getUiComponent()).getText();
        info[5]= (String) combobox_Comuna_Cliente.getSelectedItem();
        info[6]= txt_Celular_Cliente.getText();
        info[7]= (String) combobox_Estado_Cliente.getSelectedItem();
        //tabla_clientes.addRow(info);
        table_Lista_Clientes.setModel(tabla_clientes);
        */
        //PARA LIMPIAR LOS CAMPOS DESPUES DE GUARDAR
        txt_Rut_Cliente.setText("");
        txt_Nombre_Cliente.setText("");
        txt_Telefono_Cliente.setText("");
        txt_Email_Cliente.setText("");
        //date_F_Nacimiento_Cliente.setText("");
        txt_Celular_Cliente.setText("");
    }//GEN-LAST:event_btn_Guardar_ClienteActionPerformed

    private void tabbedPane_MAESTROSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPane_MAESTROSMouseClicked
        Mostrar_Lista_Clientes();
        Mostrar_Lista_Proveedores();
        Mostrar_Lista_RRSS();
        Mostrar_Lista_Categoria_Articulo();
        Mostrar_Lista_Comunas();
        Mostrar_Lista_Bancos();
        Mostrar_Lista_Categoria_Venta();
        Mostrar_Lista_Usuarios();
        Mostrar_Lista_Articulo();
        Mostrar_Lista_Pack();
        
        
        Agregar_Articulos_Lista_pack();
        Consultar_Categoria_Articulo();
        //Consultar_Proveedor_Articulo();
    
    }//GEN-LAST:event_tabbedPane_MAESTROSMouseClicked

    private void btn_Guardar_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Guardar_ArticuloActionPerformed
        
        String SQL = "SELECT CAT_NOMBRE FROM categoria_articulo";
        String SQL2 = "SELECT CAT_ID_CATEGORIA FROM categoria_articulo";
        String aux;
        String aux2;
        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO articulo (ART_ID_ARTICULO,ART_NOMBRE,ART_DESCRPCION,ART_FECHA_VENCIMIENTO,ART_MARCA,ART_ESTADO,CAT_ART_CAT_ID_CATEGORIA) VALUES (?,?,?,?,?,?,?)");
            guardar.setString(1, txt_Codigo_Articulo.getText());
            guardar.setString(2, txt_Nombre_Articulo.getText());
            guardar.setString(3, txt_Descripcion_Articulo.getText());
            guardar.setString(4, ((JTextField)date_Fecha_Vencimiento_Articulo.getDateEditor().getUiComponent()).getText());
            guardar.setString(5, txt_Marca_Articulo.getText());
            guardar.setString(6, (String) combobox_Estado_Articulo.getSelectedItem());
            
            PreparedStatement pst_nom = conect.prepareStatement(SQL);
            ResultSet rs_nom = pst_nom.executeQuery();
            
            PreparedStatement pst_id = conect.prepareStatement(SQL2);
            ResultSet rs_id = pst_id.executeQuery();
            
            while(rs_nom.next() && rs_id.next()){
                aux = (String)combobox_Categoria_Articulo_ART.getSelectedItem();
                if(rs_nom.getString("CAT_NOMBRE").contentEquals(aux)){
                    aux2 = rs_id.getString("CAT_ID_CATEGORIA");
                    guardar.setString(7,aux2);
                }   
            }
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }
        
        Mostrar_Lista_Articulo();
        
        txt_Codigo_Articulo.setText("");
        txt_Nombre_Articulo.setText("");
        txt_Descripcion_Articulo.setText("");
        txt_Marca_Articulo.setText("");
    }//GEN-LAST:event_btn_Guardar_ArticuloActionPerformed

    private void btn_Cancelar_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_ArticuloActionPerformed
        txt_Codigo_Articulo.setText("");
        txt_Nombre_Articulo.setText("");
        txt_Descripcion_Articulo.setText("");
        txt_Marca_Articulo.setText("");
    }//GEN-LAST:event_btn_Cancelar_ArticuloActionPerformed

    private void btn_Actualizar_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_ArticuloActionPerformed
        Editar_Articulo();
        bloquear_Actualizar_Articulo();
    }//GEN-LAST:event_btn_Actualizar_ArticuloActionPerformed

    private void btn_editar_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_ArticuloActionPerformed
        Modificar_Articulo();
        Desbloquear_Actualizar_Articulo();
    }//GEN-LAST:event_btn_editar_ArticuloActionPerformed

    private void btn_Crear_PackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Crear_PackActionPerformed
        
        try {
            PreparedStatement guardar = conect.prepareStatement("INSERT INTO pack(PCK_ID_PACK, PCK_NOMBRE, PCK_COSTO, PCK_STOCK, PCK_ESTADO) VALUES (?,?,?,?,?)");
            guardar.setString(1, txt_Codigo_Pack.getText());
            guardar.setString(2, txt_Nombre_Pack.getText());
            guardar.setString(3, txt_Costo_Pack.getText());
            guardar.setString(4, txt_Stock_Pack.getText());
            guardar.setString(5, (String) combobox_Estado_Pack.getSelectedItem());
            guardar.executeUpdate();
            JOptionPane.showMessageDialog(null,"Registro guardado con exito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e + "Registro no se guardó");
        }

        Mostrar_Lista_Pack();
    }//GEN-LAST:event_btn_Crear_PackActionPerformed

    private void btn_Actualizar_PackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Actualizar_PackActionPerformed
       
    }//GEN-LAST:event_btn_Actualizar_PackActionPerformed

    private void btn_Cancelar_PackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Cancelar_PackActionPerformed
        txt_Codigo_Pack.setText("");
        txt_Nombre_Pack.setText("");
        txt_Costo_Pack.setText("");
        txt_Stock_Pack.setText("");
    }//GEN-LAST:event_btn_Cancelar_PackActionPerformed

    private void btn_Agregar_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Agregar_ArticuloActionPerformed
  
    }//GEN-LAST:event_btn_Agregar_ArticuloActionPerformed

    private void btn_Quitar_ArticuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Quitar_ArticuloActionPerformed
        
    }//GEN-LAST:event_btn_Quitar_ArticuloActionPerformed
    
    public void Desactivar () {
        // TODO add your handling code here:
        
    }/*    */
    public static void main(String args[]) {
        
        //DBConeccion db = new DBConeccion();
        //DBConeccion.getConnection()
       
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Maestros().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Label_Clientes;
    private javax.swing.JLabel Label_Lista_Clientes;
    private javax.swing.JLabel Label_Lista_Clientes1;
    private javax.swing.JMenu MENU_COMPRAS;
    private javax.swing.JMenu MENU_INFORMES;
    private javax.swing.JMenu MENU_MAESTROS;
    private javax.swing.JMenu MENU_VENTAS;
    private javax.swing.JPanel Panel_Clientes;
    private javax.swing.JButton btn_Actualiizar_Categoria_Articulo;
    private javax.swing.JButton btn_Actualizar_Articulo;
    private javax.swing.JButton btn_Actualizar_Banco;
    private javax.swing.JButton btn_Actualizar_Categoría_Venta;
    private javax.swing.JButton btn_Actualizar_Cliente;
    private javax.swing.JButton btn_Actualizar_Comuna;
    private javax.swing.JButton btn_Actualizar_Pack;
    private javax.swing.JButton btn_Actualizar_Proveedor;
    private javax.swing.JButton btn_Actualizar_RRSS;
    private javax.swing.JButton btn_Agregar_Articulo;
    private javax.swing.JButton btn_Buscar_Lista_Cliente;
    private javax.swing.JButton btn_Cancelar_Articulo;
    private javax.swing.JButton btn_Cancelar_Banco;
    private javax.swing.JButton btn_Cancelar_Categoria_Articulo;
    private javax.swing.JButton btn_Cancelar_Categoria_Venta;
    private javax.swing.JButton btn_Cancelar_Cliente;
    private javax.swing.JButton btn_Cancelar_Comuna;
    private javax.swing.JButton btn_Cancelar_Pack;
    private javax.swing.JButton btn_Cancelar_Proveedor;
    private javax.swing.JButton btn_Cancelar_RRSS;
    private javax.swing.JButton btn_Cancelar_Usuario;
    private javax.swing.JButton btn_Crear_Pack;
    private javax.swing.JButton btn_Editar_Banco;
    private javax.swing.JButton btn_Editar_Categoria_Articulo;
    private javax.swing.JButton btn_Editar_Categoria_Venta;
    private javax.swing.JButton btn_Editar_Cliente;
    private javax.swing.JButton btn_Editar_Comunas;
    private javax.swing.JButton btn_Editar_Pack;
    private javax.swing.JButton btn_Editar_Proveedor;
    private javax.swing.JButton btn_Editar_RRSS;
    private javax.swing.JButton btn_Eliminar_Usuario;
    private javax.swing.JButton btn_Guardar_Articulo;
    private javax.swing.JButton btn_Guardar_Banco;
    private javax.swing.JButton btn_Guardar_Categoria_Articulo;
    private javax.swing.JButton btn_Guardar_Categoria_Venta;
    private javax.swing.JButton btn_Guardar_Cliente;
    private javax.swing.JButton btn_Guardar_Comuna;
    private javax.swing.JButton btn_Guardar_Proveedor;
    private javax.swing.JButton btn_Guardar_RRSS;
    private javax.swing.JButton btn_Guardar_Usuario;
    private javax.swing.JButton btn_Quitar_Articulo;
    private javax.swing.JButton btn_Venta_Cliente;
    private javax.swing.JButton btn_editar_Articulo;
    private javax.swing.JComboBox<String> combobox_Categoria_Articulo_ART;
    private javax.swing.JComboBox<String> combobox_Comuna_Cliente;
    private javax.swing.JComboBox<String> combobox_Estado_Articulo;
    private javax.swing.JComboBox<String> combobox_Estado_Banco;
    private javax.swing.JComboBox<String> combobox_Estado_Categoria_Articulo;
    private javax.swing.JComboBox<String> combobox_Estado_Categoria_Venta;
    private javax.swing.JComboBox<String> combobox_Estado_Cliente;
    private javax.swing.JComboBox<String> combobox_Estado_Comuna;
    private javax.swing.JComboBox<String> combobox_Estado_Pack;
    private javax.swing.JComboBox<String> combobox_Estado_Proveedor;
    private javax.swing.JComboBox<String> combobox_Estado_Red_Social;
    private com.toedter.calendar.JDateChooser date_F_Nacimiento_Cliente;
    private com.toedter.calendar.JDateChooser date_Fecha_Vencimiento_Articulo;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList2;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuBar jMenuBar4;
    private javax.swing.JMenuBar jMenuBar5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JList<String> list_Articulos_Pack;
    private javax.swing.JPasswordField pass_Ingreso_Clave;
    private javax.swing.JPasswordField pass_Verificar_Clave;
    private javax.swing.JSpinner spinner_Pack;
    private javax.swing.JTabbedPane tabbedPane_MAESTROS;
    private javax.swing.JTable table_Lista_Articulos;
    private javax.swing.JTable table_Lista_Bancos;
    private javax.swing.JTable table_Lista_Categoria_Articulo;
    private javax.swing.JTable table_Lista_Categoria_Venta;
    private javax.swing.JTable table_Lista_Clientes;
    private javax.swing.JTable table_Lista_Comunas;
    private javax.swing.JTable table_Lista_Pack;
    private javax.swing.JTable table_Lista_Proveedores;
    private javax.swing.JTable table_Lista_RRSS;
    private javax.swing.JTable table_Lista_Usuarios;
    private javax.swing.JTextField txt_Buscar_Lista_Cliente;
    private javax.swing.JTextField txt_Celular_Cliente;
    private javax.swing.JTextField txt_Codigo_Articulo;
    private javax.swing.JTextField txt_Codigo_Banco;
    private javax.swing.JTextField txt_Codigo_Categoria_Articulo;
    private javax.swing.JTextField txt_Codigo_Categoria_Venta;
    private javax.swing.JTextField txt_Codigo_Comuna;
    private javax.swing.JTextField txt_Codigo_Pack;
    private javax.swing.JTextField txt_Codigo_Red_Social;
    private javax.swing.JTextField txt_Costo_Pack;
    private javax.swing.JTextField txt_Descripcion_Articulo;
    private javax.swing.JTextField txt_Direccion_Proveedor;
    private javax.swing.JTextField txt_Email_Cliente;
    private javax.swing.JTextField txt_Email_Proveedor;
    private javax.swing.JTextField txt_Marca_Articulo;
    private javax.swing.JTextField txt_Nombre_Articulo;
    private javax.swing.JTextField txt_Nombre_Banco;
    private javax.swing.JTextField txt_Nombre_Categoria_Articulo;
    private javax.swing.JTextField txt_Nombre_Categoria_Venta;
    private javax.swing.JTextField txt_Nombre_Cliente;
    private javax.swing.JTextField txt_Nombre_Comuna;
    private javax.swing.JTextField txt_Nombre_Pack;
    private javax.swing.JTextField txt_Nombre_Proveedor;
    private javax.swing.JTextField txt_Nombre_Red_Social;
    private javax.swing.JTextField txt_Nombre_Usuario;
    private javax.swing.JTextField txt_Rut_Cliente;
    private javax.swing.JTextField txt_Rut_Proveedor;
    private javax.swing.JTextField txt_Stock_Pack;
    private javax.swing.JTextField txt_Telefono_Cliente;
    private javax.swing.JTextField txt_Telefono_Proveedor;
    private javax.swing.JLabel txtlabel;
    // End of variables declaration//GEN-END:variables
}

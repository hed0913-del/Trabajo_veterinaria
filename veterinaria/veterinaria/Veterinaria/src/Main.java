import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            try (Connection ignored = DB.connect()) {
                new MainWindow().setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                    "No se pudo conectar con MySQL.\n\n1) Iniciá MySQL en XAMPP.\n2) Importá sql/veterinaria_db.sql en phpMyAdmin.\n3) Revisá src/DB.java si tu usuario o contraseña son distintos.\n\nDetalle: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

class MainWindow extends JFrame {
    private final JTabbedPane tabs = new JTabbedPane();
    private final LinkedHashMap<String, String> tables = new LinkedHashMap<>();
    MainWindow() {
        super("VetU — Gestión Veterinaria");
        tables.put("Dueños", "duenos");
        tables.put("Mascotas", "mascotas");
        tables.put("Veterinarios", "veterinarios");
        tables.put("Citas", "citas");
        tables.put("Consultas", "consultas");
        tables.put("Vacunas", "vacunas");
        tables.put("Vacunación", "mascota_vacunas");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                int r = JOptionPane.showConfirmDialog(MainWindow.this, "¿Querés cerrar el sistema?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) { dispose(); System.exit(0); }
            }
        });
        setSize(1120, 700); setMinimumSize(new Dimension(850, 550)); setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout(12,12)); root.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("VetU  |  Sistema de gestión veterinaria"); title.setFont(title.getFont().deriveFont(Font.BOLD, 23f));
        JLabel sub = new JLabel("Dueños · mascotas · profesionales · citas · consultas · vacunas"); sub.setForeground(new Color(90,100,110));
        JPanel heading = new JPanel(); heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS)); heading.add(title); heading.add(Box.createVerticalStrut(5)); heading.add(sub);
        JButton refresh = new JButton("Actualizar todas las tablas"); refresh.addActionListener(e -> refreshTabs());
        header.add(heading, BorderLayout.CENTER); header.add(refresh, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);
        for (Map.Entry<String,String> entry : tables.entrySet()) tabs.addTab(entry.getKey(), new TablePanel(entry.getValue()));
        root.add(tabs, BorderLayout.CENTER);
        JLabel footer = new JLabel("Para salir, usá la X de la ventana. Los cambios se guardan en MySQL."); footer.setBorder(BorderFactory.createEmptyBorder(4,2,0,2));
        root.add(footer, BorderLayout.SOUTH); setContentPane(root);
    }
    private void refreshTabs() { for (int i=0;i<tabs.getTabCount();i++) ((TablePanel)tabs.getComponentAt(i)).load(); }
}

class TablePanel extends JPanel {
    private final String table;
    private final JTable grid = new JTable();
    private final DefaultTableModel model = new DefaultTableModel();
    private java.util.List<Col> columns = new ArrayList<>();
    private java.util.List<Object> rowIds = new ArrayList<>();
    private String idColumn;
    TablePanel(String table) {
        super(new BorderLayout(8,8)); this.table = table;
        grid.setModel(model); grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); grid.setFillsViewportHeight(true);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("＋ Nuevo"); JButton edit = new JButton("✎ Modificar"); JButton delete = new JButton("Eliminar"); JButton reload = new JButton("↻ Recargar");
        add.addActionListener(e -> createRow()); edit.addActionListener(e -> editRow()); delete.addActionListener(e -> deleteRow()); reload.addActionListener(e -> load());
        actions.add(add); actions.add(edit); actions.add(delete); actions.add(reload);
        add(actions, BorderLayout.NORTH); add(new JScrollPane(grid), BorderLayout.CENTER);
        load();
    }
    void load() {
        try (Connection c=DB.connect(); Statement st=c.createStatement(); ResultSet rs=st.executeQuery("SELECT * FROM `"+table+"`")) {
            ResultSetMetaData md=rs.getMetaData(); int n=md.getColumnCount(); columns.clear(); idColumn=null;
            model.setRowCount(0); model.setColumnCount(0); rowIds.clear();
            for(int i=1;i<=n;i++) { Col col=new Col(md.getColumnName(i),md.getColumnType(i),md.isAutoIncrement(i)); columns.add(col); model.addColumn(col.name); if(idColumn==null && (col.name.equalsIgnoreCase("id") || col.name.endsWith("_id") && col.name.startsWith(table.substring(0,Math.min(table.length(),3))))) idColumn=col.name; }
            if(idColumn==null && !columns.isEmpty()) idColumn=columns.get(0).name;
            while(rs.next()) { Object[] vals=new Object[n]; for(int i=1;i<=n;i++) vals[i-1]=rs.getObject(i); model.addRow(vals); rowIds.add(rs.getObject(idColumn)); }
            grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for(int i=0;i<grid.getColumnModel().getColumnCount();i++) grid.getColumnModel().getColumn(i).setPreferredWidth(150);
        } catch(SQLException ex) { error("No se pudo cargar la tabla",ex); }
    }
    private void createRow() {
        LinkedHashMap<String,Object> values=editDialog("Nuevo registro",null); if(values==null)return;
        StringJoiner names=new StringJoiner(","), marks=new StringJoiner(","); for(String name:values.keySet()){names.add("`"+name+"`");marks.add("?");}
        String sql="INSERT INTO `"+table+"` ("+names+") VALUES ("+marks+")";
        try(Connection c=DB.connect();PreparedStatement ps=c.prepareStatement(sql)){int i=1;for(Object v:values.values())ps.setObject(i++,v);ps.executeUpdate();load();JOptionPane.showMessageDialog(this,"Registro guardado correctamente.");}catch(Exception ex){error("No se pudo guardar. Revisá los datos y las relaciones.",ex);}
    }
    private void editRow() {
        int selected=grid.getSelectedRow(); if(selected<0){JOptionPane.showMessageDialog(this,"Seleccioná una fila primero.");return;}
        Object id=rowIds.get(grid.convertRowIndexToModel(selected)); LinkedHashMap<String,Object> existing=new LinkedHashMap<>();
        for(int i=0;i<columns.size();i++) existing.put(columns.get(i).name,model.getValueAt(grid.convertRowIndexToModel(selected),i));
        LinkedHashMap<String,Object> values=editDialog("Modificar registro",existing); if(values==null)return;
        StringJoiner sets=new StringJoiner(","); for(String name:values.keySet())sets.add("`"+name+"`=?");
        try(Connection c=DB.connect();PreparedStatement ps=c.prepareStatement("UPDATE `"+table+"` SET "+sets+" WHERE `"+idColumn+"`=?")){int i=1;for(Object v:values.values())ps.setObject(i++,v);ps.setObject(i,id);ps.executeUpdate();load();JOptionPane.showMessageDialog(this,"Registro actualizado.");}catch(Exception ex){error("No se pudo modificar el registro.",ex);}
    }
    private void deleteRow() {
        int selected=grid.getSelectedRow();if(selected<0){JOptionPane.showMessageDialog(this,"Seleccioná una fila primero.");return;}
        Object id=rowIds.get(grid.convertRowIndexToModel(selected));int answer=JOptionPane.showConfirmDialog(this,"¿Eliminar el registro seleccionado? Esta acción no se puede deshacer.","Confirmar eliminación",JOptionPane.YES_NO_OPTION);
        if(answer!=JOptionPane.YES_OPTION)return;
        try(Connection c=DB.connect();PreparedStatement ps=c.prepareStatement("DELETE FROM `"+table+"` WHERE `"+idColumn+"`=?")){ps.setObject(1,id);ps.executeUpdate();load();JOptionPane.showMessageDialog(this,"Registro eliminado.");}catch(Exception ex){error("No se pudo eliminar. Puede tener registros relacionados (por ejemplo, citas o mascotas).",ex);}
    }
    private LinkedHashMap<String,Object> editDialog(String title,Map<String,Object> existing) {
        JPanel form=new JPanel(new GridLayout(0,2,8,8)); LinkedHashMap<String,JTextField> inputs=new LinkedHashMap<>();
        for(Col col:columns){if(col.auto || col.name.equalsIgnoreCase("id"))continue; JLabel label=new JLabel(col.name.replace('_',' ')); JTextField field=new JTextField(existing!=null && existing.get(col.name)!=null?existing.get(col.name).toString():"");
            if(col.name.equalsIgnoreCase("activo")) field.setToolTipText("Usá 1 para sí y 0 para no");
            if(col.name.endsWith("_id")) field.setToolTipText("Ingresá el ID existente de la tabla relacionada");
            form.add(label);form.add(field);inputs.put(col.name,field);
        }
        JScrollPane scroll=new JScrollPane(form);scroll.setPreferredSize(new Dimension(520,Math.min(470,Math.max(180,inputs.size()*38))));
        int result=JOptionPane.showConfirmDialog(this,scroll,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);if(result!=JOptionPane.OK_OPTION)return null;
        LinkedHashMap<String,Object> values=new LinkedHashMap<>();
        try {for(Col col:columns){JTextField f=inputs.get(col.name);if(f==null)continue;String raw=f.getText().trim();Object value;
                if(raw.isEmpty()) value=null;
                else if(col.type==Types.INTEGER || col.type==Types.SMALLINT || col.type==Types.TINYINT) value=Integer.valueOf(raw);
                else if(col.type==Types.BIGINT) value=Long.valueOf(raw);
                else if(col.type==Types.DECIMAL || col.type==Types.NUMERIC) value=new java.math.BigDecimal(raw.replace(',','.'));
                else if(col.type==Types.DATE) value=java.sql.Date.valueOf(raw);
                else if(col.type==Types.TIME) value=Time.valueOf(raw);
                else if(col.type==Types.TIMESTAMP) value=Timestamp.valueOf(raw.replace('T',' '));
                else value=raw;
                values.put(col.name,value);
            }}catch(Exception ex){JOptionPane.showMessageDialog(this,"Hay un valor con formato incorrecto. Para fechas usá AAAA-MM-DD; para números, solo números.","Revisá los datos",JOptionPane.WARNING_MESSAGE);return null;}
        return values;
    }
    private void error(String message,Exception ex){JOptionPane.showMessageDialog(this,message+"\n\nDetalle: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    private static class Col {String name;int type;boolean auto;Col(String n,int t,boolean a){name=n;type=t;auto=a;}}
}

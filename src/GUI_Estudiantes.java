import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GUI_Estudiantes extends JFrame{
    private JPanel panel;
    private JTextField ingresarNombre;
    private JTextField ingresarEdad;
    private JTextField ingresarApellido;
    private JTextField ingresarCarrera;
    private JButton ingresarButton;
    private JButton consultarButton;
    private JList lista;
    private JLabel nombreText;
    private JLabel apellidoText;
    private JLabel edadText;
    private JLabel carreraText;
    private JLabel formularioText;

    Connection conexion;
    PreparedStatement ps;
    DefaultListModel mod = new DefaultListModel<>();
    Statement st;
    ResultSet rs;

    public GUI_Estudiantes() {
        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    insertar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static void main(String[] args) {
        GUI_Estudiantes frame = new GUI_Estudiantes();
        frame.setContentPane(new GUI_Estudiantes().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setSize(500,300);
    }

    public void conectarBDD(){
        try{
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/estudiantesbdd", "root",""); //ingresar password
            System.out.println("Conexión con la base de datos establecida.");
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void listar() throws SQLException {
        conectarBDD();
        lista.setModel(mod);
        st = conexion.createStatement();
        rs = st.executeQuery("select * from estudiante");
        mod.removeAllElements();
        while(rs.next()){
            mod.addElement(rs.getInt(1) +" "+ rs.getString(2) +" "+ rs.getString(3) + " "+ rs.getInt(4) +" " +rs.getString(5));
        }
    }

    public void insertar() throws SQLException {
        conectarBDD();
        ps = conexion.prepareStatement("INSERT INTO estudiante (nombre, apellido, edad, carrera) VALUES(?,?,?,?)");
        ps.setString(1, ingresarNombre.getText());
        ps.setString(2, ingresarApellido.getText());
        ps.setInt(3, Integer.parseInt(ingresarEdad.getText()));
        ps.setString(4, ingresarCarrera.getText());
        if (ps.executeUpdate() > 0){
            lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("La inserción fue exitosa");
            ingresarNombre.setText("");
            ingresarApellido.setText("");
            ingresarEdad.setText("");
            ingresarCarrera.setText("");
        }
    }

}

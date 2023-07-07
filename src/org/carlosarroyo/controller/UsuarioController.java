
package org.carlosarroyo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosarroyo.bean.Usuario;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.system.Principal;


public class UsuarioController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO ,ELIMINAR ,EDITAR ,ACTUALIZAR ,CANCELAR ,NINGUNO, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
  

    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtNombreApellido;
    @FXML private TextField txtUsuario;
    @FXML private TextField  txtPassword;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
     
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEliminar.setDisable(true);
    }
    
    
    
    
       public void nuevo(){
       switch (tipoDeOperacion){
            case NINGUNO:
               activarControles();
               limpiarControles();
               btnNuevo.setText("Guardar");
               btnEliminar.setText("Cancelar");
               
              imgNuevo.setImage(new Image("/org/carlosarroyo/image/Guardar.png"));
              imgEliminar.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
               tipoDeOperacion = operaciones.GUARDAR;
               break;
               
               case GUARDAR:
               guardar();
               dasactivarControles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Cancelar");
               
              imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
               tipoDeOperacion = operaciones.NINGUNO;

               break;
               
        }
    }
   
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtNombreApellido.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtPassword.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Dato Ingresado Exitosamente");
            ventanaLogin();
    }catch(Exception e){
        e.printStackTrace();
    }
    }
    
    public void eliminar (){
        switch(tipoDeOperacion){
            case GUARDAR:
               dasactivarControles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Cancelar");
               
              imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
               tipoDeOperacion = operaciones.NINGUNO;
               
              ventanaLogin();
              
        }
    }
    
    
    
    public void dasactivarControles(){

    txtNombreUsuario.setEditable(false);
    txtNombreApellido.setEditable(false);
     txtUsuario.setEditable(false);
     txtPassword.setEditable(false);
     btnEliminar.setDisable(true);
    }
    
      public void activarControles(){

    txtNombreUsuario.setEditable(true);
    txtNombreApellido.setEditable(true);
     txtUsuario.setEditable(true);
     txtPassword.setEditable(true);
     btnEliminar.setDisable(false);
    }

     public void limpiarControles(){

    txtNombreUsuario.clear();
    txtNombreApellido.clear();
     txtUsuario.clear();
     txtPassword.clear();
     
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}



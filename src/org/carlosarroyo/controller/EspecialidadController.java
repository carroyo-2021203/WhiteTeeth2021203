package org.carlosarroyo.controller;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosarroyo.bean.Especialidad;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.system.Principal;


public class EspecialidadController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, ELIMINAR,ACTUALIZAR ,CANCELAR,NINGUNO,GUARDAR};
    private operaciones tipoOperaciones = operaciones.NINGUNO;
    private ObservableList<Especialidad>ListaEspecialiadad;
    
    
    @FXML private TextField txtCodigoEspecialidad;
    @FXML private TextField txtDiescripccion;
    @FXML private TableView tblEspecialidad;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cargarDatos();
        
    }
    
        public void cargarDatos(){
            tblEspecialidad.setItems(getEspecialidad());
            colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad,Integer>("codigoEspecialidad"));
            colDescripcion.setCellValueFactory(new PropertyValueFactory<Especialidad,String>("descripcion"));
            
            
        }
    
    
        public ObservableList<Especialidad>getEspecialidad(){
        ArrayList<Especialidad> lista = new ArrayList <Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad (resultado.getInt("codigoEspecialidad"),
                                         resultado.getString("descripcion")

                                              
                        
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return ListaEspecialiadad = FXCollections.observableArrayList(lista);
    }
                public void seleccionarElemento(){
            
            try{
                txtCodigoEspecialidad.setText(String.valueOf(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
                txtDiescripccion.setText(String.valueOf(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getDescripcion()));
            }catch(NullPointerException e) {
	        JOptionPane.showMessageDialog(null,"No Existe ningún dato en esa casilla");
		}
	}
        
           public void nuevo(){
        switch (tipoOperaciones){
            case NINGUNO:
               activarControles();
               limpiarControles();
            btnNuevo.setText("Guardar");
               btnEliminar.setText("Cancelar");
               btnEditar.setDisable(true);
               btnReporte.setDisable(true);
               
              imgNuevo.setImage(new Image("/org/carlosarroyo/image/Guardar.png"));
                 imgEliminar.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
               tipoOperaciones = operaciones.GUARDAR;
               break;
               
               case GUARDAR:
               guardar();
               desactivarContoles();
               limpiarControles();
                  btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
                 btnReporte.setDisable(false);
               
                        imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/EliminarPaciente.png"));
               tipoOperaciones = operaciones.NINGUNO;
               cargarDatos();
         break;
               
        }
    }
        
   
           
           
           public void eliminar(){

        switch(tipoOperaciones){
            case GUARDAR:
               desactivarContoles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               
               imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/EliminarPaciente.png"));
               tipoOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblEspecialidad.getSelectionModel().getSelectedItem() !=null){
                int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar este Elemento ?", "Eliminar una Especialidad",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane. YES_OPTION){
                try{
                PreparedStatement procedimiento=Conexion.getInstance ().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                procedimiento.setInt (1, ((Especialidad) tblEspecialidad.getSelectionModel ().getSelectedItem()).getCodigoEspecialidad());
                procedimiento.execute ();
                ListaEspecialiadad.remove (tblEspecialidad.getSelectionModel ().getSelectedIndex());
                limpiarControles ();
                }catch (Exception e){
                e.printStackTrace ();

                    
                }
       }else {
                    limpiarControles();
                }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe selecionar un elemento");

                }
   }
   }
              
    
           
              public void editar (){
                 switch (tipoOperaciones){
                case NINGUNO:
            if (tblEspecialidad.getSelectionModel ().getSelectedItem () != null){
                btnEditar.setText("Actualizar");
                btnReporte.setText ("Cancelar");
                btnNuevo.setDisable (true);
                btnEliminar.setDisable (true);
                imgEditar.setImage(new Image("/org/carlosarroyo/image/Actualizar.png"));
                imgReporte.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
                activarControles ();
                txtCodigoEspecialidad.setEditable (false);
                tipoOperaciones=operaciones.ACTUALIZAR;
               
        }else{
                 JOptionPane.showMessageDialog (null, "Debe selecionar un elemento. ");
                }
            break;
             case ACTUALIZAR:
                 actualizar();
                             btnEditar.setText ("Editar");
                             btnReporte.setText ("Roporte");
                             btnNuevo.setDisable (false);
                            btnEliminar.setDisable (false);
                        imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
                           imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
         desactivarContoles ();
         limpiarControles ();
         cargarDatos ();
        tipoOperaciones = operaciones.NINGUNO;    
          break;
   
   
    }
    }
              
                 public void reporte (){
             switch (tipoOperaciones){
      case ACTUALIZAR:
         desactivarContoles ();
         limpiarControles ();
          btnEditar.setText ("Editar");
         btnReporte.setText ("Roporte");
         btnNuevo.setDisable (false);
          btnEliminar.setDisable (false);
          imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
          imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
          tblEspecialidad.getSelectionModel().clearSelection();
          tipoOperaciones = operaciones.NINGUNO;


          break;
}
}
    
    
      public void desactivarContoles(){
        txtCodigoEspecialidad.setEditable(false);
        txtDiescripccion.setEditable(false);
        
    }
    
    public void activarControles(){
 
        txtDiescripccion.setEditable(true);
    }
    
    public void limpiarControles(){
        
        txtCodigoEspecialidad.clear();
        txtDiescripccion.clear();
        tblEspecialidad.getSelectionModel().clearSelection();
   }
    
    
    public void guardar(){
        Especialidad registro = new Especialidad();
        registro.setDescripcion(txtDiescripccion.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");

            procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                ListaEspecialiadad.add(registro);

    }catch(Exception e){
    e.printStackTrace();
}
    }
    
    
    
    
    public void actualizar(){
        
        try{
     PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEspecialidad(?,?)}");
        Especialidad registro = (Especialidad) tblEspecialidad.getSelectionModel ().getSelectedItem();
        registro.setDescripcion(txtDiescripccion.getText());
        procedimiento.setInt(1,registro.getCodigoEspecialidad());
        procedimiento.setString(2, registro.getDescripcion());
          procedimiento.execute();
        
        }catch(Exception e){
    e.printStackTrace();
}
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
}

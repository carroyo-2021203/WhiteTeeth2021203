package org.carlosarroyo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosarroyo.bean.Doctor;
import org.carlosarroyo.bean.Especialidad;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.report.GenerarReporte;
import org.carlosarroyo.system.Principal;


public class DoctorController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO ,ELIMINAR ,EDITAR ,ACTUALIZAR ,CANCELAR ,NINGUNO, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Doctor>listaDoctor;
    private ObservableList<Especialidad> listaEspecialidad;
    
    
    
    
    @FXML private TextField txtNumeroColegiado;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private TableView tblDocotor;
    @FXML private TableColumn colNumero;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodigoEspecialidad;
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
    cmbCodigoEspecialidad.setItems(getEspecialidad());
    cmbCodigoEspecialidad.setDisable(true);
    }
    
    public void cargarDatos(){
        
        tblDocotor.setItems(getDoctor());
        colNumero.setCellValueFactory(new PropertyValueFactory<Doctor,Integer>("numeroColegiado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Doctor,String>("nombreDoctor"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Doctor,String>("apellidosDoctor"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Doctor,String>("telefonoContacto"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Doctor,Integer>("codigoEspecialidad"));
       
        
    }
    
    public ObservableList<Doctor>getDoctor(){
        ArrayList<Doctor>lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor (resultado.getInt("numeroColegiado"),
                                            resultado.getString("nombreDoctor"),
                                            resultado.getString("apellidosDoctor"),
                                            resultado.getString("telefonoContacto"),
                                            resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
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
        
        return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
    
   
   
   
   
    public void seleccionarDatos(){
        try{
            txtNumeroColegiado.setText(String.valueOf(((Doctor)tblDocotor.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            txtNombre.setText(String.valueOf(((Doctor)tblDocotor.getSelectionModel().getSelectedItem()).getNombreDoctor()));
            txtApellido.setText(String.valueOf(((Doctor)tblDocotor.getSelectionModel().getSelectedItem()).getApellidosDoctor()));
            txtTelefono.setText(String.valueOf(((Doctor)tblDocotor.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
            cmbCodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((Doctor)tblDocotor.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
            
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(null,"No Existe ningún dato en esa casilla");
        }
    }
    
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEspecialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado= new Especialidad(registro.getInt("codigoEspecialidad"),
                                            registro.getString("descripcion")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    
    
    

       public void nuevo(){
       switch (tipoDeOperacion){
            case NINGUNO:
               activarControles();
                limpiarControles();
               btnNuevo.setText("Guardar");
               btnEliminar.setText("Cancelar");
               btnEditar.setDisable(true);
               btnReporte.setDisable(true);
               
               imgNuevo.setImage(new Image("/org/carlosarroyo/image/Guardar.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
               tipoDeOperacion = operaciones.GUARDAR;
               break;
               
               case GUARDAR:
               guardar();
               desactivarControles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               
               imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/EliminarPaciente.png"));
               tipoDeOperacion = operaciones.NINGUNO;
               cargarDatos();
               break;
               
        }
       
    }
       
       public void guardar(){
           Doctor registro = new Doctor();
           registro.setNumeroColegiado(Integer.parseInt(txtNumeroColegiado.getText()));
           registro.setNombreDoctor(txtNombre.getText());
           registro.setApellidosDoctor(txtApellido.getText());
           registro.setTelefonoContacto(txtTelefono.getText());
           registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
       
       try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDoctor(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombreDoctor());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoContacto());
            procedimiento.setInt(5, registro.getCodigoEspecialidad());
            procedimiento.execute();
            listaDoctor.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
       }
                  public void eliminar(){

        switch(tipoDeOperacion){
            case GUARDAR:
               desactivarControles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               
               imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/EliminarPaciente.png"));
               tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblDocotor.getSelectionModel().getSelectedItem() !=null){
                int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar este Elemento ?", "Eliminar un Doctor",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane. YES_OPTION){
                try{
                PreparedStatement procedimiento=Conexion.getInstance ().getConexion().prepareCall("{call sp_EliminarDoctor(?)}");
                procedimiento.setInt (1, ((Doctor) tblDocotor.getSelectionModel ().getSelectedItem()).getNumeroColegiado());
                procedimiento.execute ();
                listaDoctor.remove (tblDocotor.getSelectionModel ().getSelectedIndex());
                limpiarControles ();
                }catch (Exception e){
                e.printStackTrace ();

                    
                }
       }else{
                    limpiarControles();
                }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe selecionar un elemento");

                }
   }
   }
            public void editar (){
             switch (tipoDeOperacion){
                 case NINGUNO:
                     if (tblDocotor.getSelectionModel ().getSelectedItem () != null){
                         btnEditar.setText("Actualizar");
                         btnReporte.setText ("Cancelar");
                         btnNuevo.setDisable (true);
                         btnEliminar.setDisable (true);
                         imgEditar.setImage(new Image("/org/carlosarroyo/image/Actualizar.png"));
                         imgReporte.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
                         activarControles ();
                         txtNumeroColegiado.setEditable(false);
                         cmbCodigoEspecialidad.setDisable(true);
                         tipoDeOperacion=operaciones.ACTUALIZAR;

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
                  desactivarControles ();
                  limpiarControles ();
                  cargarDatos ();
                 tipoDeOperacion = operaciones.NINGUNO;    
                   break;


             }
             }
            
               public void actualizar(){
          try {
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDoctor(?,?,?,?)}");
         Doctor registro = (Doctor)tblDocotor.getSelectionModel().getSelectedItem();
           registro.setNumeroColegiado(Integer.parseInt(txtNumeroColegiado.getText()));
           registro.setNombreDoctor(txtNombre.getText());
           registro.setApellidosDoctor(txtApellido.getText());
           registro.setTelefonoContacto(txtTelefono.getText());

            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombreDoctor());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoContacto());

            procedimiento.execute();
            listaDoctor.add(registro);
             procedimiento.execute();
            }catch(Exception e){
             e.printStackTrace();
                  }
               }
               
               
               
               
              public void reporte (){
            switch (tipoDeOperacion){
              case NINGUNO:
           imprimirReporte();
           break;
               case ACTUALIZAR:
                  desactivarControles ();
                  limpiarControles ();
                   btnEditar.setText ("Editar");
                  btnReporte.setText ("Roporte");
                  btnNuevo.setDisable (false);
                   btnEliminar.setDisable (false);
                    imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
                    imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
                    tblDocotor.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;

         break;

  }
  
  }
               public void imprimirReporte(){
                                     
                   Map parametros = new HashMap();
                   if( tblDocotor.getSelectionModel().getSelectedItem() !=null){
                       
                   parametros.put("Doctor", ((Doctor) tblDocotor.getSelectionModel ().getSelectedItem()).getNumeroColegiado());
                   GenerarReporte.mostrarReporte("RecetaMedica.jasper", "Receta", parametros);
                   
                    limpiarControles();
               }else{

    
     parametros.put("numeroColegiado", null);
     GenerarReporte.mostrarReporte("ReportesDoctores.jasper", "Reporte de Doctores", parametros);
       
       
}

               }
   
        public void desactivarControles(){
        txtNumeroColegiado.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoEspecialidad.setDisable(true);
        
    }
    
    public void activarControles(){
        txtNumeroColegiado.setEditable(true);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodigoEspecialidad.setDisable(false);
    }
    public void limpiarControles(){
        txtNumeroColegiado.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        tblDocotor.getSelectionModel().clearSelection();
        cmbCodigoEspecialidad.setValue(null);
       
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

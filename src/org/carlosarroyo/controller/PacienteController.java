
package org.carlosarroyo.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.carlosarroyo.bean.Paciente;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.report.GenerarReporte;

import org.carlosarroyo.system.Principal;


public class PacienteController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO ,ELIMINAR ,EDITAR ,ACTUALIZAR ,CANCELAR ,NINGUNO, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente>listaPaciente;
    private DatePicker fnacimiento;
    private DatePicker fPV;
    
    
    
    @FXML private TextField txtCodigoPaciente;
    @FXML private TextField txtSexo;
    @FXML private TextField txtNombresPaciente;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDireccionPaciente;
    @FXML private TextField txtTelefonoPersona;
    @FXML private GridPane grpFecha;
    @FXML private TableView tblPaciente;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colSexo;  
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colFechadeNacimiento;
    @FXML private TableColumn colPrimeraVisita;
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
           fnacimiento = new DatePicker(Locale.ENGLISH);
           fnacimiento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
           fnacimiento.getCalendarView().todayButtonTextProperty().set("Today");
           fnacimiento.getCalendarView().setShowWeeks(false);
           grpFecha.add(fnacimiento,3,4);
           fnacimiento.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");
           fPV = new DatePicker(Locale.ENGLISH);
           fPV.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
           fPV.getCalendarView().todayButtonTextProperty().set("Today");
           fPV.getCalendarView().setShowWeeks(false);
           grpFecha.add(fPV,4,7);
           fPV.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");
     }
    
    public void cargarDatos(){
        tblPaciente.setItems(getPaciente());
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Paciente,Integer>("codigoPaciente"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Paciente,String>("nombrePaciente"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente,String>("apellidosPaciente"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente,String>("sexo"));     
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente,String>("direccionPaciente"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<Paciente,String>("telefonoPersonal"));
        colFechadeNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente,String>("fechadeNacimiento"));
        colPrimeraVisita.setCellValueFactory(new PropertyValueFactory<Paciente,String>("fechaPrimeraVisita"));
    }
    
    public void seleccionarElemento(){
       
    
                try {
          txtCodigoPaciente.setText(String.valueOf(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
          txtSexo.setText(String.valueOf(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getSexo()));
          txtNombresPaciente.setText(String.valueOf(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getNombrePaciente()));
          txtTelefonoPersona.setText(String.valueOf(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getTelefonoPersonal()));
          txtApellido.setText(String.valueOf(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getApellidosPaciente()));
          txtDireccionPaciente.setText(String.valueOf(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getDireccionPaciente()));
          fnacimiento.selectedDateProperty().set(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getFechadeNacimiento());
          fPV.selectedDateProperty().set(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getFechaPrimeraVisita());
		}
		catch(NullPointerException e) {
	        JOptionPane.showMessageDialog(null,"No Existe ningún dato en esa casilla");
		}
	}
    
    
    public ObservableList<Paciente>getPaciente(){
        ArrayList<Paciente> lista = new ArrayList <Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                                         resultado.getString("nombrePaciente"),
                                         resultado.getString("apellidosPaciente"),
                                         resultado.getString("sexo"),
                                         resultado.getDate("fechadeNacimiento"),
                                         resultado.getString("direccionPaciente"),
                                         resultado.getString("telefonoPersonal"),
                                         resultado.getDate("fechaPrimeraVisita")
                                              
                        
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPaciente = FXCollections.observableArrayList(lista);
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
               dasactivarControles();
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
   
   public void eliminar(){
       switch(tipoDeOperacion){
            case GUARDAR:
               dasactivarControles();
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
                if(tblPaciente.getSelectionModel().getSelectedItem() !=null){
                int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Un Paciente",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane. YES_OPTION){
                try{
                PreparedStatement procedimiento=Conexion.getInstance ().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                procedimiento.setInt (1, ((Paciente) tblPaciente.getSelectionModel ().getSelectedItem()).getCodigoPaciente ());
                procedimiento.execute ();
                listaPaciente.remove (tblPaciente.getSelectionModel ().getSelectedIndex());
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
            if (tblPaciente.getSelectionModel ().getSelectedItem () != null){
                btnEditar.setText("Actualizar");
                btnReporte.setText ("Cancelar");
                btnNuevo.setDisable (true);
                btnEliminar.setDisable (true);
                imgEditar.setImage(new Image("/org/carlosarroyo/image/Actualizar.png"));
                imgReporte.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
                activarControles ();
                txtCodigoPaciente.setEditable (false);
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
         dasactivarControles ();
         limpiarControles ();
         cargarDatos ();
        tipoDeOperacion = operaciones.NINGUNO;    
          break;
   
   
    }
    }
   
   public void actualizar(){
           try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarPaciente(?,?,?,?,?,?,?,?)}");
     Paciente registro = (Paciente) tblPaciente.getSelectionModel ().getSelectedItem();

     registro.setNombrePaciente(txtNombresPaciente.getText());
     registro.setApellidosPaciente(txtApellido.getText());
     registro.setSexo(txtSexo.getText());
     registro.setFechadeNacimiento(fnacimiento.getSelectedDate());
     registro.setDireccionPaciente(txtDireccionPaciente.getText());
     registro.setTelefonoPersonal(txtTelefonoPersona.getText());
     registro.setFechaPrimeraVisita(fPV.getSelectedDate());
     procedimiento.setInt(1, registro.getCodigoPaciente());
        procedimiento.setString(2, registro.getNombrePaciente()); 
        procedimiento.setString(3, registro.getApellidosPaciente()); 
        procedimiento.setString(4, registro.getSexo()); 
        procedimiento.setDate(5, new java.sql.Date(registro.getFechadeNacimiento().getTime())); 
        procedimiento.setString(6, registro.getDireccionPaciente());   
        procedimiento.setString(7, registro.getTelefonoPersonal());    
        procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime())); 
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
         dasactivarControles ();
         limpiarControles ();
          btnEditar.setText ("Editar");
         btnReporte.setText ("Roporte");
         btnNuevo.setDisable (false);
          btnEliminar.setDisable (false);
          imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
          imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
          tblPaciente.getSelectionModel().clearSelection();
          tipoDeOperacion = operaciones.NINGUNO;


          break;
          
}
}
   
   
   public void imprimirReporte(){
    Map parametros = new HashMap();
    
     parametros.put("codigoPaciente", null);
     GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
       
       
}
   
  
    
 public void guardar(){
     Paciente registro = new Paciente();
     registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
     registro.setNombrePaciente(txtNombresPaciente.getText());
     registro.setApellidosPaciente(txtApellido.getText());
     registro.setSexo(txtSexo.getText());
     registro.setFechadeNacimiento(fnacimiento.getSelectedDate());
     registro.setDireccionPaciente(txtDireccionPaciente.getText());
     registro.setTelefonoPersonal(txtTelefonoPersona.getText());
     registro.setFechaPrimeraVisita(fPV.getSelectedDate());
     
 
    try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPaciente(?,?,?,?,?,?,?,?)}");
procedimiento.setInt(1, registro.getCodigoPaciente());
procedimiento.setString(2, registro.getNombrePaciente()); 
procedimiento.setString(3, registro.getApellidosPaciente()); 
procedimiento.setString(4, registro.getSexo()); 
procedimiento.setDate(5, new java.sql.Date(registro.getFechadeNacimiento().getTime())); 
procedimiento.setString(6, registro.getDireccionPaciente());   
procedimiento.setString(7, registro.getTelefonoPersonal());    
procedimiento.setDate(8, new java.sql.Date(registro.getFechaPrimeraVisita().getTime())); 
procedimiento.execute();
listaPaciente.add(registro);

    }catch(Exception e){
    e.printStackTrace();
}
    
    
 }  
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void dasactivarControles(){
    txtCodigoPaciente.setEditable(false);
    txtSexo.setEditable(false);
    txtNombresPaciente.setEditable(false);
    txtApellido.setEditable(false);
    txtDireccionPaciente.setEditable(false);
    txtTelefonoPersona.setEditable(false);

    
    }

     public void activarControles(){
        txtCodigoPaciente.setEditable(true);
        txtCodigoPaciente.setEditable(true);
        txtSexo.setEditable(true);
        txtNombresPaciente.setEditable(true);
        txtApellido.setEditable(true);

        txtDireccionPaciente.setEditable(true);
        txtTelefonoPersona.setEditable(true);

    }
    
    public void limpiarControles(){
        txtCodigoPaciente.clear();
        txtSexo.clear();
        txtNombresPaciente.clear();
        txtApellido.clear();
        txtDireccionPaciente.clear();
        txtTelefonoPersona.clear();
        tblPaciente.getSelectionModel().clearSelection();
        
           fnacimiento = new DatePicker(Locale.ENGLISH);
           fnacimiento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
           fnacimiento.getCalendarView().todayButtonTextProperty().set("Today");
           fnacimiento.getCalendarView().setShowWeeks(false);
           grpFecha.add(fnacimiento,3,4);
           fnacimiento.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");
           fPV = new DatePicker(Locale.ENGLISH);
           fPV.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
           fPV.getCalendarView().todayButtonTextProperty().set("Today");
           fPV.getCalendarView().setShowWeeks(false);
           grpFecha.add(fPV,4,7);
           fPV.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");

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

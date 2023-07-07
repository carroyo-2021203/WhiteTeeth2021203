package org.carlosarroyo.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.carlosarroyo.bean.Doctor;
import org.carlosarroyo.bean.Receta;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.system.Principal;


public class RecetasController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO,GUARDAR,ACTUALIZAR,ELIMINAR,EDITAR,CANCELAR,NUEVO};
    private operaciones tipoOperaciones = operaciones.NINGUNO;
    private ObservableList<Receta>listaReceta;
    private DatePicker fecha;
    private ObservableList<Doctor>listaDoctor;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private ComboBox cmbDoctor;
    @FXML private GridPane grpRecetas;
    @FXML private TableView tblReceta;
    @FXML private TableColumn colCodigoReceta;  
    @FXML private TableColumn colFechaReceta;  
    @FXML private TableColumn colNumeroColegiado;  
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
        fecha = new DatePicker (Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpRecetas.add(fecha, 3, 1);
        fecha.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");
        cmbDoctor.setItems(getDoctor());
        cmbDoctor.setDisable(true);
    }
    
    
    public void cargarDatos(){
        tblReceta.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta,Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta,String>("fechaReceta"));
       colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta,Integer>("numeroColegiado"));
        
    }
    


    
    public ObservableList <Receta>getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Receta (resultado.getInt("codigoReceta"),
                                      resultado.getDate("fechaReceta"),
                                      resultado.getInt("numeroColegiado") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableArrayList(lista);
                
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
        
    public void seleccionarDatos(){
        try{
            
            txtCodigoReceta.setText(String.valueOf(((Receta)tblReceta.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fecha.selectedDateProperty().set(((Receta)tblReceta.getSelectionModel().getSelectedItem()).getFechaReceta());
            cmbDoctor.getSelectionModel().select(buscarDoctor(((Receta)tblReceta.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            
            
        }catch(NullPointerException e) {
	        JOptionPane.showMessageDialog(null,"No Existe ningún dato en esa casilla");
		}
    }
    
        
    public Doctor buscarDoctor(int NumeroColegiado){
        Doctor resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, NumeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                
                               resultado= new Doctor(registro.getInt("numeroColegiado"),
                                            registro.getString("nombreDoctor"),
                                             registro.getString("apellidosDoctor"),
                                                registro.getString("telefonoContacto"),
                                            registro.getInt("codigoEspecialidad"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
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
               desactivarControles();
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
               desactivarControles();
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
                if(tblReceta.getSelectionModel().getSelectedItem() !=null){
                int respuesta = JOptionPane.showConfirmDialog(null, "Quieres Eliminar este Elemento ?", "Eliminar una Receta",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane. YES_OPTION){
                try{
                PreparedStatement procedimiento=Conexion.getInstance ().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                procedimiento.setInt (1, ((Receta) tblReceta.getSelectionModel ().getSelectedItem()).getCodigoReceta());
                procedimiento.execute ();
                listaReceta.remove (tblReceta.getSelectionModel ().getSelectedIndex());
                limpiarControles ();
                }catch (Exception e){
                e.printStackTrace ();

                    
                }
       }else{
                    limpiarControles();
                }
                }else{
                    JOptionPane.showMessageDialog(null,"No ha seleccionado un elemento");

                }
   }
   }
                   public void editar (){
             switch (tipoOperaciones){
                 case NINGUNO:
                     if (tblReceta.getSelectionModel ().getSelectedItem () != null){
                         btnEditar.setText("Actualizar");
                         btnReporte.setText ("Cancelar");
                         btnNuevo.setDisable (true);
                         btnEliminar.setDisable (true);
                         imgEditar.setImage(new Image("/org/carlosarroyo/image/Actualizar.png"));
                         imgReporte.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
                         activarControles ();
                         txtCodigoReceta.setEditable(false);
                         cmbDoctor.setDisable(true);
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
                  desactivarControles ();
                  limpiarControles ();
                  cargarDatos ();
                 tipoOperaciones = operaciones.NINGUNO;    
                   break;


             }
             }
           
                   
                   public void reporte (){
            switch (tipoOperaciones){
               case ACTUALIZAR:
                  desactivarControles ();
                  limpiarControles ();
                   btnEditar.setText ("Editar");
                  btnReporte.setText ("Roporte");
                  btnNuevo.setDisable (false);
                   btnEliminar.setDisable (false);
                    imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
                    imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
                    tblReceta.getSelectionModel().clearSelection();
                    tipoOperaciones = operaciones.NINGUNO;

         break;

  }
  
  }
    
              public void guardar(){
           Receta registro = new Receta();
  
           registro.setFechaReceta(fecha.getSelectedDate());
           registro.setNumeroColegiado(((Doctor)cmbDoctor.getSelectionModel().getSelectedItem()).getNumeroColegiado());
       
       try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarReceta(?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime())); 
            procedimiento.setInt(2, registro.getNumeroColegiado());
            listaReceta.add(registro);
            procedimiento.execute();
           
       }catch(Exception e){
           e.printStackTrace();
       }
       }
              
       public void actualizar(){
           try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarReceta(?,?,?)}");
               Receta registro = (Receta)tblReceta.getSelectionModel().getSelectedItem();
               
                registro.setFechaReceta(fecha.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime())); 
            procedimiento.setInt(3, registro.getNumeroColegiado());       
              
               listaReceta.add(registro);
               procedimiento.execute();
            
           }catch(Exception e){
               e.printStackTrace();
           }
           
       }
    
    public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
        fecha.setDisable(true);
        cmbDoctor.setDisable(true);
       
    }
    
     public void activarControles(){
       
       
        fecha.setDisable(false);
        cmbDoctor.setDisable(false);
    }
     
 public void limpiarControles(){
     
   
        txtCodigoReceta.clear();
        cmbDoctor.setValue(null);
        fecha = new DatePicker (Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpRecetas.add(fecha, 3, 1);
        fecha.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");
        cmbDoctor.setValue(null);
        tblReceta.getSelectionModel().clearSelection();
      

            
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

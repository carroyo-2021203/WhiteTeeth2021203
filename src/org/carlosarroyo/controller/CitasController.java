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
import org.carlosarroyo.bean.Cita;
import org.carlosarroyo.bean.Doctor;
import org.carlosarroyo.bean.Paciente;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.system.Principal;

public class CitasController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones { ACTUALIZAR,ELIMINAR ,EDITAR ,NUEVO ,CANCELAR ,NINGUNO, GUARDAR};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Cita>listaCita;
    private ObservableList<Doctor>listaDoctor;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fechaCita;

    
    
    
    
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtHora;
    @FXML private TextField txtDes;
    @FXML private GridPane grpCita;
    @FXML private ComboBox cmbPaciente;
    @FXML private ComboBox cmbDoctor;
    @FXML private TableView tblCita;
    @FXML private TableColumn colCita;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colPaciente;
    @FXML private TableColumn colDoctor;
    @FXML private TableColumn colDesrip;
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
    fechaCita = new DatePicker(Locale.ENGLISH);
    fechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    fechaCita.getCalendarView().todayButtonTextProperty().set("Today");
    fechaCita.getCalendarView().setShowWeeks(false);
    fechaCita.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");
    
    grpCita.add(fechaCita,3,2);

    
    cmbPaciente.setItems(getPaciente());
    cmbDoctor.setItems(getDoctor());
    cmbPaciente.setDisable(true);
    cmbDoctor.setDisable(true);
    fechaCita.setDisable(true);
    }
    
    public void cargarDatos(){
        
        tblCita.setItems(getCita());
        colCita.setCellValueFactory(new PropertyValueFactory<Cita,Integer>("codigoCita"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Cita,String>("fechaCita"));
        colHora.setCellValueFactory(new PropertyValueFactory<Cita,String>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita,String>("tratamiento"));
        colDesrip.setCellValueFactory(new PropertyValueFactory<Cita,String>("descripCondActual"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<Cita,Integer>("codigoPaciente"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<Cita,Integer>("numeroColegiado"));
       
        
    }
    
    
    public ObservableList<Cita>getCita(){
        ArrayList<Cita>lista = new ArrayList<Cita>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCitas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cita(resultado.getInt("codigoCita"),
                                    resultado.getDate("fechaCita"),
                                    resultado.getString("horaCita"),
                                      resultado.getString("tratamiento"),
                                      resultado.getString("descripCondActual"),
                                       resultado.getInt("codigoPaciente"),
                                         resultado.getInt("numeroColegiado")));
            }
                
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCita = FXCollections.observableArrayList(lista);
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
                                            resultado.getInt("codigoEspecialidad"))
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
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
                                         resultado.getDate("fechaPrimeraVisita")));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPaciente = FXCollections.observableArrayList(lista);
        
    }
    
 
   
   
   
    public void seleccionarLosDatos(){
        try{
            txtCodigoCita.setText(String.valueOf(((Cita)tblCita.getSelectionModel().getSelectedItem()).getCodigoCita()));
            txtTratamiento.setText(String.valueOf(((Cita)tblCita.getSelectionModel().getSelectedItem()).getTratamiento()));
            txtHora.setText(String.valueOf(((Cita)tblCita.getSelectionModel().getSelectedItem()).getHoraCita()));
            txtDes.setText(String.valueOf(((Cita)tblCita.getSelectionModel().getSelectedItem()).getDescripCondActual()));
            fechaCita.selectedDateProperty().set(((Cita)tblCita.getSelectionModel().getSelectedItem()).getFechaCita());
            cmbDoctor.getSelectionModel().select(buscarDoctor(((Cita)tblCita.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
            cmbPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCita.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
            
            
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(null,"No Existe Ningun dato en esa casilla");
        }
    }
    
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado= new Paciente(registro.getInt("codigoPaciente"),
                          registro.getString("nombrePaciente"),
                        registro.getString("apellidosPaciente"),
                         registro.getString("sexo"),
                      registro.getDate("fechadeNacimiento"),
                     registro.getString("direccionPaciente"),
                   registro.getString("telefonoPersonal"),
                     registro.getDate("fechaPrimeraVisita"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
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
       switch (tipoOperacion){
            case NINGUNO:
               activarControles();
                limpiarControles();
               btnNuevo.setText("Guardar");
               btnEliminar.setText("Cancelar");
               btnEditar.setDisable(true);
               btnReporte.setDisable(true);
               
               imgNuevo.setImage(new Image("/org/carlosarroyo/image/Guardar.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
               tipoOperacion = operaciones.GUARDAR;
               break;
               
               case GUARDAR:
               guardar();
               fechaCita.setDisable(true);
               desactivarControles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               
               imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/EliminarPaciente.png"));
               tipoOperacion = operaciones.NINGUNO;
               cargarDatos();
               break;
               
        }
       
    }
       
           public static boolean hora(String horario){
        String[] parametros = horario.split(":");
//        System.out.println(parametros.length);
        if (parametros.length == 3){
//            System.out.println("parametros correctos");
            try {
                int hora = Integer.parseInt(parametros[0]);
                int minuto = Integer.parseInt(parametros[1]);
                int segundo = Integer.parseInt(parametros[2]);
                if (hora >= 0 && hora <= 23 && minuto >= 0 && minuto <=59 && segundo >= 0 && segundo <=59){
//                    System.out.println("hora: "+hora);
//                    System.out.println("munito: "+minuto);
//                    System.out.println("segundo: "+segundo);
                    //AQUI FORMATO CORRECTO
                    return true;
                } else {
//                    System.out.println("error valores");
                }
            } catch (Exception e) {
//                System.out.println("error parse int");
            }
        } else {
//             JOptionPane.showMessageDialog(null,"error parametros");
        }
        return false;
    }
    

       
       public void guardar(){
           
                   if (hora(txtHora.getText())) {
//            System.out.println("ingresa a la base de datos");
             Cita registro = new Cita();
       
           registro.setTratamiento(txtTratamiento.getText());
           registro.setHoraCita(txtHora.getText());
           registro.setDescripCondActual(txtDes.getText());
           registro.setFechaCita(fechaCita.getSelectedDate());
           registro.setCodigoPaciente(((Paciente)cmbPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
           registro.setNumeroColegiado(((Doctor)cmbDoctor.getSelectionModel().getSelectedItem()).getNumeroColegiado());
       
       try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCita(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime())); 
             procedimiento.setString(2, registro.getHoraCita());
             procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripCondActual());
   
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
        } else {
             JOptionPane.showMessageDialog(null,"error en el horario");
        }
    

       }
    
      public void eliminar(){

        switch(tipoOperacion){
            case GUARDAR:
                fechaCita.setDisable(true);
               desactivarControles();
               limpiarControles();
               btnNuevo.setText("Nuevo");
               btnEliminar.setText("Eliminar");
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               
               imgNuevo.setImage(new Image("/org/carlosarroyo/image/NuevoPaciente.png"));
               imgEliminar.setImage(new Image("/org/carlosarroyo/image/EliminarPaciente.png"));
               tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblCita.getSelectionModel().getSelectedItem() !=null){
                int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar este Elemento ?", "Eliminar una Cita",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (respuesta == JOptionPane.YES_OPTION){
                try{
                PreparedStatement procedimiento= Conexion.getInstance ().getConexion().prepareCall("{call sp_EliminarCita(?)}");
                procedimiento.setInt (1, ((Cita) tblCita.getSelectionModel ().getSelectedItem()).getCodigoCita());
                procedimiento.execute ();
                listaCita.remove (tblCita.getSelectionModel ().getSelectedIndex());
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
             switch (tipoOperacion){
                 case NINGUNO:
                     if (tblCita.getSelectionModel ().getSelectedItem () != null){
                         btnEditar.setText("Actualizar");
                         btnReporte.setText ("Cancelar");
                         btnNuevo.setDisable (true);
                         btnEliminar.setDisable (true);
                         imgEditar.setImage(new Image("/org/carlosarroyo/image/Actualizar.png"));
                         imgReporte.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
                         activarControles ();
                         txtCodigoCita.setEditable(false);
                         cmbPaciente.setDisable(true);
                         cmbDoctor.setDisable(true);
                         tipoOperacion=operaciones.ACTUALIZAR;

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
                   cargarDatos ();
                  desactivarControles ();
                 fechaCita.setDisable(true);
                  limpiarControles ();

                 tipoOperacion = operaciones.NINGUNO;    
                   break;


             }
             }
            
            
            
           public void actualizar(){
              if (hora(txtHora.getText())) {
//            System.out.println("ingresa a la base de datos");
       
       try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCita(?,?,?,?,?)}");
           Cita registro = (Cita)tblCita.getSelectionModel().getSelectedItem();
            registro.setTratamiento(txtTratamiento.getText());
           registro.setHoraCita(txtHora.getText());
           registro.setDescripCondActual(txtDes.getText());
           registro.setFechaCita(fechaCita.getSelectedDate());
             procedimiento.setInt(1, registro.getCodigoCita());
             procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime())); 
             procedimiento.setString(3, registro.getHoraCita());
             procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescripCondActual());

            procedimiento.execute();

       }catch(Exception e){
           e.printStackTrace();
       }
        } else {
             JOptionPane.showMessageDialog(null,"error en el horario");
        }    
                   
               }
               
               
               
               
              public void reporte (){
            switch (tipoOperacion){
               case ACTUALIZAR:
                  desactivarControles ();
                  limpiarControles ();
                   btnEditar.setText ("Editar");
                 btnReporte.setText ("Roporte");
                 btnNuevo.setDisable (false);
                  btnEliminar.setDisable (false);
                imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
                imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
                tblCita.getSelectionModel().clearSelection();
                tipoOperacion = operaciones.NINGUNO;

         break;

  }
  
  }
            


   
        public void desactivarControles(){
        txtCodigoCita.setEditable(false);
        txtTratamiento.setEditable(false);
        txtHora.setEditable(false);
        txtDes.setEditable(false);
        cmbPaciente.setDisable(true);
        cmbDoctor.setDisable(true);
        fechaCita.setDisable(true);
    }
    
    public void activarControles(){

        txtTratamiento.setEditable(true);
        txtHora.setEditable(true);
        txtDes.setEditable(true);
        cmbPaciente.setDisable(false);
        cmbDoctor.setDisable(false);
         fechaCita.setDisable(false);
        
    }
    public void limpiarControles(){
        txtCodigoCita.clear();
        txtTratamiento.clear();
        txtHora.clear();
        txtDes.clear();
        cmbPaciente.setValue(null);
        cmbDoctor.setValue(null);
        tblCita.getSelectionModel().clearSelection();
        
    fechaCita = new DatePicker(Locale.ENGLISH);
    fechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    fechaCita.getCalendarView().todayButtonTextProperty().set("Today");
    fechaCita.getCalendarView().setShowWeeks(false);
    fechaCita.getStylesheets().add("org/carlosarroyo/resource/DatePicker.css");
    grpCita.add(fechaCita,3,2);
       
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

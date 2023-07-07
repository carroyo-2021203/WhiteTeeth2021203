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
import javafx.scene.layout.GridPane;
import org.carlosarroyo.bean.Medicamento;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.system.Principal;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;


public class MedicamentoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO ,ELIMINAR ,EDITAR ,ACTUALIZAR ,CANCELAR ,NINGUNO, GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medicamento>listaMedicamento;
    
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombre;
    @FXML private TableView tblMedicamento;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colNombres;
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
        tblMedicamento.setItems(getMedicamento());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento,Integer>("codigoMedicamento"));
        colNombres.setCellValueFactory(new PropertyValueFactory <Medicamento,String>("nombreMedicamento"));

    
}
    
    public void SeleccionarElementos(){
        try{
            txtCodigoMedicamento.setText(String.valueOf(((Medicamento)tblMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            txtNombre.setText(String.valueOf(((Medicamento)tblMedicamento.getSelectionModel().getSelectedItem()).getNombreMedicamento()));
        }catch(NullPointerException e) {
	        JOptionPane.showMessageDialog(null,"No Existe ningún dato en esa casilla");
		}
    }
    
    
    public ObservableList<Medicamento>getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList <Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while (resultado.next()){
                lista.add(new Medicamento (resultado.getInt("codigoMedicamento"),
                                          resultado.getString("nombreMedicamento")
                                                  
                ) );
            }
            
            
            
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
         return listaMedicamento = FXCollections.observableArrayList(lista);
         
    
}
    
    
    
    public void nuevo (){
        switch(tipoDeOperacion){
            case NINGUNO:
               
                activarControles();
                limpiarControles();
               
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelaar");
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
                if(tblMedicamento.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar este Elemento?", "Eliminar Un Medicamento",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if   (respuesta == JOptionPane.YES_OPTION){
                     
                     try {
                            PreparedStatement procedimiento=Conexion.getInstance ().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                procedimiento.setInt (1, ((Medicamento) tblMedicamento.getSelectionModel ().getSelectedItem()).getCodigoMedicamento());
              
                procedimiento.execute ();
                listaMedicamento.remove (tblMedicamento.getSelectionModel ().getSelectedIndex());
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
          if (tblMedicamento.getSelectionModel ().getSelectedItem () != null){
              btnEditar.setText("Actualizar");
              btnReporte.setText ("Cancelar");
              btnNuevo.setDisable (true);
              btnEliminar.setDisable (true);
              imgEditar.setImage(new Image("/org/carlosarroyo/image/Actualizar.png"));
              imgReporte.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
              activarControles ();
              txtCodigoMedicamento.setEditable (false);
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
        try {
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?,?)}");
         Medicamento registro = (Medicamento)tblMedicamento.getSelectionModel().getSelectedItem();
         registro.setNombreMedicamento(txtNombre.getText());
         procedimiento.setInt(1, registro.getCodigoMedicamento());
        procedimiento.setString(2, registro.getNombreMedicamento());
             procedimiento.execute();
            }catch(Exception e){
    e.printStackTrace();
}
            
        }

      
  public void reporte (){
  switch (tipoDeOperacion){
     case ACTUALIZAR:
        dasactivarControles ();
        limpiarControles ();
         btnEditar.setText ("Editar");
        btnReporte.setText ("Roporte");
        btnNuevo.setDisable (false);
         btnEliminar.setDisable (false);
         imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
         imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
         tblMedicamento.getSelectionModel().clearSelection();
         tipoDeOperacion = operaciones.NINGUNO;


         break;

  }
  
  }
  
    
    public void dasactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombre.setEditable(false);
        
    }
    
    public void activarControles(){
    txtNombre.setEditable(true);
}
    
    
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtNombre.clear();
        tblMedicamento.getSelectionModel().clearSelection();
    }
    
    
    

    
    
    
    public void guardar(){
    Medicamento registro = new Medicamento();
   
    registro.setNombreMedicamento(txtNombre.getText());
    
    try {
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamento(?)}");
      
        procedimiento.setString(1, registro.getNombreMedicamento());
        procedimiento.execute();
        listaMedicamento.add(registro);
        
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



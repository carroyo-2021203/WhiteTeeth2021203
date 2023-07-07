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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.carlosarroyo.bean.DetalleReceta;
import org.carlosarroyo.bean.Medicamento;
import org.carlosarroyo.bean.Receta;
import org.carlosarroyo.db.Conexion;
import org.carlosarroyo.system.Principal;


public class DetalleRecetaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {CANCELAR ,ELIMINAR ,EDITAR , GUARDAR,NUEVO ,NINGUNO, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleReceta>listaDetalle;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Medicamento> listaMedicamento;
    
    
    
    @FXML private TextField txtCodigoDetalle;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbReceta;
    @FXML private ComboBox cmbMedicamento;
    @FXML private TableView tblDetalle;
    @FXML private TableColumn colCodigoDetalle;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colReceta;
    @FXML private TableColumn colMedicamento;
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
    cmbReceta.setItems(getReceta());
    cmbReceta.setDisable(true);
    cmbMedicamento.setItems(getMedicamento());
    cmbMedicamento.setDisable(true);
    }
    
    public void cargarDatos(){
        
        tblDetalle.setItems(getDetalle());
        colCodigoDetalle.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta,String>("dosis"));
        colReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoReceta"));
        colMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoMedicamento"));

       
        
    }
    
    public ObservableList<DetalleReceta>getDetalle(){
        ArrayList<DetalleReceta>lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta (resultado.getInt("codigoDetalleReceta"),
                                            resultado.getString("dosis"),
                                            resultado.getInt("codigoReceta"),
                                            resultado.getInt("codigoMedicamento") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalle = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Medicamento>getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList <Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
    while (resultado.next()){
                lista.add(new Medicamento (resultado.getInt("codigoMedicamento"),
                                               resultado.getString("nombreMedicamento")) );
            }

            
        }catch(Exception e){
            e.printStackTrace();
            
        } 
               return listaMedicamento = FXCollections.observableArrayList(lista);
}
    
   
   
    public void seleccionarDatos(){
        try{
            txtCodigoDetalle.setText(String.valueOf(((DetalleReceta)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
            txtDosis.setText(String.valueOf(((DetalleReceta)tblDetalle.getSelectionModel().getSelectedItem()).getDosis()));
            cmbReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            cmbMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(null,"No Existe ningún dato en esa casilla");
        }
    }
    
    
    public Receta buscarReceta(int codigoReceta){
        Receta resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Receta (registro.getInt("codigoReceta"),
                                        registro.getDate("fechaReceta"),
                                        registro.getInt("numeroColegiado")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
        public Medicamento buscarMedicamento(int codigoMedicamento){
        Medicamento resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado= new Medicamento(registro.getInt("codigoMedicamento"),
                                           registro.getString("nombreMedicamento"));
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
               txtCodigoDetalle.setEditable(false);
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
           DetalleReceta registro = new DetalleReceta();
 
           registro.setDosis(txtDosis.getText());
           registro.setCodigoMedicamento(((Medicamento)cmbMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
           registro.setCodigoReceta(((Receta)cmbReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
       
       try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleReceta(? ,? ,?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            listaDetalle.add(registro);
            procedimiento.execute();
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
                if(tblDetalle.getSelectionModel().getSelectedItem() !=null){
                int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar este Elemento ?", "Eliminar un Detalle de La receta",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane. YES_OPTION){
                try{
                PreparedStatement procedimiento=Conexion.getInstance ().getConexion().prepareCall("{call sp_EliminarDetalleReceta(?)}");
                procedimiento.setInt (1, ((DetalleReceta) tblDetalle.getSelectionModel ().getSelectedItem()).getCodigoDetalleReceta());
                procedimiento.execute ();
                listaDetalle.remove (tblDetalle.getSelectionModel ().getSelectedIndex());
                limpiarControles ();
                }catch (Exception e){
                e.printStackTrace ();

                    
                }
       }else{
                    limpiarControles();    
                    cmbMedicamento.getSelectionModel().clearSelection();
        cmbReceta.getSelectionModel().clearSelection();
                    
                }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe selecionar un elemento");

                }
   }
   }
            public void editar (){
             switch (tipoDeOperacion){
                 case NINGUNO:
                     if (tblDetalle.getSelectionModel ().getSelectedItem () != null){
                         btnEditar.setText("Actualizar");
                         btnReporte.setText ("Cancelar");
                         btnNuevo.setDisable (true);
                         btnEliminar.setDisable (true);
                         imgEditar.setImage(new Image("/org/carlosarroyo/image/Actualizar.png"));
                         imgReporte.setImage(new Image("/org/carlosarroyo/image/Cancelar.png"));
                         activarControles ();
                     
                             cmbReceta.setDisable(true);
                             cmbMedicamento.setDisable(true);
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
                    cargarDatos ();
                   desactivarControles ();
                  limpiarControles ();
                  

                 tipoDeOperacion = operaciones.NINGUNO;    
                   break;


             }
             }
            
               public void actualizar(){
          try {
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalleReceta(?,?)}");
         DetalleReceta registro = (DetalleReceta)tblDetalle.getSelectionModel().getSelectedItem();
            registro.setDosis(txtDosis.getText());

            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2, registro.getDosis());

            listaDetalle.add(registro);
            procedimiento.execute();


            }catch(Exception e){
             e.printStackTrace();
                  }
               }
               
               
               
               
              public void reporte (){
            switch (tipoDeOperacion){
               case ACTUALIZAR:
                  desactivarControles ();
                  limpiarControles ();
                   btnEditar.setText ("Editar");
                  btnReporte.setText ("Roporte");
                  btnNuevo.setDisable (false);
                   btnEliminar.setDisable (false);
                    imgEditar.setImage(new Image("/org/carlosarroyo/image/EditarPaciente.png"));
                    imgReporte.setImage(new Image("/org/carlosarroyo/image/ReportedePaciente.png"));
                    tblDetalle.getSelectionModel().clearSelection();
                    tipoDeOperacion = operaciones.NINGUNO;

         break;

  }
  
  }
            


   
        public void desactivarControles(){
        txtCodigoDetalle.setEditable(false);
        txtDosis.setEditable(false);
        cmbReceta.setDisable(true);
        cmbMedicamento.setDisable(true);
        
    }
    
    public void activarControles(){
      
        txtDosis.setEditable(true);
        cmbReceta.setDisable(false);
        cmbMedicamento.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoDetalle.clear();
        txtDosis.clear();
        tblDetalle.getSelectionModel().clearSelection();
        cmbMedicamento.setValue(null);
        cmbReceta.setValue(null);
        
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

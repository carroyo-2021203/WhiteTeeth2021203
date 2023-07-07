/*
Carlos Arnoldo Arroyo Gudiel
IN5AV
2021203
05/04/2022
Modificaciones:
*   20/04/2022 
* 
* 
 */
package org.carlosarroyo.system;

    import java.io.IOException;
    import java.io.InputStream;
    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.fxml.JavaFXBuilderFactory;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.image.Image;
    import javafx.scene.layout.AnchorPane;
    import javafx.stage.Stage;
    import org.carlosarroyo.controller.CitasController;
    import org.carlosarroyo.controller.DetalleRecetaController;
    import org.carlosarroyo.controller.DoctorController;
    import org.carlosarroyo.controller.EspecialidadController;
    import org.carlosarroyo.controller.LoginController;
    import org.carlosarroyo.controller.MedicamentoController;
    import org.carlosarroyo.controller.MenuPrincipalController;
    import org.carlosarroyo.controller.PacienteController;
    import org.carlosarroyo.controller.ProgramadorController;
    import org.carlosarroyo.controller.RecetasController;
    import org.carlosarroyo.controller.UsuarioController;

/**
 *
 * @author Carlos
 **/

public class Principal extends Application {
    
    private Stage escenarioPrincipal ;
    private Scene escena ;
    private final String PAQUTE_VISTA = "/org/carlosarroyo/view/";
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("White Teeth");
        escenarioPrincipal.getIcons().add(new Image("/org/carlosarroyo/image/Logo.png"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/carlosarroyo/view/MenuPrincipalView.fxml"))  ;  
//        Scene escena = new Scene(root);
//        escenarioPrincipal.setScene(escena);

    menuPrincipal();
    // ventanaLogin();
        escenarioPrincipal.show();
    }
    
        public void ventanaLogin(){
        try{
            LoginController vistaLogin = (LoginController)cambiarEscena("LoginView.fxml",600,400);
            vistaLogin.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
        public void ventanaUsuario(){
        try{
            UsuarioController vistaUsuario = (UsuarioController)cambiarEscena("UsuarioView.fxml",926,414);
            vistaUsuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void menuPrincipal(){
        try{
            MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",397,400);
            ventanaMenu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
    public void ventanaProgramador (){
        try{
            ProgramadorController vistaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",632,425);
            vistaProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
    
    public void ventanaPaciente(){
        try{
            PacienteController vistaPaciente = (PacienteController)cambiarEscena("PacientesView.fxml",1087,415);
            vistaPaciente.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
        
    public void ventanaEspecialidad(){
        try{
            EspecialidadController vistaEspecialidad = (EspecialidadController)cambiarEscena("EspecialidadesView.fxml",944,415);
            
            vistaEspecialidad.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
    public void ventanaMedicamento(){
        try{
            MedicamentoController vistaMedicamentos = (MedicamentoController)cambiarEscena("MedicamentosView.fxml",944,415);
            
            vistaMedicamentos.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
    
    public void ventanaDoctor(){
        try{
            DoctorController vistaDoctor = (DoctorController)cambiarEscena("DoctoresView.fxml",1117,418);
            
            vistaDoctor.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
    
    public void ventanaReceta(){
        try{
            RecetasController vistaReceta = (RecetasController)cambiarEscena("RecetasView.fxml",915,430);
            
            vistaReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
    
  public void ventanaDetalleReceta(){
        try{
            DetalleRecetaController vistaDetalleReceta = (DetalleRecetaController)cambiarEscena("DetalleRecetasView.fxml",932,438);
            
            vistaDetalleReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
  public void ventanaCita(){
        try{
            CitasController vistaCitas = (CitasController)cambiarEscena("CitasView.fxml",1186,411);
            
            vistaCitas.setEscenarioPrincipal(this);
        }catch(Exception e){
              e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    
    public Initializable cambiarEscena(String fxml,int ancho, int alto) throws IOException{
        Initializable resultado = null;
       
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUTE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUTE_VISTA+fxml));
        
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
        
    }
    
    
}












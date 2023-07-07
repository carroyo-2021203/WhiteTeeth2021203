
package org.carlosarroyo.bean;


public class Doctor {
  
    private int numeroColegiado ;
    private String nombreDoctor ;
    private String apellidosDoctor ;
    private String telefonoContacto;
    private int codigoEspecialidad;

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getApellidosDoctor() {
        return apellidosDoctor;
    }

    public void setApellidosDoctor(String apellidosDoctor) {
        this.apellidosDoctor = apellidosDoctor;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public Doctor(int numeroColegiado, String nombreDoctor, String apellidosDoctor, String telefonoContacto, int codigoEspecialidad) {
        this.numeroColegiado = numeroColegiado;
        this.nombreDoctor = nombreDoctor;
        this.apellidosDoctor = apellidosDoctor;
        this.telefonoContacto = telefonoContacto;
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public Doctor() {
    }

    @Override
    public String toString() {
        return getNumeroColegiado()+" | "+getNombreDoctor()+" | " +getApellidosDoctor() +" | "+getTelefonoContacto() ;
    }



}


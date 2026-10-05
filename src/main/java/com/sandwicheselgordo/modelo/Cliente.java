/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

public class Cliente {

    private int idCliente;
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private int puntos;
    private boolean activo;

    public Cliente() {
        puntos = 0;
        activo = true;
    }

    public Cliente(int idCliente,
                   String identificacion,
                   String nombre,
                   String telefono,
                   String correo,
                   String direccion,
                   int puntos,
                   boolean activo) {

        this.idCliente = idCliente;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        setPuntos(puntos);
        this.activo = activo;
    }

    public void acreditarPuntos(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "Los puntos deben ser mayores que cero."
            );
        }

        puntos += cantidad;
    }

    public void descontarPuntos(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "Los puntos deben ser mayores que cero."
            );
        }

        if (cantidad > puntos) {
            throw new IllegalArgumentException(
                    "El cliente no tiene suficientes puntos."
            );
        }

        puntos -= cantidad;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {

        if (puntos < 0) {
            throw new IllegalArgumentException(
                    "Los puntos no pueden ser negativos."
            );
        }

        this.puntos = puntos;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

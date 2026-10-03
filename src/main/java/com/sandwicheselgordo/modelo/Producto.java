/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

public class Producto {

    private int idProducto;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipoProducto;
    private String marca;
    private double precio;
    private int existencia;
    private int idCategoria;
    private boolean activo;

    public Producto() {
    }

    public Producto(int idProducto,
                    String codigo,
                    String nombre,
                    String descripcion,
                    String tipoProducto,
                    String marca,
                    double precio,
                    int existencia,
                    int idCategoria,
                    boolean activo) {

        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoProducto = tipoProducto;
        this.marca = marca;
        setPrecio(precio);
        setExistencia(existencia);
        this.idCategoria = idCategoria;
        this.activo = activo;
    }

    public boolean hayExistencia(int cantidad) {
        return cantidad > 0 && existencia >= cantidad;
    }

    public void descontarExistencia(int cantidad) {

        if (!hayExistencia(cantidad)) {
            throw new IllegalArgumentException(
                    "Existencia insuficiente para " + nombre
            );
        }

        existencia -= cantidad;
    }

    public void aumentarExistencia(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        existencia += cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {

        if (precio < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo."
            );
        }

        this.precio = precio;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {

        if (existencia < 0) {
            throw new IllegalArgumentException(
                    "La existencia no puede ser negativa."
            );
        }

        this.existencia = existencia;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}

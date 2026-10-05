/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

import java.time.LocalDateTime;

public class Canje {

    private int idCanje;
    private Cliente cliente;
    private Recompensa recompensa;
    private LocalDateTime fecha;
    private int puntosUtilizados;
    private String estado;

    public Canje(int idCanje,
                 Cliente cliente,
                 Recompensa recompensa) {

        this.idCanje = idCanje;
        this.cliente = cliente;
        this.recompensa = recompensa;
        this.fecha = LocalDateTime.now();
        this.puntosUtilizados =
                recompensa.getPuntosNecesarios();

        this.estado = "APROBADO";
    }

    public int getIdCanje() {
        return idCanje;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Recompensa getRecompensa() {
        return recompensa;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getPuntosUtilizados() {
        return puntosUtilizados;
    }

    public String getEstado() {
        return estado;
    }
}

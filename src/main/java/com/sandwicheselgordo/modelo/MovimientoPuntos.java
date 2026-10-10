package com.sandwicheselgordo.modelo;

import java.time.LocalDateTime;

public class MovimientoPuntos {

    private int idMovimiento;
    private Cliente cliente;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private int puntos;
    private String referencia;

    public MovimientoPuntos(
            int idMovimiento,
            Cliente cliente,
            String tipoMovimiento,
            int puntos,
            String referencia) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                "El cliente es obligatorio."
            );
        }

        if (tipoMovimiento == null ||
                (!tipoMovimiento.equals("ACUMULACION") &&
                 !tipoMovimiento.equals("CANJE"))) {

            throw new IllegalArgumentException(
                "El tipo debe ser ACUMULACION o CANJE."
            );
        }

        if (puntos <= 0) {
            throw new IllegalArgumentException(
                "Los puntos deben ser mayores que cero."
            );
        }

        this.idMovimiento = idMovimiento;
        this.cliente = cliente;
        this.fecha = LocalDateTime.now();
        this.tipoMovimiento = tipoMovimiento;
        this.puntos = puntos;
        this.referencia = referencia;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getReferencia() {
        return referencia;
    }
}
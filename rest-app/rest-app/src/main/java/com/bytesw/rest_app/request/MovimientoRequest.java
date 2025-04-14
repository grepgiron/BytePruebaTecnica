package com.bytesw.rest_app.request;

import com.bytesw.rest_app.models.Cuenta;
import com.bytesw.rest_app.models.Movimiento;
import java.util.List;

public class MovimientoRequest {
    private Cuenta cuenta;
    private List<Movimiento> movimientos;

    public MovimientoRequest(Cuenta cuenta, List<Movimiento> movimientos) {
        this.cuenta = cuenta;
        this.movimientos = movimientos;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
    
}


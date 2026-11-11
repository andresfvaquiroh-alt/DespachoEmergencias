package despacho.chain;

import despacho.modelo.ReporteEmergencia;

public class OperadorLinea extends ManejadorAutorizacion {
    @Override
    protected boolean puedeAutorizar(ReporteEmergencia r) {
        return r.gravedad() <= 2 && r.personasAfectadas() <= 2;
    }

    @Override
    protected String cargo() { return "Operador de línea"; }
}

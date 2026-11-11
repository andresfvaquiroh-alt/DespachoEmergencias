package despacho.chain;

import despacho.modelo.ReporteEmergencia;

public class SupervisorTurno extends ManejadorAutorizacion {
    @Override
    protected boolean puedeAutorizar(ReporteEmergencia r) {
        return r.gravedad() <= 4 && r.personasAfectadas() <= 10;
    }

    @Override
    protected String cargo() { return "Supervisor de turno"; }
}

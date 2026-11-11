package despacho.chain;

import despacho.modelo.ReporteEmergencia;

/** Último eslabón: autoriza cualquier reporte. */
public class DirectorOperaciones extends ManejadorAutorizacion {
    @Override
    protected boolean puedeAutorizar(ReporteEmergencia r) { return true; }

    @Override
    protected String cargo() { return "Director de operaciones"; }
}

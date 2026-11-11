package despacho.state;

import despacho.modelo.Incidente;

public class EstadoReportado implements EstadoIncidente {
    @Override public String nombre() { return "REPORTADO"; }

    @Override public void despachar(Incidente i) { i.cambiarEstado(new EstadoDespachado()); }

    @Override public void cancelar(Incidente i) { i.cambiarEstado(new EstadoCancelado()); }
}

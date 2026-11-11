package despacho.state;

import despacho.modelo.Incidente;

public class EstadoDespachado implements EstadoIncidente {
    @Override public String nombre() { return "DESPACHADO"; }

    @Override public void iniciarAtencion(Incidente i) { i.cambiarEstado(new EstadoEnAtencion()); }

    @Override public void cancelar(Incidente i) { i.cambiarEstado(new EstadoCancelado()); }
}

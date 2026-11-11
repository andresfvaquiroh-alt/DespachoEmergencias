package despacho.state;

import despacho.modelo.Incidente;

public class EstadoEnAtencion implements EstadoIncidente {
    @Override public String nombre() { return "EN_ATENCION"; }

    /** Cuando llega la 2.ª o 3.ª unidad el incidente ya está en atención: no pasa nada. */
    @Override public void iniciarAtencion(Incidente i) { }

    @Override public void resolver(Incidente i) { i.cambiarEstado(new EstadoResuelto()); }

    @Override public void cancelar(Incidente i) { i.cambiarEstado(new EstadoCancelado()); }
}

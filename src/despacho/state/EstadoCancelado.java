package despacho.state;

/** Estado final: ninguna transición está permitida. */
public class EstadoCancelado implements EstadoIncidente {
    @Override public String nombre() { return "CANCELADO"; }
}

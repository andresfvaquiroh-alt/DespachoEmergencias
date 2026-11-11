package despacho.state;

/** Estado final: ninguna transición está permitida. */
public class EstadoResuelto implements EstadoIncidente {
    @Override public String nombre() { return "RESUELTO"; }
}

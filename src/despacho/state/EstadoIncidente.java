package despacho.state;

import despacho.modelo.Incidente;

/**
 * Interfaz del patrón STATE. Por defecto toda transición es inválida;
 * cada estado concreto sobrescribe solo las que le están permitidas.
 */
public interface EstadoIncidente {
    String nombre();

    default void despachar(Incidente i) { rechazar("despachar"); }

    default void iniciarAtencion(Incidente i) { rechazar("iniciar la atención de"); }

    default void resolver(Incidente i) { rechazar("resolver"); }

    default void cancelar(Incidente i) { rechazar("cancelar"); }

    private void rechazar(String accion) {
        throw new IllegalStateException(
                "Transición no permitida: no se puede " + accion + " un incidente en estado " + nombre());
    }
}

package despacho.template;

import despacho.modelo.ReporteEmergencia;
import despacho.modelo.TipoIncidente;
import despacho.modelo.TipoUnidad;

import java.util.List;

/**
 * TEMPLATE METHOD: planificar() fija el esqueleto del protocolo (siempre en el mismo orden);
 * cada tipo de emergencia solo rellena los pasos abstractos y, si quiere, los hooks.
 */
public abstract class ProtocoloAtencion {

    // ----- método plantilla (final: nadie puede cambiar el orden de los pasos) -----
    public final List<TipoUnidad> planificar(ReporteEmergencia reporte) {
        validarDatos(reporte);                                        // paso fijo
        System.out.println("  [PROTOCOLO " + nombre() + "] aplicando protocolo");
        List<TipoUnidad> unidades = seleccionarUnidades(reporte);     // paso abstracto
        System.out.println("  [PROTOCOLO " + nombre() + "] unidades requeridas: " + unidades);
        System.out.println("  [PROTOCOLO " + nombre() + "] instrucción al ciudadano: " + instruccionCiudadano(reporte));
        if (requiereAvisoExterno(reporte)) {                          // hook
            avisarEntidadExterna(reporte);
        }
        return unidades;
    }

    private void validarDatos(ReporteEmergencia r) {
        if (r.telefono() == null || r.telefono().isBlank()) {
            throw new IllegalArgumentException("Reporte sin teléfono de contacto");
        }
        if (r.gravedad() < 1 || r.gravedad() > 5) {
            throw new IllegalArgumentException("La gravedad debe estar entre 1 y 5");
        }
    }

    // ----- pasos que cambian según el tipo de emergencia -----
    protected abstract String nombre();

    protected abstract List<TipoUnidad> seleccionarUnidades(ReporteEmergencia reporte);

    protected abstract String instruccionCiudadano(ReporteEmergencia reporte);

    // ----- hooks (opcionales) -----
    protected boolean requiereAvisoExterno(ReporteEmergencia reporte) { return false; }

    protected void avisarEntidadExterna(ReporteEmergencia reporte) { }

    // Ayuda para el Main/Central (no cuenta como uno de los 8 patrones)
    public static ProtocoloAtencion para(TipoIncidente tipo) {
        return switch (tipo) {
            case INCENDIO -> new ProtocoloIncendio();
            case ACCIDENTE_VIAL -> new ProtocoloAccidenteVial();
            case EMERGENCIA_MEDICA -> new ProtocoloEmergenciaMedica();
        };
    }
}

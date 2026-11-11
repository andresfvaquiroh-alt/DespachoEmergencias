package despacho;

import despacho.modelo.Incidente;
import despacho.modelo.ReporteEmergencia;
import despacho.modelo.TipoIncidente;
import despacho.modelo.Ubicacion;
import despacho.modelo.Unidad;

import java.util.ArrayList;

/**
 * Caso de uso completo: Sistema de Despacho de Emergencias.
 * Recorre los 8 patrones: Chain of Responsibility, Template Method, Strategy, Command,
 * Memento, State, Mediator y Observer.
 */
public class Main {

    public static void main(String[] args) {
        CentralEmergencias central = new CentralEmergencias();

        // ---------------------------------------------------------------
        titulo("CASO 1: incendio grave -> autoriza el supervisor, estrategia MAS_CERCANA");
        Incidente i1 = central.registrarReporte(new ReporteEmergencia(
                "María Gómez", "3001112233", TipoIncidente.INCENDIO,
                new Ubicacion("Calle 12 con Carrera 14", 2, 2),
                "Incendio en bodega de reciclaje", 4, 3));
        central.despachar(i1);
        atender(i1);

        // ---------------------------------------------------------------
        titulo("CASO 2: accidente leve -> autoriza el operador, estrategia MENOR_CARGA, con UNDO");
        Incidente i2 = central.registrarReporte(new ReporteEmergencia(
                "Carlos Pérez", "3004445566", TipoIncidente.ACCIDENTE_VIAL,
                new Ubicacion("Avenida 8 con Calle 3", 3, 3),
                "Choque entre dos motos", 2, 1));
        central.despachar(i2);
        System.out.println("  >> El operador se equivocó de incidente: deshace el despacho");
        central.deshacerUltimo();
        System.out.println("  >> Estado actual del incidente #" + i2.getId() + ": " + i2.getEstado().nombre());
        System.out.println("  >> Lo despacha de nuevo, ahora sí");
        central.despachar(i2);
        atender(i2);

        // ---------------------------------------------------------------
        titulo("CASO 3: emergencia médica crítica -> director, luego falsa alarma (cancelación)");
        Incidente i3 = central.registrarReporte(new ReporteEmergencia(
                "Ana Torres", "3007778899", TipoIncidente.EMERGENCIA_MEDICA,
                new Ubicacion("Carrera 20 con Calle 9", 6, 6),
                "Persona inconsciente en la vía", 5, 1));
        central.despachar(i3);
        central.cancelar(i3);
        try {
            i3.resolver();
        } catch (IllegalStateException e) {
            System.out.println("  [ERROR ESPERADO - State] " + e.getMessage());
        }

        // ---------------------------------------------------------------
        titulo("RESUMEN FINAL");
        central.imprimirResumen();
    }

    /** Simula que las unidades despachadas llegan al sitio y terminan su servicio. */
    private static void atender(Incidente incidente) {
        for (Unidad u : new ArrayList<>(incidente.getUnidades())) {
            u.llegarAlSitio();
        }
        for (Unidad u : new ArrayList<>(incidente.getUnidades())) {
            u.finalizarServicio();
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println(texto);
        System.out.println("=".repeat(80));
    }
}

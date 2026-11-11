package despacho.mediator;

import despacho.modelo.Incidente;
import despacho.modelo.TipoUnidad;
import despacho.modelo.Unidad;
import despacho.strategy.EstrategiaAsignacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MEDIATOR concreto. Las unidades nunca se hablan entre sí: todo pasa por aquí.
 * Además es el CONTEXT del patrón STRATEGY (usa la estrategia de asignación vigente).
 */
public class CentroDespacho implements Mediador {
    private final List<Unidad> unidades = new ArrayList<>();
    private EstrategiaAsignacion estrategia;

    public void registrarUnidad(Unidad u) { unidades.add(u); }

    public void setEstrategia(EstrategiaAsignacion estrategia) { this.estrategia = estrategia; }

    public EstrategiaAsignacion getEstrategia() { return estrategia; }

    public List<Unidad> getUnidades() { return unidades; }

    public Optional<Unidad> asignarUnidad(TipoUnidad tipo, Incidente incidente) {
        List<Unidad> candidatas = unidades.stream()
                .filter(u -> u.getTipo() == tipo && u.isDisponible())
                .toList();

        Optional<Unidad> elegida = estrategia.seleccionar(candidatas, incidente);
        elegida.ifPresent(u -> {
            u.asignar(incidente);
            incidente.agregarUnidad(u);
            System.out.println("  [CENTRO] Asigna " + u + " al incidente #" + incidente.getId());
        });
        return elegida;
    }

    public void liberarUnidades(Incidente incidente) {
        for (Unidad u : incidente.getUnidades()) {
            if (u.getIncidenteActual() == incidente) {
                u.liberar();
                System.out.println("  [CENTRO] Libera " + u);
            }
        }
    }

    public void reocuparUnidades(Incidente incidente) {
        for (Unidad u : incidente.getUnidades()) {
            u.asignar(incidente);
            System.out.println("  [CENTRO] Reasigna " + u + " al incidente #" + incidente.getId());
        }
    }

    @Override
    public void notificar(Unidad emisor, EventoDespacho evento, Incidente incidente) {
        switch (evento) {
            case UNIDAD_LLEGO -> incidente.iniciarAtencion();
            case SERVICIO_FINALIZADO -> {
                emisor.liberar();
                boolean todasTerminaron = incidente.getUnidades().stream()
                        .allMatch(u -> u.getIncidenteActual() != incidente);
                if (todasTerminaron) {
                    incidente.resolver();
                }
            }
        }
    }
}

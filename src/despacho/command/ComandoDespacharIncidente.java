package despacho.command;

import despacho.mediator.CentroDespacho;
import despacho.memento.CuidadorMementos;
import despacho.modelo.Incidente;
import despacho.modelo.TipoUnidad;

import java.util.List;

/** COMMAND concreto: encapsula la orden "despachar unidades a un incidente", con undo vía MEMENTO. */
public class ComandoDespacharIncidente implements Comando {
    private final CentroDespacho centro;
    private final Incidente incidente;
    private final List<TipoUnidad> unidadesRequeridas;
    private final CuidadorMementos cuidador;

    public ComandoDespacharIncidente(CentroDespacho centro, Incidente incidente,
                                     List<TipoUnidad> unidadesRequeridas, CuidadorMementos cuidador) {
        this.centro = centro;
        this.incidente = incidente;
        this.unidadesRequeridas = unidadesRequeridas;
        this.cuidador = cuidador;
    }

    @Override
    public void ejecutar() {
        cuidador.guardar(incidente.getId(), incidente.crearMemento());   // MEMENTO antes de cambiar nada

        int asignadas = 0;
        for (TipoUnidad tipo : unidadesRequeridas) {
            if (centro.asignarUnidad(tipo, incidente).isPresent()) {
                asignadas++;
            } else {
                System.out.println("  [CENTRO] Sin " + tipo + " disponible para el incidente #" + incidente.getId());
            }
        }
        if (asignadas == 0) {
            cuidador.recuperarUltimo(incidente.getId());
            throw new IllegalStateException("No hay unidades disponibles para el incidente #" + incidente.getId());
        }
        incidente.despachar();                                           // STATE: REPORTADO -> DESPACHADO
    }

    @Override
    public void deshacer() {
        centro.liberarUnidades(incidente);
        incidente.restaurar(cuidador.recuperarUltimo(incidente.getId()));
    }

    @Override
    public String descripcion() {
        return "Despachar incidente #" + incidente.getId() + " " + unidadesRequeridas;
    }
}

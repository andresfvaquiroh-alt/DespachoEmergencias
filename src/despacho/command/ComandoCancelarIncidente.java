package despacho.command;

import despacho.mediator.CentroDespacho;
import despacho.memento.CuidadorMementos;
import despacho.modelo.Incidente;

/** COMMAND concreto: cancela un incidente (falsa alarma) y libera sus unidades. */
public class ComandoCancelarIncidente implements Comando {
    private final CentroDespacho centro;
    private final Incidente incidente;
    private final CuidadorMementos cuidador;

    public ComandoCancelarIncidente(CentroDespacho centro, Incidente incidente, CuidadorMementos cuidador) {
        this.centro = centro;
        this.incidente = incidente;
        this.cuidador = cuidador;
    }

    @Override
    public void ejecutar() {
        cuidador.guardar(incidente.getId(), incidente.crearMemento());
        try {
            incidente.cancelar();                                        // STATE valida la transición
        } catch (IllegalStateException e) {
            cuidador.recuperarUltimo(incidente.getId());                 // no quedó nada que deshacer
            throw e;
        }
        centro.liberarUnidades(incidente);
    }

    @Override
    public void deshacer() {
        incidente.restaurar(cuidador.recuperarUltimo(incidente.getId()));
        centro.reocuparUnidades(incidente);
    }

    @Override
    public String descripcion() {
        return "Cancelar incidente #" + incidente.getId();
    }
}

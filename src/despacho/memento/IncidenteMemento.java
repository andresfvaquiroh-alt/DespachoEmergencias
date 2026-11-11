package despacho.memento;

import despacho.modelo.Unidad;
import despacho.state.EstadoIncidente;

import java.util.List;

/** MEMENTO: instantánea inmutable del estado y de las unidades de un incidente. */
public final class IncidenteMemento {
    private final EstadoIncidente estado;
    private final List<Unidad> unidades;

    public IncidenteMemento(EstadoIncidente estado, List<Unidad> unidades) {
        this.estado = estado;
        this.unidades = List.copyOf(unidades);
    }

    public EstadoIncidente getEstado() { return estado; }

    public List<Unidad> getUnidades() { return unidades; }
}

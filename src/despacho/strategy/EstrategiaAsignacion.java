package despacho.strategy;

import despacho.modelo.Incidente;
import despacho.modelo.Unidad;

import java.util.List;
import java.util.Optional;

public interface EstrategiaAsignacion {
    Optional<Unidad> seleccionar(List<Unidad> candidatas, Incidente incidente);

    String nombre();
}

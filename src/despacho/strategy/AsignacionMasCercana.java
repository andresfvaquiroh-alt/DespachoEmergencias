package despacho.strategy;

import despacho.modelo.Incidente;
import despacho.modelo.Ubicacion;
import despacho.modelo.Unidad;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Para emergencias graves: gana la unidad con menor tiempo de llegada (distancia). */
public class AsignacionMasCercana implements EstrategiaAsignacion {

    @Override
    public Optional<Unidad> seleccionar(List<Unidad> candidatas, Incidente incidente) {
        Ubicacion destino = incidente.getReporte().ubicacion();
        return candidatas.stream()
                .min(Comparator.comparingDouble(u -> u.getUbicacion().distanciaA(destino)));
    }

    @Override
    public String nombre() { return "MAS_CERCANA"; }
}

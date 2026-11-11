package despacho.strategy;

import despacho.modelo.Incidente;
import despacho.modelo.Ubicacion;
import despacho.modelo.Unidad;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Para emergencias leves: se reparte el trabajo (menos servicios atendidos), desempata la distancia. */
public class AsignacionMenorCarga implements EstrategiaAsignacion {

    @Override
    public Optional<Unidad> seleccionar(List<Unidad> candidatas, Incidente incidente) {
        Ubicacion destino = incidente.getReporte().ubicacion();
        return candidatas.stream()
                .min(Comparator.comparingInt(Unidad::getServiciosAtendidos)
                        .thenComparingDouble(u -> u.getUbicacion().distanciaA(destino)));
    }

    @Override
    public String nombre() { return "MENOR_CARGA"; }
}

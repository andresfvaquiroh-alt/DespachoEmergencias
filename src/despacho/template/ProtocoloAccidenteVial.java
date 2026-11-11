package despacho.template;

import despacho.modelo.ReporteEmergencia;
import despacho.modelo.TipoUnidad;

import java.util.ArrayList;
import java.util.List;

public class ProtocoloAccidenteVial extends ProtocoloAtencion {
    @Override protected String nombre() { return "ACCIDENTE_VIAL"; }

    @Override
    protected List<TipoUnidad> seleccionarUnidades(ReporteEmergencia r) {
        List<TipoUnidad> unidades = new ArrayList<>();
        unidades.add(TipoUnidad.PATRULLA);
        if (r.personasAfectadas() > 0) unidades.add(TipoUnidad.AMBULANCIA);
        return unidades;
    }

    @Override
    protected String instruccionCiudadano(ReporteEmergencia r) {
        return "señalice la vía y no mueva a los heridos";
    }
}

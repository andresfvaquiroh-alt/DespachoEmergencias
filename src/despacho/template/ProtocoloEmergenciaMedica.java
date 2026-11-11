package despacho.template;

import despacho.modelo.ReporteEmergencia;
import despacho.modelo.TipoUnidad;

import java.util.ArrayList;
import java.util.List;

public class ProtocoloEmergenciaMedica extends ProtocoloAtencion {
    @Override protected String nombre() { return "EMERGENCIA_MEDICA"; }

    @Override
    protected List<TipoUnidad> seleccionarUnidades(ReporteEmergencia r) {
        List<TipoUnidad> unidades = new ArrayList<>();
        unidades.add(TipoUnidad.AMBULANCIA);
        if (r.personasAfectadas() > 1) unidades.add(TipoUnidad.AMBULANCIA);
        return unidades;
    }

    @Override
    protected String instruccionCiudadano(ReporteEmergencia r) {
        return "mantenga al paciente tranquilo y siga las indicaciones del operador";
    }
}

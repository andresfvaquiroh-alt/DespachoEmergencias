package despacho.template;

import despacho.modelo.ReporteEmergencia;
import despacho.modelo.TipoUnidad;

import java.util.ArrayList;
import java.util.List;

public class ProtocoloIncendio extends ProtocoloAtencion {
    @Override protected String nombre() { return "INCENDIO"; }

    @Override
    protected List<TipoUnidad> seleccionarUnidades(ReporteEmergencia r) {
        List<TipoUnidad> unidades = new ArrayList<>();
        unidades.add(TipoUnidad.BOMBEROS);
        if (r.gravedad() >= 4) unidades.add(TipoUnidad.BOMBEROS);
        if (r.personasAfectadas() > 0) unidades.add(TipoUnidad.AMBULANCIA);
        return unidades;
    }

    @Override
    protected String instruccionCiudadano(ReporteEmergencia r) {
        return "evacue el lugar y no use ascensores";
    }

    @Override
    protected boolean requiereAvisoExterno(ReporteEmergencia r) { return r.gravedad() >= 4; }

    @Override
    protected void avisarEntidadExterna(ReporteEmergencia r) {
        System.out.println("  [PROTOCOLO INCENDIO] aviso a la empresa de energía y gas para cortar el servicio");
    }
}

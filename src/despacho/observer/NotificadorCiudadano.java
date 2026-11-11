package despacho.observer;

import despacho.modelo.Incidente;

/** Envía un SMS (simulado) al ciudadano cada vez que cambia el estado de su reporte. */
public class NotificadorCiudadano implements ObservadorIncidente {
    @Override
    public void actualizar(Incidente incidente, String evento) {
        if (!evento.startsWith("ESTADO")) {
            return;
        }
        System.out.println("  [SMS -> " + incidente.getReporte().telefono() + "] Su reporte #"
                + incidente.getId() + " ahora está: " + incidente.getEstado().nombre());
    }
}

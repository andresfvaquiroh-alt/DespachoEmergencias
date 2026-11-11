package despacho.mediator;

import despacho.modelo.Incidente;
import despacho.modelo.Unidad;

public interface Mediador {
    void notificar(Unidad emisor, EventoDespacho evento, Incidente incidente);
}

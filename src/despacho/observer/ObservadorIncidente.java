package despacho.observer;

import despacho.modelo.Incidente;

public interface ObservadorIncidente {
    void actualizar(Incidente incidente, String evento);
}

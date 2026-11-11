package despacho.observer;

import despacho.modelo.Incidente;

/** Pantalla del centro de control: muestra todo lo que le pasa a un incidente. */
public class PanelMonitor implements ObservadorIncidente {
    @Override
    public void actualizar(Incidente incidente, String evento) {
        System.out.println("  [PANEL] #" + incidente.getId() + " "
                + incidente.getReporte().tipo() + " | " + evento);
    }
}

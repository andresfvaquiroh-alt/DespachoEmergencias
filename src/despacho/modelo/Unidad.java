package despacho.modelo;

import despacho.mediator.EventoDespacho;
import despacho.mediator.Mediador;

/**
 * Colega del patrón MEDIATOR: no conoce a las demás unidades,
 * solo se comunica con el Mediador (CentroDespacho).
 */
public class Unidad {
    private final String id;
    private final TipoUnidad tipo;
    private Ubicacion ubicacion;
    private boolean disponible = true;
    private int serviciosAtendidos = 0;
    private Incidente incidenteActual;
    private final Mediador mediador;

    public Unidad(String id, TipoUnidad tipo, Ubicacion ubicacion, Mediador mediador) {
        this.id = id;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.mediador = mediador;
    }

    public void asignar(Incidente incidente) {
        this.disponible = false;
        this.incidenteActual = incidente;
    }

    public void liberar() {
        this.disponible = true;
        this.incidenteActual = null;
    }

    public void llegarAlSitio() {
        Incidente incidente = incidenteActual;
        this.ubicacion = incidente.getReporte().ubicacion();
        System.out.println("  [" + id + "] llegó al sitio del incidente #" + incidente.getId());
        mediador.notificar(this, EventoDespacho.UNIDAD_LLEGO, incidente);
    }

    public void finalizarServicio() {
        Incidente incidente = incidenteActual;
        this.serviciosAtendidos++;
        System.out.println("  [" + id + "] finalizó su servicio en el incidente #" + incidente.getId());
        mediador.notificar(this, EventoDespacho.SERVICIO_FINALIZADO, incidente);
    }

    public String getId() { return id; }
    public TipoUnidad getTipo() { return tipo; }
    public Ubicacion getUbicacion() { return ubicacion; }
    public boolean isDisponible() { return disponible; }
    public int getServiciosAtendidos() { return serviciosAtendidos; }
    public Incidente getIncidenteActual() { return incidenteActual; }

    @Override
    public String toString() {
        return id + " (" + tipo + ")";
    }
}

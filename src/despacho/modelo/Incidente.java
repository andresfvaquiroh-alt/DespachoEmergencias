package despacho.modelo;

import despacho.memento.IncidenteMemento;
import despacho.observer.ObservadorIncidente;
import despacho.state.EstadoIncidente;
import despacho.state.EstadoReportado;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase central del sistema. Cumple tres roles a la vez:
 *  - CONTEXT del patrón STATE (delega las acciones en su estado actual)
 *  - SUBJECT del patrón OBSERVER (notifica a sus observadores)
 *  - ORIGINATOR del patrón MEMENTO (crea y restaura sus instantáneas)
 */
public class Incidente {
    private static int contador = 0;

    private final int id;
    private final ReporteEmergencia reporte;
    private final String autorizadoPor;
    private EstadoIncidente estado;
    private List<Unidad> unidades = new ArrayList<>();
    private final List<ObservadorIncidente> observadores = new ArrayList<>();

    public Incidente(ReporteEmergencia reporte, String autorizadoPor) {
        this.id = ++contador;
        this.reporte = reporte;
        this.autorizadoPor = autorizadoPor;
        this.estado = new EstadoReportado();
    }

    // ---------- OBSERVER (Subject) ----------
    public void suscribir(ObservadorIncidente o) { observadores.add(o); }

    public void desuscribir(ObservadorIncidente o) { observadores.remove(o); }

    public void notificarObservadores(String evento) {
        for (ObservadorIncidente o : observadores) {
            o.actualizar(this, evento);
        }
    }

    // ---------- STATE (Context) ----------
    public void despachar() { estado.despachar(this); }

    public void iniciarAtencion() { estado.iniciarAtencion(this); }

    public void resolver() { estado.resolver(this); }

    public void cancelar() { estado.cancelar(this); }

    public void cambiarEstado(EstadoIncidente nuevo) {
        String anterior = estado.nombre();
        this.estado = nuevo;
        notificarObservadores("ESTADO: " + anterior + " -> " + nuevo.nombre());
    }

    // ---------- MEMENTO (Originator) ----------
    public IncidenteMemento crearMemento() {
        return new IncidenteMemento(estado, unidades);
    }

    public void restaurar(IncidenteMemento memento) {
        this.estado = memento.getEstado();
        this.unidades = new ArrayList<>(memento.getUnidades());
        notificarObservadores("RESTAURADO: vuelve a " + estado.nombre());
    }

    // ---------- Datos ----------
    public void agregarUnidad(Unidad u) { unidades.add(u); }

    public int getId() { return id; }
    public ReporteEmergencia getReporte() { return reporte; }
    public String getAutorizadoPor() { return autorizadoPor; }
    public EstadoIncidente getEstado() { return estado; }
    public List<Unidad> getUnidades() { return unidades; }
}

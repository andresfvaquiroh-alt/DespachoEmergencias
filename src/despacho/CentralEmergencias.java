package despacho;

import despacho.chain.Autorizacion;
import despacho.chain.DirectorOperaciones;
import despacho.chain.ManejadorAutorizacion;
import despacho.chain.OperadorLinea;
import despacho.chain.SupervisorTurno;
import despacho.command.ComandoCancelarIncidente;
import despacho.command.ComandoDespacharIncidente;
import despacho.command.GestorComandos;
import despacho.mediator.CentroDespacho;
import despacho.memento.CuidadorMementos;
import despacho.modelo.Incidente;
import despacho.modelo.ReporteEmergencia;
import despacho.modelo.TipoUnidad;
import despacho.modelo.Ubicacion;
import despacho.modelo.Unidad;
import despacho.observer.NotificadorCiudadano;
import despacho.observer.ObservadorIncidente;
import despacho.observer.PanelMonitor;
import despacho.observer.RegistroAuditoria;
import despacho.strategy.AsignacionMasCercana;
import despacho.strategy.AsignacionMenorCarga;
import despacho.strategy.EstrategiaAsignacion;
import despacho.template.ProtocoloAtencion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pegamento del sistema: conecta la salida de cada patrón con la entrada del siguiente.
 *
 *  Reporte -> CHAIN (¿quién autoriza?) -> TEMPLATE (¿qué unidades hacen falta?)
 *          -> STRATEGY (¿cómo se elige cada unidad?) -> COMMAND (despachar, con MEMENTO para el undo)
 *          -> STATE (REPORTADO -> DESPACHADO -> EN_ATENCION -> RESUELTO)
 *          -> MEDIATOR (las unidades avisan al centro) -> OBSERVER (panel, auditoría y SMS)
 */
public class CentralEmergencias {
    private final CentroDespacho centro = new CentroDespacho();                 // Mediator
    private final GestorComandos gestor = new GestorComandos();                 // Invoker (Command)
    private final CuidadorMementos cuidador = new CuidadorMementos();           // Caretaker (Memento)
    private final ManejadorAutorizacion cadena;                                 // Chain of Responsibility
    private final RegistroAuditoria auditoria = new RegistroAuditoria();
    private final List<ObservadorIncidente> observadores;                       // Observer
    private final EstrategiaAsignacion masCercana = new AsignacionMasCercana(); // Strategy
    private final EstrategiaAsignacion menorCarga = new AsignacionMenorCarga(); // Strategy
    private final Map<Integer, List<TipoUnidad>> planes = new HashMap<>();

    public CentralEmergencias() {
        centro.registrarUnidad(new Unidad("B-1", TipoUnidad.BOMBEROS, new Ubicacion("Estación Norte", 1, 1), centro));
        centro.registrarUnidad(new Unidad("B-2", TipoUnidad.BOMBEROS, new Ubicacion("Estación Sur", 8, 8), centro));
        centro.registrarUnidad(new Unidad("A-1", TipoUnidad.AMBULANCIA, new Ubicacion("Hospital Central", 2, 5), centro));
        centro.registrarUnidad(new Unidad("A-2", TipoUnidad.AMBULANCIA, new Ubicacion("Clínica Este", 9, 2), centro));
        centro.registrarUnidad(new Unidad("P-1", TipoUnidad.PATRULLA, new Ubicacion("CAI Centro", 4, 4), centro));
        centro.registrarUnidad(new Unidad("P-2", TipoUnidad.PATRULLA, new Ubicacion("CAI Oeste", 7, 7), centro));

        ManejadorAutorizacion operador = new OperadorLinea();
        operador.setSiguiente(new SupervisorTurno()).setSiguiente(new DirectorOperaciones());
        this.cadena = operador;

        this.observadores = List.of(new PanelMonitor(), auditoria, new NotificadorCiudadano());
    }

    /** Paso 1: recibe el reporte, lo autoriza (Chain) y arma el plan de unidades (Template). */
    public Incidente registrarReporte(ReporteEmergencia reporte) {
        System.out.println("  Reporte de " + reporte.ciudadano() + ": " + reporte.descripcion()
                + " (gravedad " + reporte.gravedad() + ", afectados " + reporte.personasAfectadas() + ")");

        Autorizacion autorizacion = cadena.manejar(reporte);

        Incidente incidente = new Incidente(reporte, autorizacion.cargo());
        observadores.forEach(incidente::suscribir);
        incidente.notificarObservadores("REPORTE RECIBIDO (autorizado por " + autorizacion.cargo() + ")");

        ProtocoloAtencion protocolo = ProtocoloAtencion.para(reporte.tipo());
        planes.put(incidente.getId(), protocolo.planificar(reporte));
        return incidente;
    }

    /** Paso 2: elige la estrategia según la gravedad y despacha mediante un Command. */
    public void despachar(Incidente incidente) {
        centro.setEstrategia(incidente.getReporte().gravedad() >= 4 ? masCercana : menorCarga);
        System.out.println("  [ESTRATEGIA] Se usará " + centro.getEstrategia().nombre()
                + " (gravedad " + incidente.getReporte().gravedad() + ")");
        gestor.ejecutar(new ComandoDespacharIncidente(centro, incidente, planes.get(incidente.getId()), cuidador));
    }

    public void cancelar(Incidente incidente) {
        gestor.ejecutar(new ComandoCancelarIncidente(centro, incidente, cuidador));
    }

    public void deshacerUltimo() {
        gestor.deshacerUltimo();
    }

    public void imprimirResumen() {
        System.out.println("  --- Historial de comandos ---");
        gestor.imprimirBitacora();
        System.out.println("  --- Bitácora de auditoría (Observer) ---");
        auditoria.imprimir();
        System.out.println("  --- Servicios atendidos por unidad ---");
        for (Unidad u : centro.getUnidades()) {
            System.out.println("  " + u + " -> " + u.getServiciosAtendidos() + " servicio(s)");
        }
    }
}

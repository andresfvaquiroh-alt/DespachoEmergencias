package despacho.chain;

import despacho.modelo.ReporteEmergencia;

/** CHAIN OF RESPONSIBILITY: cada eslabón autoriza el reporte o lo escala al siguiente nivel. */
public abstract class ManejadorAutorizacion {
    private ManejadorAutorizacion siguiente;

    public ManejadorAutorizacion setSiguiente(ManejadorAutorizacion siguiente) {
        this.siguiente = siguiente;
        return siguiente;                       // permite encadenar: a.setSiguiente(b).setSiguiente(c)
    }

    public final Autorizacion manejar(ReporteEmergencia reporte) {
        if (puedeAutorizar(reporte)) {
            System.out.println("  [CADENA] " + cargo() + " autoriza el reporte");
            return new Autorizacion(cargo());
        }
        if (siguiente == null) {
            throw new IllegalStateException("Ningún nivel puede autorizar este reporte");
        }
        System.out.println("  [CADENA] " + cargo() + " no puede autorizarlo -> escala a " + siguiente.cargo());
        return siguiente.manejar(reporte);
    }

    protected abstract boolean puedeAutorizar(ReporteEmergencia reporte);

    protected abstract String cargo();
}

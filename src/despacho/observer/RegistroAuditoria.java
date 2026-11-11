package despacho.observer;

import despacho.modelo.Incidente;

import java.util.ArrayList;
import java.util.List;

/** Guarda una bitácora permanente de todos los eventos. */
public class RegistroAuditoria implements ObservadorIncidente {
    private final List<String> registros = new ArrayList<>();

    @Override
    public void actualizar(Incidente incidente, String evento) {
        registros.add(String.format("%02d | Incidente #%d | %s", registros.size() + 1, incidente.getId(), evento));
    }

    public void imprimir() {
        registros.forEach(r -> System.out.println("  " + r));
    }
}

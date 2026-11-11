package despacho.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/** CARETAKER: guarda las instantáneas por incidente sin conocer su contenido. */
public class CuidadorMementos {
    private final Map<Integer, Deque<IncidenteMemento>> historial = new HashMap<>();

    public void guardar(int idIncidente, IncidenteMemento memento) {
        historial.computeIfAbsent(idIncidente, k -> new ArrayDeque<>()).push(memento);
    }

    public IncidenteMemento recuperarUltimo(int idIncidente) {
        Deque<IncidenteMemento> pila = historial.get(idIncidente);
        if (pila == null || pila.isEmpty()) {
            throw new IllegalStateException("No hay instantáneas guardadas para el incidente #" + idIncidente);
        }
        return pila.pop();
    }
}

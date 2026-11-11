package despacho.command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** INVOKER: ejecuta comandos y mantiene el historial para poder deshacer. */
public class GestorComandos {
    private final Deque<Comando> historial = new ArrayDeque<>();
    private final List<String> bitacora = new ArrayList<>();

    public void ejecutar(Comando comando) {
        System.out.println("  [COMANDO] Ejecutando: " + comando.descripcion());
        comando.ejecutar();
        historial.push(comando);
        bitacora.add("EJECUTADO: " + comando.descripcion());
    }

    public void deshacerUltimo() {
        if (historial.isEmpty()) {
            System.out.println("  [COMANDO] No hay nada que deshacer");
            return;
        }
        Comando comando = historial.pop();
        System.out.println("  [COMANDO] Deshaciendo: " + comando.descripcion());
        comando.deshacer();
        bitacora.add("DESHECHO:  " + comando.descripcion());
    }

    public void imprimirBitacora() {
        bitacora.forEach(b -> System.out.println("  " + b));
    }
}

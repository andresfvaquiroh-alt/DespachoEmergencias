package despacho.modelo;

public record Ubicacion(String nombre, double x, double y) {

    public double distanciaA(Ubicacion otra) {
        return Math.hypot(x - otra.x, y - otra.y);
    }
}

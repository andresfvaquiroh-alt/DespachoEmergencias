package despacho.command;

public interface Comando {
    void ejecutar();

    void deshacer();

    String descripcion();
}

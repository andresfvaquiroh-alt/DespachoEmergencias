package despacho.modelo;

public record ReporteEmergencia(String ciudadano,
                                String telefono,
                                TipoIncidente tipo,
                                Ubicacion ubicacion,
                                String descripcion,
                                int gravedad,
                                int personasAfectadas) {
}

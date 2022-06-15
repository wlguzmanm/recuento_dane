package co.gov.dane.recuento.dtos;

import java.util.Date;

public class BackupAutoDTO {

    private String id;
    private String nombre;
    private String fechaCreacion;
    private String formato;
    private String usuario;
    private String imei;
    private String ruta;
    private String nombreArchivo;

    public BackupAutoDTO() {
    }

    public BackupAutoDTO(String id, String nombre, String fechaCreacion,
                         String formato, String usuario, String imei,
                         String ruta, String nombreArchivo) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.formato = formato;
        this.usuario = usuario;
        this.imei = imei;
        this.ruta = ruta;
        this.nombreArchivo = nombreArchivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}

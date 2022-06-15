package co.gov.dane.recuento.dtos;

import java.util.Date;

public class LogEstadosDTO {

    private String id;
    private String idEncuesta;
    private String estado;
    private String fechaCreacion;
    private String usuerioId;
    private String imei;

    public LogEstadosDTO() {
    }

    public LogEstadosDTO(String id, String idEncuesta, String estado,
                         String fechaCreacion, String usuerioId, String imei) {
        this.id = id;
        this.idEncuesta = idEncuesta;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuerioId = usuerioId;
        this.imei = imei;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuerioId() {
        return usuerioId;
    }

    public void setUsuerioId(String usuerioId) {
        this.usuerioId = usuerioId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}

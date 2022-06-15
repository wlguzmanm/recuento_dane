package co.gov.dane.recuento.dtos;

import java.util.Date;

public class EncuestaDTO {

    private String id;
    private String idManzana;
    private String idEdificacion;
    private String idUnidad;
    private String uuidEncuesta;
    private String uuidManzana;
    private String uuidEdificacion;
    private String uuidUnidad;
    private String fechaCreacion;
    private String fechaModificacion;
    private String activo;
    private String latitud;
    private String longitud;
    private String imei;
    private String pendienteSincronizar;
    private String sincronizado;
    private String sincronizadoDoble;
    private String sincronizadoNube;
    private String sincronizacionPendiente;
    private String sincronizacionMensaje;
    private String sincronizacionEstatus;
    private String encuestador;
    private String supervisor;
    private String finalizado;
    private String tipoEncuesta;
    private String fechaSincronizacion;
    private String usuarioSincronizacion;

    public EncuestaDTO() {
    }

    public EncuestaDTO(String id, String idManzana, String idEdificacion, String idUnidad, String uuidEncuesta,
                       String uuidManzana, String uuidEdificacion, String uuidUnidad, String fechaCreacion,
                       String fechaModificacion, String activo, String latitud, String longitud, String imei,
                       String pendienteSincronizar, String sincronizado, String sincronizadoDoble,
                       String sincronizadoNube, String sincronizacionPendiente, String sincronizacionMensaje,
                       String sincronizacionEstatus, String encuestador, String supervisor, String finalizado,
                       String tipoEncuesta, String fechaSincronizacion, String usuarioSincronizacion) {
        this.id = id;
        this.idManzana = idManzana;
        this.idEdificacion = idEdificacion;
        this.idUnidad = idUnidad;
        this.uuidEncuesta = uuidEncuesta;
        this.uuidManzana = uuidManzana;
        this.uuidEdificacion = uuidEdificacion;
        this.uuidUnidad = uuidUnidad;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.activo = activo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imei = imei;
        this.pendienteSincronizar = pendienteSincronizar;
        this.sincronizado = sincronizado;
        this.sincronizadoDoble = sincronizadoDoble;
        this.sincronizadoNube = sincronizadoNube;
        this.sincronizacionPendiente = sincronizacionPendiente;
        this.sincronizacionMensaje = sincronizacionMensaje;
        this.sincronizacionEstatus = sincronizacionEstatus;
        this.encuestador = encuestador;
        this.supervisor = supervisor;
        this.finalizado = finalizado;
        this.tipoEncuesta = tipoEncuesta;
        this.fechaSincronizacion = fechaSincronizacion;
        this.usuarioSincronizacion = usuarioSincronizacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdManzana() {
        return idManzana;
    }

    public void setIdManzana(String idManzana) {
        this.idManzana = idManzana;
    }

    public String getIdEdificacion() {
        return idEdificacion;
    }

    public void setIdEdificacion(String idEdificacion) {
        this.idEdificacion = idEdificacion;
    }

    public String getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(String idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getUuidEncuesta() {
        return uuidEncuesta;
    }

    public void setUuidEncuesta(String uuidEncuesta) {
        this.uuidEncuesta = uuidEncuesta;
    }

    public String getUuidManzana() {
        return uuidManzana;
    }

    public void setUuidManzana(String uuidManzana) {
        this.uuidManzana = uuidManzana;
    }

    public String getUuidEdificacion() {
        return uuidEdificacion;
    }

    public void setUuidEdificacion(String uuidEdificacion) {
        this.uuidEdificacion = uuidEdificacion;
    }

    public String getUuidUnidad() {
        return uuidUnidad;
    }

    public void setUuidUnidad(String uuidUnidad) {
        this.uuidUnidad = uuidUnidad;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPendienteSincronizar() {
        return pendienteSincronizar;
    }

    public void setPendienteSincronizar(String pendienteSincronizar) {
        this.pendienteSincronizar = pendienteSincronizar;
    }

    public String getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(String sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getSincronizadoDoble() {
        return sincronizadoDoble;
    }

    public void setSincronizadoDoble(String sincronizadoDoble) {
        this.sincronizadoDoble = sincronizadoDoble;
    }

    public String getSincronizadoNube() {
        return sincronizadoNube;
    }

    public void setSincronizadoNube(String sincronizadoNube) {
        this.sincronizadoNube = sincronizadoNube;
    }

    public String getSincronizacionPendiente() {
        return sincronizacionPendiente;
    }

    public void setSincronizacionPendiente(String sincronizacionPendiente) {
        this.sincronizacionPendiente = sincronizacionPendiente;
    }

    public String getSincronizacionMensaje() {
        return sincronizacionMensaje;
    }

    public void setSincronizacionMensaje(String sincronizacionMensaje) {
        this.sincronizacionMensaje = sincronizacionMensaje;
    }

    public String getSincronizacionEstatus() {
        return sincronizacionEstatus;
    }

    public void setSincronizacionEstatus(String sincronizacionEstatus) {
        this.sincronizacionEstatus = sincronizacionEstatus;
    }

    public String getEncuestador() {
        return encuestador;
    }

    public void setEncuestador(String encuestador) {
        this.encuestador = encuestador;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(String finalizado) {
        this.finalizado = finalizado;
    }

    public String getTipoEncuesta() {
        return tipoEncuesta;
    }

    public void setTipoEncuesta(String tipoEncuesta) {
        this.tipoEncuesta = tipoEncuesta;
    }

    public String getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(String fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public String getUsuarioSincronizacion() {
        return usuarioSincronizacion;
    }

    public void setUsuarioSincronizacion(String usuarioSincronizacion) {
        this.usuarioSincronizacion = usuarioSincronizacion;
    }
}

package co.gov.dane.recuento.model;

import java.util.List;

public class EsquemaEdificacionEnvioViewModel {

    private String id_manzana;
    private String id_edificacion;
    private String latitud;
    private String longitud;
    private String fechaModificacion;
    private String estadoSincronizacion;
    private String id_manzana_edificacion;
    private List<EsquemaUnidadesEnvioViewModel> unidades;

    public EsquemaEdificacionEnvioViewModel() {
    }

    public String getId_manzana() {
        return id_manzana;
    }

    public void setId_manzana(String id_manzana) {
        this.id_manzana = id_manzana;
    }

    public String getId_edificacion() {
        return id_edificacion;
    }

    public void setId_edificacion(String id_edificacion) {
        this.id_edificacion = id_edificacion;
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

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getEstadoSincronizacion() {
        return estadoSincronizacion;
    }

    public void setEstadoSincronizacion(String estadoSincronizacion) {
        this.estadoSincronizacion = estadoSincronizacion;
    }

    public String getId_manzana_edificacion() {
        return id_manzana_edificacion;
    }

    public void setId_manzana_edificacion(String id_manzana_edificacion) {
        this.id_manzana_edificacion = id_manzana_edificacion;
    }

    public List<EsquemaUnidadesEnvioViewModel> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<EsquemaUnidadesEnvioViewModel> unidades) {
        this.unidades = unidades;
    }
}

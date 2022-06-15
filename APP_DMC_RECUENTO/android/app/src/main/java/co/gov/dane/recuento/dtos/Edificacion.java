package co.gov.dane.recuento.dtos;

import java.util.List;

public class Edificacion {

    private String id;
    private String id_manzana;
    private String uuid_manzana;
    private String id_edificacion;
    private String uuid_edificacion;
    private String latitud,longitud;
    private String check_base_datos, imei, fechaModificacion;

    private List<UnidadEconomica> unidades;
    public Edificacion(){

    }
    public Edificacion(String id_edificacion){
        this.id_edificacion=id_edificacion;
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

    public List<UnidadEconomica> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadEconomica> unidades) {
        this.unidades = unidades;
    }

    public String getCheck_base_datos() {
        return check_base_datos;
    }

    public void setCheck_base_datos(String check_base_datos) {
        this.check_base_datos = check_base_datos;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUuid_manzana() {
        return uuid_manzana;
    }

    public void setUuid_manzana(String uuid_manzana) {
        this.uuid_manzana = uuid_manzana;
    }

    public String getUuid_edificacion() {
        return uuid_edificacion;
    }

    public void setUuid_edificacion(String uuid_edificacion) {
        this.uuid_edificacion = uuid_edificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
package co.gov.dane.recuento.dtos;


public class UnidadEconomica {


    private String imei, tipoEncuesta;
    private String id_manzana,id_edificacion,id_unidad,fechaModificacion, uuidManzana,uuidEdificacion,uuidUnidad;

    public UnidadEconomica(){

    }

    public UnidadEconomica(String id_unidad){
        this.id_unidad=id_unidad;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getTipoEncuesta() {
        return tipoEncuesta;
    }

    public void setTipoEncuesta(String tipoEncuesta) {
        this.tipoEncuesta = tipoEncuesta;
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

    public String getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
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
}

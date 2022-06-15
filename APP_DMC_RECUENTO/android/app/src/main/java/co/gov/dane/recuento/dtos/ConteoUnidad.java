package co.gov.dane.recuento.dtos;


public class ConteoUnidad {
    private String id_unidad;
    private String uuid_unidad;
    private String nombre;
    private String fecha;
    private String id_edificacion;
    private String uuid_edificacion;
    private String id_manzana;
    private String uuid_manzana;
    private String tipoEncuesta;

    public ConteoUnidad() {
    }

    public ConteoUnidad(String id_manzana, String id_edificacion, String id_unidad, String nombre, String fecha,
                        String uuid_manzana, String uuid_edificacion, String uuid_unidad, String tipoEncuesta) {
        this.id_manzana=id_manzana;
        this.id_edificacion=id_edificacion;
        this.id_unidad = id_unidad;
        this.nombre = nombre;
        this.fecha = fecha;
        this.uuid_manzana = uuid_manzana;
        this.uuid_edificacion = uuid_edificacion;
        this.uuid_unidad = uuid_unidad;
        this.tipoEncuesta = tipoEncuesta;
    }

    public String getId_unidad() {
        return id_unidad;
    }

    public String getId_edificacion() {
        return id_edificacion;
    }


    public String getId_manzana() {
        return id_manzana;
    }

    public String getFecha() {
        return "Modificado: "+fecha;
    }


    public String getNombre() {
        return "Unidad: "+nombre;
    }

    public String getUuid_unidad() {
        return uuid_unidad;
    }

    public void setUuid_unidad(String uuid_unidad) {
        this.uuid_unidad = uuid_unidad;
    }

    public String getUuid_edificacion() {
        return uuid_edificacion;
    }

    public void setUuid_edificacion(String uuid_edificacion) {
        this.uuid_edificacion = uuid_edificacion;
    }

    public String getUuid_manzana() {
        return uuid_manzana;
    }

    public void setUuid_manzana(String uuid_manzana) {
        this.uuid_manzana = uuid_manzana;
    }

    public String getTipoEncuesta() {
        return tipoEncuesta;
    }

    public void setTipoEncuesta(String tipoEncuesta) {
        this.tipoEncuesta = tipoEncuesta;
    }
}
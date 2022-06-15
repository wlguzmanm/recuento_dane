package co.gov.dane.recuento.dtos;


public class ConteoEdificacion {

    private String id_edificacion;
    private String uuid_edificacion;
    private long id;
    private String nombre;
    private String fecha;
    private String id_manzana;
    private String uuid_manzana;

    public ConteoEdificacion() {
    }

    public ConteoEdificacion(String id_manzana, String id_edificacion, String nombre, String fecha, long id, String uuid_edificacion, String uuid_manzana) {
        this.id_edificacion = id_edificacion;
        this.nombre = nombre;
        this.fecha = fecha;
        this.id_manzana = id_manzana;
        this.id = id;
        this.uuid_edificacion = uuid_edificacion;
        this.uuid_manzana = uuid_manzana;
    }

    public String getId_edificacion() {
        return id_edificacion;
    }


    public String getFecha() {
        return "Modificado: " + fecha;
    }


    public String getNombre() {
        return "Edificacion: " + nombre;
    }

    public String getId_manzana() {
        return id_manzana;
    }

    public void setId_manzana(String id_manzana) {
        this.id_manzana = id_manzana;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}

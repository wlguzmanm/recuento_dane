package co.gov.dane.reconteo.model;

public class ConteoEdificacion {

    private String id_edificacion;
    private long id;
    private String nombre;
    private String fecha;
    private String id_manzana;

    public ConteoEdificacion() {
    }

    public ConteoEdificacion(String id_manzana, String id_edificacion, String nombre, String fecha, long id ) {
        this.id_edificacion = id_edificacion;
        this.nombre = nombre;
        this.fecha = fecha;
        this.id_manzana = id_manzana;
        this.id = id;
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
}

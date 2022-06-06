package co.gov.dane.reconteo.model;

public class ConteoUnidad {
    private String id_unidad;
    private String nombre;
    private String fecha;
    private String id_edificacion;
    private String id_manzana;

    public ConteoUnidad() {
    }

    public ConteoUnidad(String id_manzana, String id_edificacion, String id_unidad, String nombre, String fecha) {
        this.id_manzana=id_manzana;
        this.id_edificacion=id_edificacion;
        this.id_unidad = id_unidad;
        this.nombre = nombre;
        this.fecha = fecha;
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




}

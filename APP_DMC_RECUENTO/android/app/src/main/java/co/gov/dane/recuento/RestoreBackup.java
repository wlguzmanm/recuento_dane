package co.gov.dane.recuento;

public class RestoreBackup {

    private int id;
    private String nombre;
    private String ruta;
    private boolean activo;

    public RestoreBackup() {
    }

    /**
     * Constructor
     *
     * @param id
     * @param nombre
     * @param ruta
     * @param activo
     */
    public RestoreBackup(int id, String nombre, String ruta, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
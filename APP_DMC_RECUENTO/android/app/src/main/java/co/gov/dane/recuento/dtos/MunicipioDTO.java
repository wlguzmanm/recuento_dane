package co.gov.dane.recuento.dtos;

public class MunicipioDTO {
    private String id;
    private String nombre;
    private String codigo;

    /*Contructor*/
    public MunicipioDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}

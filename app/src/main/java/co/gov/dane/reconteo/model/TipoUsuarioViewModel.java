package co.gov.dane.reconteo.model;

public class TipoUsuarioViewModel {

    private String idTipoUsuario;
    private String nombreTipo;
    private String desTipo;
    private int estadoTipo;

    public String getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(String idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public String getDesTipo() {
        return desTipo;
    }

    public void setDesTipo(String desTipo) {
        this.desTipo = desTipo;
    }

    public int getEstadoTipo() {
        return estadoTipo;
    }

    public void setEstadoTipo(int estadoTipo) {
        this.estadoTipo = estadoTipo;
    }
}

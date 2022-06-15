package co.gov.dane.recuento.model;

public class ResponseToken {
    private String token;
    private UserTokenViewModel usuario;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserTokenViewModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UserTokenViewModel usuario) {
        this.usuario = usuario;
    }
}

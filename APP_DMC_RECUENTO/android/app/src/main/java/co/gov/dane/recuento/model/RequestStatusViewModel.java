package co.gov.dane.recuento.model;

public class RequestStatusViewModel {
    private String status;
    private String message;
    private Boolean validar;
    private String imei;
    private String usuario;
    private String fkManzana;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFkManzana() {
        return fkManzana;
    }

    public void setFkManzana(String fkManzana) {
        this.fkManzana = fkManzana;
    }
}

package co.gov.dane.recuento.model;

public class ResponseEnvioManzanaViewModel {
    private String status;
    private String message;
    private Boolean validar;

    public ResponseEnvioManzanaViewModel() {
    }

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
}

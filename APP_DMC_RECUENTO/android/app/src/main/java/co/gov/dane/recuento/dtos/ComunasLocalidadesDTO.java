package co.gov.dane.recuento.dtos;

public class ComunasLocalidadesDTO {
    private String id;
    private String getCodDpto;
    private String getNomDpto;
    private String getCodMpio;
    private String getNomMpio;
    private String getCodCpob;
    private String getCodLocalidad;
    private String getNomLocalidad;
    private String getTipoLocComCorr;


    /*Constuctor*/
    public ComunasLocalidadesDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGetCodDpto() {
        return getCodDpto;
    }

    public void setGetCodDpto(String getCodDpto) {
        this.getCodDpto = getCodDpto;
    }

    public String getGetNomDpto() {
        return getNomDpto;
    }

    public void setGetNomDpto(String getNomDpto) {
        this.getNomDpto = getNomDpto;
    }

    public String getGetCodMpio() {
        return getCodMpio;
    }

    public void setGetCodMpio(String getCodMpio) {
        this.getCodMpio = getCodMpio;
    }

    public String getGetNomMpio() {
        return getNomMpio;
    }

    public void setGetNomMpio(String getNomMpio) {
        this.getNomMpio = getNomMpio;
    }

    public String getGetCodCpob() {
        return getCodCpob;
    }

    public void setGetCodCpob(String getCodCpob) {
        this.getCodCpob = getCodCpob;
    }

    public String getGetCodLocalidad() {
        return getCodLocalidad;
    }

    public void setGetCodLocalidad(String getCodLocalidad) {
        this.getCodLocalidad = getCodLocalidad;
    }

    public String getGetNomLocalidad() {
        return getNomLocalidad;
    }

    public void setGetNomLocalidad(String getNomLocalidad) {
        this.getNomLocalidad = getNomLocalidad;
    }

    public String getGetTipoLocComCorr() {
        return getTipoLocComCorr;
    }

    public void setGetTipoLocComCorr(String getTipoLocComCorr) {
        this.getTipoLocComCorr = getTipoLocComCorr;
    }
}

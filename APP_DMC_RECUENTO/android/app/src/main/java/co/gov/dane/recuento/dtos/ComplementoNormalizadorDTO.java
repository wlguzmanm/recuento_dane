package co.gov.dane.recuento.dtos;

public class ComplementoNormalizadorDTO {

    private String id;
    private String idNormalizador;
    private String direcPComp;
    private String direcComp;
    private String direcComplemento;

    public ComplementoNormalizadorDTO() {
    }

    public ComplementoNormalizadorDTO(String id, String idNormalizador, String direcPComp, String direcComp, String direcComplemento) {
        this.id = id;
        this.idNormalizador = idNormalizador;
        this.direcPComp = direcPComp;
        this.direcComp = direcComp;
        this.direcComplemento = direcComplemento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNormalizador() {
        return idNormalizador;
    }

    public void setIdNormalizador(String idNormalizador) {
        this.idNormalizador = idNormalizador;
    }

    public String getDirecPComp() {
        return direcPComp;
    }

    public void setDirecPComp(String direcPComp) {
        this.direcPComp = direcPComp;
    }

    public String getDirecComp() {
        return direcComp;
    }

    public void setDirecComp(String direcComp) {
        this.direcComp = direcComp;
    }

    public String getDirecComplemento() {
        return direcComplemento;
    }

    public void setDirecComplemento(String direcComplemento) {
        this.direcComplemento = direcComplemento;
    }
}

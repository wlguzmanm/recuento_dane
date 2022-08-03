package co.gov.dane.recuento.dtos;

import java.util.List;

public class NormalizadorDireccionDTO {

    private String id;
    private String idManzana;
    private String idEdificacion;
    private String idUnidadEconomica;
    private String direcVp;
    private String direcNnomVp;
    private String direcNumVp;
    private String direcLetVp;
    private String direcLetVpOtro;
    private String direcSfVp;
    private String direcLetSvp;
    private String direcLetSvpOtro;
    private String direcCuadVp;
    private String direcNumVg;
    private String direcLetVg;
    private String direcLetVgOtro;
    private String direcSfVg;
    private String direcLetSvg;
    private String direcLetSvgOtro;
    private String direcNumPlaca;
    private String direcCuadVg;
    private String direcPComp;
    private String imei;
    private String usuario;
    private String fechaCreacion;

    private List<ComplementoNormalizadorDTO> complememnto;

    public NormalizadorDireccionDTO() {
    }

    public NormalizadorDireccionDTO(String id, String idManzana, String idEdificacion, String idUnidadEconomica, String direcVp, String direcNnomVp, String direcNumVp,
                                    String direcLetVp,  String direcLetVpOtro, String direcSfVp, String direcLetSvp, String direcLetSvpOtro, String direcCuadVp,
                                    String direcNumVg, String direcLetVg, String direcLetVgOtro, String direcSfVg, String direcLetSvg, String direcLetSvgOtro,
                                    String direcNumPlaca, String direcCuadVg, String direcPComp, String imei, String usuario, String fechaCreacion) {
        this.id = id;
        this.idManzana = idManzana;
        this.idEdificacion = idEdificacion;
        this.idUnidadEconomica = idUnidadEconomica;
        this.direcVp = direcVp;
        this.direcNnomVp = direcNnomVp;
        this.direcNumVp = direcNumVp;
        this.direcLetVp = direcLetVp;
        this.direcLetVpOtro = direcLetVpOtro;
        this.direcSfVp = direcSfVp;
        this.direcLetSvp = direcLetSvp;
        this.direcLetSvpOtro = direcLetSvpOtro;
        this.direcCuadVp = direcCuadVp;
        this.direcNumVg = direcNumVg;
        this.direcLetVg = direcLetVg;
        this.direcLetVgOtro = direcLetVgOtro;
        this.direcSfVg = direcSfVg;
        this.direcLetSvg = direcLetSvg;
        this.direcLetSvgOtro = direcLetSvgOtro;
        this.direcNumPlaca = direcNumPlaca;
        this.direcCuadVg = direcCuadVg;
        this.direcPComp = direcPComp;
        this.imei = imei;
        this.usuario = usuario;
        this.fechaCreacion = fechaCreacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirecVp() {
        return direcVp;
    }

    public void setDirecVp(String direcVp) {
        this.direcVp = direcVp;
    }

    public String getDirecNnomVp() {
        return direcNnomVp;
    }

    public void setDirecNnomVp(String direcNnomVp) {
        this.direcNnomVp = direcNnomVp;
    }

    public String getDirecLetVp() {
        return direcLetVp;
    }

    public void setDirecLetVp(String direcLetVp) {
        this.direcLetVp = direcLetVp;
    }

    public String getDirecSfVp() {
        return direcSfVp;
    }

    public void setDirecSfVp(String direcSfVp) {
        this.direcSfVp = direcSfVp;
    }

    public String getDirecLetSvp() {
        return direcLetSvp;
    }

    public void setDirecLetSvp(String direcLetSvp) {
        this.direcLetSvp = direcLetSvp;
    }

    public String getDirecCuadVp() {
        return direcCuadVp;
    }

    public void setDirecCuadVp(String direcCuadVp) {
        this.direcCuadVp = direcCuadVp;
    }

    public String getDirecNumVg() {
        return direcNumVg;
    }

    public void setDirecNumVg(String direcNumVg) {
        this.direcNumVg = direcNumVg;
    }

    public String getDirecLetVg() {
        return direcLetVg;
    }

    public void setDirecLetVg(String direcLetVg) {
        this.direcLetVg = direcLetVg;
    }

    public String getDirecSfVg() {
        return direcSfVg;
    }

    public void setDirecSfVg(String direcSfVg) {
        this.direcSfVg = direcSfVg;
    }

    public String getDirecLetSvg() {
        return direcLetSvg;
    }

    public void setDirecLetSvg(String direcLetSvg) {
        this.direcLetSvg = direcLetSvg;
    }

    public String getDirecNumPlaca() {
        return direcNumPlaca;
    }

    public void setDirecNumPlaca(String direcNumPlaca) {
        this.direcNumPlaca = direcNumPlaca;
    }

    public String getDirecCuadVg() {
        return direcCuadVg;
    }

    public void setDirecCuadVg(String direcCuadVg) {
        this.direcCuadVg = direcCuadVg;
    }

    public String getDirecPComp() {
        return direcPComp;
    }

    public void setDirecPComp(String direcPComp) {
        this.direcPComp = direcPComp;
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDirecNumVp() {
        return direcNumVp;
    }

    public void setDirecNumVp(String direcNumVp) {
        this.direcNumVp = direcNumVp;
    }

    public String getIdManzana() {
        return idManzana;
    }

    public void setIdManzana(String idManzana) {
        this.idManzana = idManzana;
    }

    public String getIdUnidadEconomica() {
        return idUnidadEconomica;
    }

    public void setIdUnidadEconomica(String idUnidadEconomica) {
        this.idUnidadEconomica = idUnidadEconomica;
    }

    public String getDirecLetVpOtro() {
        return direcLetVpOtro;
    }

    public void setDirecLetVpOtro(String direcLetVpOtro) {
        this.direcLetVpOtro = direcLetVpOtro;
    }

    public String getDirecLetSvpOtro() {
        return direcLetSvpOtro;
    }

    public void setDirecLetSvpOtro(String direcLetSvpOtro) {
        this.direcLetSvpOtro = direcLetSvpOtro;
    }

    public String getDirecLetVgOtro() {
        return direcLetVgOtro;
    }

    public void setDirecLetVgOtro(String direcLetVgOtro) {
        this.direcLetVgOtro = direcLetVgOtro;
    }

    public String getDirecLetSvgOtro() {
        return direcLetSvgOtro;
    }

    public void setDirecLetSvgOtro(String direcLetSvgOtro) {
        this.direcLetSvgOtro = direcLetSvgOtro;
    }

    public String getIdEdificacion() {
        return idEdificacion;
    }

    public void setIdEdificacion(String idEdificacion) {
        this.idEdificacion = idEdificacion;
    }

    public List<ComplementoNormalizadorDTO> getComplememnto() {
        return complememnto;
    }

    public void setComplememnto(List<ComplementoNormalizadorDTO> complememnto) {
        this.complememnto = complememnto;
    }
}

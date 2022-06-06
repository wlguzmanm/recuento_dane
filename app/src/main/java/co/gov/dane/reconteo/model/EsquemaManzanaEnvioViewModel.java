package co.gov.dane.reconteo.model;

import java.util.List;

public class EsquemaManzanaEnvioViewModel {

    private String id_manzana;
    private String fechaConteo;
    private String departamento;
    private String municipio;
    private String clase;
    private String centro_poblado;
    private String sector_urbano;
    private String seccion_urbana;
    private String manzana;
    private String latitud;
    private String altura;
    private String longitud;
    private String precision;
    private String presencia_und;
    private String fechaModificacion;
    private String cod_enumerador;
    private String supervisor;
    private String finalizado;
    private String barrio;
    private String observaciones;
    private String codigoAG;
    private String novedadesCartograficas;
    private String imei;
    private Boolean validar;
    private List<EsquemaEdificacionEnvioViewModel> esquemaEdificacion;

    public EsquemaManzanaEnvioViewModel() {
    }

    public String getId_manzana() {
        return id_manzana;
    }

    public void setId_manzana(String id_manzana) {
        this.id_manzana = id_manzana;
    }

    public String getFechaConteo() {
        return fechaConteo;
    }

    public void setFechaConteo(String fechaConteo) {
        this.fechaConteo = fechaConteo;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getCentro_poblado() {
        return centro_poblado;
    }

    public void setCentro_poblado(String centro_poblado) {
        this.centro_poblado = centro_poblado;
    }

    public String getSector_urbano() {
        return sector_urbano;
    }

    public void setSector_urbano(String sector_urbano) {
        this.sector_urbano = sector_urbano;
    }

    public String getSeccion_urbana() {
        return seccion_urbana;
    }

    public void setSeccion_urbana(String seccion_urbana) {
        this.seccion_urbana = seccion_urbana;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getPresencia_und() {
        return presencia_und;
    }

    public void setPresencia_und(String presencia_und) {
        this.presencia_und = presencia_und;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getCod_enumerador() {
        return cod_enumerador;
    }

    public void setCod_enumerador(String cod_enumerador) {
        this.cod_enumerador = cod_enumerador;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(String finalizado) {
        this.finalizado = finalizado;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCodigoAG() {
        return codigoAG;
    }

    public void setCodigoAG(String codigoAG) {
        this.codigoAG = codigoAG;
    }

    public String getNovedadesCartograficas() {
        return novedadesCartograficas;
    }

    public void setNovedadesCartograficas(String novedadesCartograficas) {
        this.novedadesCartograficas = novedadesCartograficas;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    public List<EsquemaEdificacionEnvioViewModel> getEsquemaEdificacion() {
        return esquemaEdificacion;
    }

    public void setEsquemaEdificacion(List<EsquemaEdificacionEnvioViewModel> esquemaEdificacion) {
        this.esquemaEdificacion = esquemaEdificacion;
    }
}

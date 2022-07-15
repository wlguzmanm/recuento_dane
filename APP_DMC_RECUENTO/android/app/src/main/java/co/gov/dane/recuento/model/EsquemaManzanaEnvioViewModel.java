package co.gov.dane.recuento.model;

import java.util.List;

public class EsquemaManzanaEnvioViewModel {

    private String id_manzana;
    private String fechaConteo;
    private String pto_lat_gps;
    private String pto_lon_gps;
    private String pto_alt_gps;
    private String pto_pre_gp;
    private String dpto;
    private String mpio;
    private String clase;
    private String com_loc;
    private String c_pob;
    private String territorio_etnico;
    private String sel_terr_etnico;
    private String cod_resg_etnico;
    private String cod_comun_etnico;
    private String co;
    private String ao;
    private String ag;
    private String acer;
    private String direc_barrio;
    private String existe_unidad;
    private String tipo_novedad;
    private String manzana;

    private String uc;
    private String obsmz;
    private String nov_carto;

    private String fechaModificacion;
    private String cod_enumerador;
    private String supervisor;
    private String finalizado;
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

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getPto_lat_gps() {
        return pto_lat_gps;
    }

    public void setPto_lat_gps(String pto_lat_gps) {
        this.pto_lat_gps = pto_lat_gps;
    }

    public String getPto_lon_gps() {
        return pto_lon_gps;
    }

    public void setPto_lon_gps(String pto_lon_gps) {
        this.pto_lon_gps = pto_lon_gps;
    }

    public String getPto_alt_gps() {
        return pto_alt_gps;
    }

    public void setPto_alt_gps(String pto_alt_gps) {
        this.pto_alt_gps = pto_alt_gps;
    }

    public String getPto_pre_gp() {
        return pto_pre_gp;
    }

    public void setPto_pre_gp(String pto_pre_gp) {
        this.pto_pre_gp = pto_pre_gp;
    }

    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    public String getMpio() {
        return mpio;
    }

    public void setMpio(String mpio) {
        this.mpio = mpio;
    }

    public String getCom_loc() {
        return com_loc;
    }

    public void setCom_loc(String com_loc) {
        this.com_loc = com_loc;
    }

    public String getC_pob() {
        return c_pob;
    }

    public void setC_pob(String c_pob) {
        this.c_pob = c_pob;
    }

    public String getTerritorio_etnico() {
        return territorio_etnico;
    }

    public void setTerritorio_etnico(String territorio_etnico) {
        this.territorio_etnico = territorio_etnico;
    }

    public String getSel_terr_etnico() {
        return sel_terr_etnico;
    }

    public void setSel_terr_etnico(String sel_terr_etnico) {
        this.sel_terr_etnico = sel_terr_etnico;
    }

    public String getCod_resg_etnico() {
        return cod_resg_etnico;
    }

    public void setCod_resg_etnico(String cod_resg_etnico) {
        this.cod_resg_etnico = cod_resg_etnico;
    }

    public String getCod_comun_etnico() {
        return cod_comun_etnico;
    }

    public void setCod_comun_etnico(String cod_comun_etnico) {
        this.cod_comun_etnico = cod_comun_etnico;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getAo() {
        return ao;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    public String getAg() {
        return ag;
    }

    public void setAg(String ag) {
        this.ag = ag;
    }

    public String getAcer() {
        return acer;
    }

    public void setAcer(String acer) {
        this.acer = acer;
    }

    public String getDirec_barrio() {
        return direc_barrio;
    }

    public void setDirec_barrio(String direc_barrio) {
        this.direc_barrio = direc_barrio;
    }

    public String getExiste_unidad() {
        return existe_unidad;
    }

    public void setExiste_unidad(String existe_unidad) {
        this.existe_unidad = existe_unidad;
    }

    public String getTipo_novedad() {
        return tipo_novedad;
    }

    public void setTipo_novedad(String tipo_novedad) {
        this.tipo_novedad = tipo_novedad;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
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

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    public String getObsmz() {
        return obsmz;
    }

    public void setObsmz(String obsmz) {
        this.obsmz = obsmz;
    }

    public String getNov_carto() {
        return nov_carto;
    }

    public void setNov_carto(String nov_carto) {
        this.nov_carto = nov_carto;
    }
}

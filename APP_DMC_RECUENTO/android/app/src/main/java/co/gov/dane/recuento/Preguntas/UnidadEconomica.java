package co.gov.dane.recuento.Preguntas;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UnidadEconomica {

    private String direc_previa;
    private String direc_p_tipo;
    private String direcc;
    private String nov_carto;
    private String estado_unidad_observacion;
    private String tipo_unidad_observacion;
    private String tipo_vendedor;
    private String sector_economico;
    private String unidad_osbservacion;
    private String observacion;


    private String id_manzana,id_edificacion,id_unidad,fechaModificacion, imei;

    public UnidadEconomica(){

    }

    public UnidadEconomica(String id_unidad){
        this.id_unidad=id_unidad;
    }

    public String getDirec_previa() {
        return direc_previa;
    }

    public void setDirec_previa(String direc_previa) {
        this.direc_previa = direc_previa;
    }

    public String getDirec_p_tipo() {
        return direc_p_tipo;
    }

    public void setDirec_p_tipo(String direc_p_tipo) {
        this.direc_p_tipo = direc_p_tipo;
    }

    public String getDirecc() {
        return direcc;
    }

    public void setDirecc(String direcc) {
        this.direcc = direcc;
    }

    public String getNov_carto() {
        return nov_carto;
    }

    public void setNov_carto(String nov_carto) {
        this.nov_carto = nov_carto;
    }

    public String getEstado_unidad_observacion() {
        return estado_unidad_observacion;
    }

    public void setEstado_unidad_observacion(String estado_unidad_observacion) {
        this.estado_unidad_observacion = estado_unidad_observacion;
    }

    public String getTipo_unidad_observacion() {
        return tipo_unidad_observacion;
    }

    public void setTipo_unidad_observacion(String tipo_unidad_observacion) {
        this.tipo_unidad_observacion = tipo_unidad_observacion;
    }

    public String getTipo_vendedor() {
        return tipo_vendedor;
    }

    public void setTipo_vendedor(String tipo_vendedor) {
        this.tipo_vendedor = tipo_vendedor;
    }

    public String getSector_economico() {
        return sector_economico;
    }

    public void setSector_economico(String sector_economico) {
        this.sector_economico = sector_economico;
    }

    public String getUnidad_osbservacion() {
        return unidad_osbservacion;
    }

    public void setUnidad_osbservacion(String unidad_osbservacion) {
        this.unidad_osbservacion = unidad_osbservacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getId_manzana() {
        return id_manzana;
    }

    public void setId_manzana(String id_manzana) {
        this.id_manzana = id_manzana;
    }

    public String getId_edificacion() {
        return id_edificacion;
    }

    public void setId_edificacion(String id_edificacion) {
        this.id_edificacion = id_edificacion;
    }

    public String getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}

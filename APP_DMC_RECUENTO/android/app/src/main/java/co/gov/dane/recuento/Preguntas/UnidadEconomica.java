package co.gov.dane.recuento.Preguntas;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UnidadEconomica {


    private String tipo_via_principal,via_principal,via_secundaria,
            placa_cuadrante,complemento_direccion,estado_ocupado,estado_desocupado,estado_obra,establecimiento_fijo,
            establecimiento_semifijo,puesto_movil,vivienda_actividad_economica,obra_edificacion,comercio,
            industria,servicios,transporte,construccion,no_aplica,nombre_unidad_observacion,observaciones_unidad_observacion,unidades_ocupadas,observaciones_sn,
            imei;

    private String id_manzana,id_edificacion,id_unidad,fechaModificacion;

    public UnidadEconomica(){

    }

    public UnidadEconomica(String id_unidad){
        this.id_unidad=id_unidad;
    }

    public String getTipo_via_principal() {
        return tipo_via_principal;
    }

    public void setTipo_via_principal(String tipo_via_principal) {
        this.tipo_via_principal = tipo_via_principal;
    }

    public String getVia_principal() {
        return via_principal;
    }

    public void setVia_principal(String via_principal) {
        this.via_principal = via_principal;
    }

    public String getVia_secundaria() {
        return via_secundaria;
    }

    public void setVia_secundaria(String via_secundaria) {
        this.via_secundaria = via_secundaria;
    }

    public String getPlaca_cuadrante() {
        return placa_cuadrante;
    }

    public void setPlaca_cuadrante(String placa_cuadrante) {
        this.placa_cuadrante = placa_cuadrante;
    }

    public String getComplemento_direccion() {
        return complemento_direccion;
    }

    public void setComplemento_direccion(String complemento_direccion) {
        this.complemento_direccion = complemento_direccion;
    }

    public String getEstado_ocupado() {
        return estado_ocupado;
    }

    public void setEstado_ocupado(String estado_ocupado) {
        this.estado_ocupado = estado_ocupado;
    }

    public String getEstado_desocupado() {
        return estado_desocupado;
    }

    public void setEstado_desocupado(String estado_desocupado) {
        this.estado_desocupado = estado_desocupado;
    }

    public String getEstado_obra() {
        return estado_obra;
    }

    public void setEstado_obra(String estado_obra) {
        this.estado_obra = estado_obra;
    }

    public String getEstablecimiento_fijo() {
        return establecimiento_fijo;
    }

    public void setEstablecimiento_fijo(String establecimiento_fijo) {
        this.establecimiento_fijo = establecimiento_fijo;
    }

    public String getEstablecimiento_semifijo() {
        return establecimiento_semifijo;
    }

    public void setEstablecimiento_semifijo(String establecimiento_semifijo) {
        this.establecimiento_semifijo = establecimiento_semifijo;
    }

    public String getPuesto_movil() {
        return puesto_movil;
    }

    public void setPuesto_movil(String puesto_movil) {
        this.puesto_movil = puesto_movil;
    }

    public String getVivienda_actividad_economica() {
        return vivienda_actividad_economica;
    }

    public void setVivienda_actividad_economica(String vivienda_actividad_economica) {
        this.vivienda_actividad_economica = vivienda_actividad_economica;
    }

    public String getObra_edificacion() {
        return obra_edificacion;
    }

    public void setObra_edificacion(String obra_edificacion) {
        this.obra_edificacion = obra_edificacion;
    }

    public String getComercio() {
        return comercio;
    }

    public void setComercio(String comercio) {
        this.comercio = comercio;
    }

    public String getIndustria() {
        return industria;
    }

    public void setIndustria(String industria) {
        this.industria = industria;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public String getConstruccion() {
        return construccion;
    }

    public void setConstruccion(String construccion) {
        this.construccion = construccion;
    }

    public String getNo_aplica() {
        return no_aplica;
    }

    public void setNo_aplica(String no_aplica) {
        this.no_aplica = no_aplica;
    }

    public String getNombre_unidad_observacion() {
        return nombre_unidad_observacion;
    }

    public void setNombre_unidad_observacion(String nombre_unidad_observacion) {
        this.nombre_unidad_observacion = nombre_unidad_observacion;
    }

    public String getObservaciones_unidad_observacion() {
        return observaciones_unidad_observacion;
    }

    public void setObservaciones_unidad_observacion(String observaciones_unidad_observacion) {
        this.observaciones_unidad_observacion = observaciones_unidad_observacion;
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

    public void setId_edificacion(String id_edificacion_) {
        this.id_edificacion = id_edificacion_;
    }

    public String getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }


    public String getUnidades_ocupadas() {
        return unidades_ocupadas;
    }

    public void setUnidades_ocupadas(String unidades_ocupadas) {
        this.unidades_ocupadas = unidades_ocupadas;
    }

    public String getObservaciones_sn() {
        return observaciones_sn;
    }

    public void setObservaciones_sn(String observaciones_SN) {
        this.observaciones_sn = observaciones_SN;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}

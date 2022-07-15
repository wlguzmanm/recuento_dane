package co.gov.dane.recuento.Preguntas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class Manzana {

    private String id;
    private String fecha;
    private String departamento;
    private String municipio;
    private String clase;
    private String localidad;
    private String centro_poblado;
    private String territorioEtnico;
    private String selTerritorioEtnico;
    private String resguardoEtnico;
    private String comunidadEtnica;
    private String coordinacionOperativa;  //CO
    private String areaOperativa;       //AO
    private String unidad_cobertura;    //AG
    private String ACER;        //ACER
    private String barrio;
    private String existeUnidad;
    private String tipoNovedad;

    private String uc;
    private String obsmz;
    private String nov_carto;


    private String manzana;

    private String latitud;
    private String longitud;
    private String altura;
    private String precision;

    private String imei;
    private String fechaModificacion;

    private Boolean validar;
    private String cod_enumerador;
    private String supervisor;
    private String finalizado;
    private String doble_sincronizado;
    private String sincronizado_nube;

    private String id_manzana;
    private String pendiente_sincronizar;

    private List<Edificacion> edificaciones;

    public Manzana(){

    }

    public Manzana(String id, String id_manzana) {
        this.id = id;
        this.id_manzana = id_manzana;
    }

    public Manzana(String id_manzana){
        this.id_manzana=id_manzana;
    }


    public String getLatitud(){
        if(latitud==null){
            return "";
        }else{
            return latitud;
        }

    }

    public void setLatitud(String latitud){
        this.latitud=latitud;
    }

    public String getLongitud(){
        if(longitud==null){
            return "";
        }else{
            return longitud;
        }

    }

    public void setLongitud(String longitud){
            this.longitud=longitud;
    }

    public String getAltura(){
        if(altura==null){
            return "";
        }else{
            return altura;
        }
    }

    public void setAltura(String altura){this.altura=altura;}

    public String getPrecision() {
        if(precision==null){
            return "";
        }else{
            return precision;
        }
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String json(Manzana manzana){
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(manzana);// obj is your object
        return json;
    }


    public String getFinalizado() {
        if(finalizado==null){
            return "No";
        }else{
            return finalizado;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCentro_poblado() {
        return centro_poblado;
    }

    public void setCentro_poblado(String centro_poblado) {
        this.centro_poblado = centro_poblado;
    }

    public String getTerritorioEtnico() {
        return territorioEtnico;
    }

    public void setTerritorioEtnico(String territorioEtnico) {
        this.territorioEtnico = territorioEtnico;
    }

    public String getSelTerritorioEtnico() {
        return selTerritorioEtnico;
    }

    public void setSelTerritorioEtnico(String selTerritorioEtnico) {
        this.selTerritorioEtnico = selTerritorioEtnico;
    }

    public String getResguardoEtnico() {
        return resguardoEtnico;
    }

    public void setResguardoEtnico(String resguardoEtnico) {
        this.resguardoEtnico = resguardoEtnico;
    }

    public String getComunidadEtnica() {
        return comunidadEtnica;
    }

    public void setComunidadEtnica(String comunidadEtnica) {
        this.comunidadEtnica = comunidadEtnica;
    }

    public String getCoordinacionOperativa() {
        return coordinacionOperativa;
    }

    public void setCoordinacionOperativa(String coordinacionOperativa) {
        this.coordinacionOperativa = coordinacionOperativa;
    }

    public String getAreaOperativa() {
        return areaOperativa;
    }

    public void setAreaOperativa(String areaOperativa) {
        this.areaOperativa = areaOperativa;
    }

    public String getUnidad_cobertura() {
        return unidad_cobertura;
    }

    public void setUnidad_cobertura(String unidad_cobertura) {
        this.unidad_cobertura = unidad_cobertura;
    }

    public String getACER() {
        return ACER;
    }

    public void setACER(String ACER) {
        this.ACER = ACER;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getExisteUnidad() {
        return existeUnidad;
    }

    public void setExisteUnidad(String existeUnidad) {
        this.existeUnidad = existeUnidad;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
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

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
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

    public void setFinalizado(String finalizado) {
        this.finalizado = finalizado;
    }

    public String getDoble_sincronizado() {
        return doble_sincronizado;
    }

    public void setDoble_sincronizado(String doble_sincronizado) {
        this.doble_sincronizado = doble_sincronizado;
    }

    public String getSincronizado_nube() {
        return sincronizado_nube;
    }

    public void setSincronizado_nube(String sincronizado_nube) {
        this.sincronizado_nube = sincronizado_nube;
    }

    public String getId_manzana() {
        return id_manzana;
    }

    public void setId_manzana(String id_manzana) {
        this.id_manzana = id_manzana;
    }

    public String getPendiente_sincronizar() {
        return pendiente_sincronizar;
    }

    public void setPendiente_sincronizar(String pendiente_sincronizar) {
        this.pendiente_sincronizar = pendiente_sincronizar;
    }

    public List<Edificacion> getEdificaciones() {
        return edificaciones;
    }

    public void setEdificaciones(List<Edificacion> edificaciones) {
        this.edificaciones = edificaciones;
    }

    public String getTipoNovedad() {
        return tipoNovedad;
    }

    public void setTipoNovedad(String tipoNovedad) {
        this.tipoNovedad = tipoNovedad;
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

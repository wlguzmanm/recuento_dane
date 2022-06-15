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
    private String centro_poblado;
    private String localidad;
    private String coordinacionOperativa;
    private String areaOperativa;
    private String ACER;
    private String unidad_cobertura;
    private String sector_urbano;
    private String seccion_urbana;
    private String manzana;
    private String presencia_und;
    private String latitud;
    private String longitud;
    private String altura;
    private String precision;
    private String barrio;
    private String observaciones;
    private String codigoAG;
    private String novedadesCartografias;
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

    public String getFecha(){
        return fecha;
    }
    public void setFecha(String fecha){
        this.fecha=fecha;
    }

    public String getDepartamento(){
        return departamento;
    }
    public void setDepartamento(String departamento){
        this.departamento=departamento;
    }

    public String getMunicipio(){
        return municipio;
    }
    public void setMunicipio(String municipio){
        this.municipio=municipio;
    }

    public String getClase(){
        return clase;
    }
    public void setClase(String clase){
        this.clase=clase;
    }

    public String getCentroPoblado(){
        return centro_poblado;
    }
    public void setCentroPoblado(String centro_poblado){
        this.centro_poblado=centro_poblado;
    }

    public String getSectorUrbano(){
        return sector_urbano;
    }

    public void setSectorUrbano(String sector_urbano){
        this.sector_urbano=sector_urbano;
    }

    public String getSeccionUrbana(){
        return seccion_urbana;
    }
    public void setSeccionUrbana(String seccion_urbana){
        this.seccion_urbana=seccion_urbana;
    }

    public String getManzana(){
        return manzana;
    }
    public void setManzana(String manzana){
        this.manzana=manzana;
    }

    public String getPresenciaUnidades(){
        return presencia_und;
    }

    public void setMPresenciaUnidades(String presencia_und){
        this.presencia_und=presencia_und;
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


    public List<Edificacion> getEdificaciones() {
        return edificaciones;
    }

    public void setEdificaciones(List<Edificacion> edificaciones) {
        this.edificaciones = edificaciones;
    }

    public String getId_manzana() {
        return id_manzana;
    }

    public void setId_manzana(String id_manzana) {
        this.id_manzana = id_manzana;
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
        if(finalizado==null){
            return "No";
        }else{
            return finalizado;
        }
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

    public String getNovedadesCartografias() {
        return novedadesCartografias;
    }

    public void setNovedadesCartografias(String novedadesCartografias) {
        this.novedadesCartografias = novedadesCartografias;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPendiente_sincronizar() {
        return pendiente_sincronizar;
    }

    public void setPendiente_sincronizar(String pendiente_sincronizar) {
        this.pendiente_sincronizar = pendiente_sincronizar;
    }
    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
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

    public String getACER() {
        return ACER;
    }

    public void setACER(String ACER) {
        this.ACER = ACER;
    }

    public String getUnidad_cobertura() {
        return unidad_cobertura;
    }

    public void setUnidad_cobertura(String unidad_cobertura) {
        this.unidad_cobertura = unidad_cobertura;
    }

}

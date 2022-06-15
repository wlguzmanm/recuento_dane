package co.gov.dane.recuento.model;

public class ConteoManzana {
    private String id, nombre, fecha,finalizado,sincronizado, doble_sincronizado, sincronizado_nube;
    private int enviar;

    public ConteoManzana() {
    }

    public ConteoManzana(String id, String nombre, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public ConteoManzana(String id, String nombre, String fecha,String finalizado,String sincronizado, String doble_sincronizado, String sincronizado_nube) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.finalizado=finalizado;
        this.sincronizado=sincronizado;
        this.enviar=0;
        this.doble_sincronizado = doble_sincronizado;
        this.sincronizado_nube = sincronizado_nube;
    }

    public String getId() {
        return id;
    }


    public String getFecha() {
        return "Modificado: "+fecha;
    }


    public String getNombre() {
        return "Manzana: "+nombre;
    }

    public void setEnviar(int enviar){this.enviar=enviar;}

    public int getEnviar(){
        return enviar;
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

    public String getSincronizado() {
        if(sincronizado==null){
            return "No";
        }else{
            return sincronizado;
        }
    }

    public void setSincronizado(String sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getDoble_sincronizado() {
        if(doble_sincronizado==null){
            return "No";
        }else{
            return doble_sincronizado;
        }
    }

    public void setDoble_sincronizado(String doble_sincronizado) {
        this.doble_sincronizado = doble_sincronizado;
    }

    public String getSincronizado_nube() {
        if(sincronizado_nube==null){
            return "No";
        }else{
            return sincronizado_nube;
        }
    }

    public void setSincronizado_nube(String sincronizado_nube) {
        this.sincronizado_nube = sincronizado_nube;
    }
}

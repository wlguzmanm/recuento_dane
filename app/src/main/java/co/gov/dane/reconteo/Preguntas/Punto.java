package co.gov.dane.reconteo.Preguntas;

public class Punto {

    double Latitud;
    double Longitud;

    public Punto(Double Latitud,Double Longitud){
        this.Latitud=Latitud;
        this.Longitud=Longitud;
    }

    public Double getLatitud(){
        return Latitud;
    }
    public Double getLongitud(){
        return  Longitud;
    }

}

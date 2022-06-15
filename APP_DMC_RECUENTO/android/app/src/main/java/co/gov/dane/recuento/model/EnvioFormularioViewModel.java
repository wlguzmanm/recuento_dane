package co.gov.dane.recuento.model;

import java.util.List;

public class EnvioFormularioViewModel {

    private List<EsquemaManzanaEnvioViewModel> esquemaManzana;
    private String fechaInicial;
    private String estado;

    public List<EsquemaManzanaEnvioViewModel> getEsquemaManzana() {
        return esquemaManzana;
    }

    public void setEsquemaManzana(List<EsquemaManzanaEnvioViewModel> esquemaManzana) {
        this.esquemaManzana = esquemaManzana;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

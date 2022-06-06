package co.gov.dane.reconteo.model;

import java.io.Serializable;

public class Offline implements Serializable {

    private Long id;
    private String username;
    private Boolean activo = false;

    public Offline(Long id) {
        this.id = id;
    }

    public Offline() {
    }

    public Offline(Long id,  String username, Boolean activo) {
        this.id = id;
        this.activo = activo;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
package br.com.santos.valdomiro.anotacoeschopp.model;

import java.time.LocalDateTime;

public class Nota {

    private int id;
    private String codigo;
    private String hora;

    public Nota() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHora() {
        return hora;
    }

}

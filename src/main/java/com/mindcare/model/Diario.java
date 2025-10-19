package com.mindcare.model;

public class Diario {
    private Integer id;
    private String titulo;
    private String conteudo;
    private String dataCriacao;

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    @Override
    public String toString() {
        String t = titulo != null ? titulo : "(sem título)";
        String d = dataCriacao != null ? dataCriacao : "";
        return t + " — " + d;
    }
}

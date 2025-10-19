package com.mindcare.model;

public class Consulta {
    private Integer id;
    private String estadoMental;
    private String dicasSobre;
    private String relato;
    private String tomConsulta;
    private String respostaIA;
    private String dataConsulta;

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEstadoMental() { return estadoMental; }
    public void setEstadoMental(String estadoMental) { this.estadoMental = estadoMental; }
    public String getDicasSobre() { return dicasSobre; }
    public void setDicasSobre(String dicasSobre) { this.dicasSobre = dicasSobre; }
    public String getRelato() { return relato; }
    public void setRelato(String relato) { this.relato = relato; }
    public String getTomConsulta() { return tomConsulta; }
    public void setTomConsulta(String tomConsulta) { this.tomConsulta = tomConsulta; }
    public String getRespostaIA() { return respostaIA; }
    public void setRespostaIA(String respostaIA) { this.respostaIA = respostaIA; }
    public String getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(String dataConsulta) { this.dataConsulta = dataConsulta; }

    @Override
    public String toString() {
        String s = estadoMental != null ? estadoMental : "";
        String d = dataConsulta != null ? dataConsulta : "";
        String ds = dicasSobre != null ? dicasSobre : "";
        return s + " — " + ds + " — " + d;
    }
}

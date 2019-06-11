package com.example.finalsimme.Model;

public class Equipamento {

    public String codoper, cEquipamentoID, dataAquisicao;
    public Boolean estado;

    public Equipamento(String codoper, String cEquipamentoID, Boolean estado, String dataAquisicao) {
        this.codoper = codoper;
        this.cEquipamentoID = cEquipamentoID;
        this.estado = estado;
        this.dataAquisicao = dataAquisicao;
    }

    public String getCodoper() {
        return codoper;
    }

    public void setCodoper(String codoper) {
        this.codoper = codoper;
    }

    public String getcEquipamentoID() {
        return cEquipamentoID;
    }

    public void setcEquipamentoID(String cEquipamentoID) {
        this.cEquipamentoID = cEquipamentoID;
    }

    public Boolean getEstado(){
        return estado;
    }

    public void setEstado(Boolean estado){
        this.estado = estado;
    }

    public String getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(String dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public Equipamento(){

    }

}

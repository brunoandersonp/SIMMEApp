package com.example.finalsimme.Model;

import java.io.Serializable;

public class Pontos implements Serializable {

    public String descr, datahora, pNome, instrume, valorglobal, alarmeinf, alarmesup, cPontoID, unidade;
    public boolean isSelected;

    public Pontos(String descr, String datahora, String pNome, String instrume, String alarmeinf, String alarmesup, String valorglobal, String unidade, String cPontoID) {
        this.descr = descr;
        this.datahora = datahora;
        this.pNome = pNome;
        this.instrume = instrume;
        this.alarmeinf = alarmeinf;
        this.alarmesup = alarmesup;
        this.valorglobal = valorglobal;
        this.unidade = unidade;
        this.cPontoID = cPontoID;
    }

    public String getDescr() {

        return descr;
    }

    public void setDescr(String descr) {

        this.descr = descr;
    }

    public String getDatahora() {
        return datahora;
    }

    public void setDatahora(String datahora) {
        this.datahora = datahora;
    }

    public String getpNome() {
        return pNome;
    }

    public void setpNome(String pNome) {
        this.pNome = pNome;
    }

    public String getInstrume() {
        return instrume;
    }

    public void setInstrume(String instrume) {
        this.instrume = instrume;
    }

    public String getAlarmeinf() {
        return alarmeinf;
    }

    public void setAlarmeinf(String alarmeinf) {
        this.alarmeinf = alarmeinf;
    }

    public String getAlarmesup() {
        return alarmesup;
    }

    public void setAlarmesup(String alarmesup) {
        this.alarmesup = alarmesup;
    }

    public String getValorglobal() {
        return valorglobal;
    }

    public void setValorglobal(String valorglobal) {
        this.valorglobal = valorglobal;
    }

    public String getUnidade() { return unidade;}

    public  void setUnidade(String unidade) { this.unidade = unidade;}

    public String getcPontoID() {
        return cPontoID;
    }

    public void setcPontoID(String cPontoID) {
        this.cPontoID = cPontoID;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Pontos(){

    }

}

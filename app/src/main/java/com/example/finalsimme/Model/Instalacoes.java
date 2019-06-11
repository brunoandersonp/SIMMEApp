package com.example.finalsimme.Model;

public class Instalacoes {

    public String nome, tipo, banco, cInstalacaoID;

    public Instalacoes(String nome, String tipo, String banco, String cInstalacaoID) {
        this.nome = nome;
        this.tipo = tipo;
        this.banco = banco;
        this.cInstalacaoID = cInstalacaoID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getcInstalacaoID() {
        return cInstalacaoID;
    }

    public void setcInstalacaoID(String cInstalacaoID) {
        this.cInstalacaoID = cInstalacaoID;
    }


    public Instalacoes(){

    }

}

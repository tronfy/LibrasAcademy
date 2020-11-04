package br.unicamp.cotuca.librasacademy.dbo;

import java.io.Serializable;

public class Licao implements Serializable {
    private String nome;
    private String descricao;

    public Licao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}

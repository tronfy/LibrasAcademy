package br.unicamp.cotuca.librasacademy.dbo;

import java.io.Serializable;

public class Licao implements Serializable {
    private final String nome;
    private final String descricao;
    private int codigo;

    public Licao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Licao(String nome, String descricao, int codigo) {
        this.nome = nome;
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCodigo() { return codigo; }
}

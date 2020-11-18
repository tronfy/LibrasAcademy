package br.unicamp.cotuca.librasacademy.dbo;

public class SubLicao {
    private final String texto;
    private final int codigo;

    public SubLicao (String texto, int codigo) {
        this.texto = texto;
        this.codigo = codigo;
    }

    public String getTexto() {
        return texto;
    }

    public int getCodigo() {
        return codigo;
    }
}

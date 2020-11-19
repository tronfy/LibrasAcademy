package br.unicamp.cotuca.librasacademy.dbo;

public class SubLicao {
    private final String texto;
    private final int codigo;
    private final String imagem;

    public SubLicao (String texto, int codigo, String imagem) {
        this.texto = texto;
        this.codigo = codigo;
        this.imagem = imagem;
    }

    public String getTexto() {
        return texto;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getImagem() { return imagem; }
}

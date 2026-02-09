package br.luciano.quempegou;

import br.luciano.quempegou.enums.PrioridadeDevolucao;

public class Emprestimo {

    private String nomeItemEmprestado;
    private int amigo;
    private PrioridadeDevolucao prioridadeDevolucao;
    private boolean fragil;
    private boolean devolvido;
    private String observacao;

    public Emprestimo(String nomeItemEmprestado, int amigo, PrioridadeDevolucao prioridadeDevolucao, boolean fragil, String observacao) {
        this.nomeItemEmprestado = nomeItemEmprestado;
        this.amigo = amigo;
        this.prioridadeDevolucao = prioridadeDevolucao;
        this.fragil = fragil;
        this.observacao = observacao;
    }

    public String getNomeItemEmprestado() {
        return nomeItemEmprestado;
    }

    public void setNomeItemEmprestado(String nomeItemEmprestado) {
        this.nomeItemEmprestado = nomeItemEmprestado;
    }

    public int getAmigo() {
        return amigo;
    }

    public void setAmigo(int amigo) {
        this.amigo = amigo;
    }

    public PrioridadeDevolucao getPrioridadeDevolucao() {
        return prioridadeDevolucao;
    }

    public void setPrioridadeDevolucao(PrioridadeDevolucao prioridadeDevolucao) {
        this.prioridadeDevolucao = prioridadeDevolucao;
    }

    public boolean isFragil() {
        return fragil;
    }

    public void setFragil(boolean fragil) {
        this.fragil = fragil;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return nomeItemEmprestado + "\n" +
                amigo + "\n" +
                prioridadeDevolucao + "\n" +
                fragil + "\n" +
                observacao;
    }
}

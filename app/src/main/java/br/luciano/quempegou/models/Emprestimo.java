package br.luciano.quempegou.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.Objects;

@Entity
public class Emprestimo {

    public static Comparator<Emprestimo> ordenacaoCrescente = new Comparator<>() {
        @Override
        public int compare(Emprestimo emprestimo1, Emprestimo emprestimo2) {
            return emprestimo1.getNomeItemEmprestado().compareToIgnoreCase(emprestimo2.getNomeItemEmprestado());
        }
    };

    public static Comparator<Emprestimo> ordenacaoNaoDevolvidos = new Comparator<>() {
        @Override
        public int compare(Emprestimo emprestimo1, Emprestimo emprestimo2) {
            int resultado = Boolean.compare(emprestimo1.isDevolvido(), emprestimo2.isDevolvido());

            if (resultado == 0) {
                resultado = emprestimo1.getNomeItemEmprestado().compareToIgnoreCase(emprestimo2.getNomeItemEmprestado());
            }

            return resultado;
        }
    };

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @NonNull
    @ColumnInfo(index = true)
    private String nomeItemEmprestado;
    private int amigo;
    private PrioridadeDevolucao prioridadeDevolucao;
    private boolean fragil;
    private boolean devolvido;
    private String observacao;
    private long dataEmprestimo;
    private Long dataDevolucao;

    public Emprestimo(String nomeItemEmprestado, int amigo, PrioridadeDevolucao prioridadeDevolucao, boolean fragil, boolean devolvido, String observacao, long dataEmprestimo, Long dataDevolucao) {
        this.nomeItemEmprestado = nomeItemEmprestado;
        this.amigo = amigo;
        this.prioridadeDevolucao = prioridadeDevolucao;
        this.fragil = fragil;
        this.devolvido = devolvido;
        this.observacao = observacao;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(long dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Long getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Long dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Emprestimo emprestimo = (Emprestimo) o;

        return amigo == emprestimo.amigo &&
                fragil == emprestimo.fragil &&
                devolvido == emprestimo.devolvido &&
                dataEmprestimo == emprestimo.dataEmprestimo &&
                Objects.equals(dataDevolucao, emprestimo.dataDevolucao) &&
                nomeItemEmprestado.equalsIgnoreCase(emprestimo.nomeItemEmprestado) &&
                prioridadeDevolucao == emprestimo.prioridadeDevolucao &&
                observacao.equalsIgnoreCase(emprestimo.observacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeItemEmprestado, amigo, prioridadeDevolucao, fragil, devolvido, observacao, dataEmprestimo, dataDevolucao);
    }

    @Override
    public String toString() {
        return nomeItemEmprestado + "\n" +
                amigo + "\n" +
                prioridadeDevolucao + "\n" +
                fragil + "\n" +
                observacao + "\n" +
                dataEmprestimo + "\n" +
                dataDevolucao;
    }
}

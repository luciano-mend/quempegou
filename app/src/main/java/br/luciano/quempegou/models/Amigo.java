package br.luciano.quempegou.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Amigo {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    @ColumnInfo(index = true)
    private String nome;

    private String observacao;

    private long dataInclusao;

    private boolean ativo;

    public Amigo(@NonNull String nome, String observacao, long dataInclusao, boolean ativo) {
        this.nome = nome;
        this.observacao = observacao;
        this.dataInclusao = dataInclusao;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public long getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(long dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Amigo amigo = (Amigo) o;

        return dataInclusao == amigo.dataInclusao &&
                ativo == amigo.ativo &&
                nome.equalsIgnoreCase(amigo.nome) &&
                (Objects.equals(observacao, amigo.observacao) ||
                 (observacao != null && observacao.equalsIgnoreCase(amigo.observacao)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase(), observacao != null ? observacao.toLowerCase() : null, dataInclusao, ativo);
    }

    @Override
    public String toString() {
        return nome + "\n" +
                observacao + "\n" +
                dataInclusao + "\n" +
                ativo;
    }
}

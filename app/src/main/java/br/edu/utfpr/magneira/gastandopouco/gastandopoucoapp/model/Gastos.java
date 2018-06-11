package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class Gastos {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String descricao;

    @ColumnInfo(index = true)
    private int tipoId;

    @NonNull
    private Double valor;

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NonNull String descricao) {
        this.descricao = descricao;
    }

    @NonNull
    public Double getValor() {
        return valor;
    }

    public void setValor(@NonNull Double valor) {
        this.valor = valor;
    }


}

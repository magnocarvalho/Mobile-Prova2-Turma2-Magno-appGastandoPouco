package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Comparator;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "gastos",
        foreignKeys = @ForeignKey(entity = Tipo.class,
                parentColumns = "id",
                childColumns  = "tipoId"))

public class Gasto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private double valor;

    @ColumnInfo(index = true)
    private int tipoId;
    private String tipoGastoS;

    public String getTipoGastoS() {
        return tipoGastoS;
    }

    public void setTipoGastoS(String tipoGastoS) {
        this.tipoGastoS = tipoGastoS;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    @Override
    public String toString() {
        return ""+ getValor() + "  :  " + getTipoGastoS();
    }


}

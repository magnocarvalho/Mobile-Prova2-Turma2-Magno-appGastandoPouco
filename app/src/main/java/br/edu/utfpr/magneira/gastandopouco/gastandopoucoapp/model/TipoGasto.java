package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model;

import android.arch.persistence.room.Entity;

@Entity(tableName = "tipo_gasto", inheritSuperIndices = true)
public class TipoGasto extends Tipo {

    public TipoGasto(String descricao) {
        super(descricao);
    }
}

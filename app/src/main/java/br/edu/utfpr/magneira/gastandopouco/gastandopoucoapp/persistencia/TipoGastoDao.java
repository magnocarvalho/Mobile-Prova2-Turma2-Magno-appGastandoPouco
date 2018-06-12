package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.TipoGasto;

@Dao
public interface TipoGastoDao {

    @Insert
    long insert(TipoGasto tipoGasto);

    @Delete
    void delete(TipoGasto tipoGasto);

    @Update
    void update(TipoGasto tipoGasto);

    @Query("SELECT * FROM tipo_gasto WHERE id = :id")
    TipoGasto queryForId(long id);

    @Query("SELECT * FROM tipo_gasto WHERE descricao = :descricao ORDER BY descricao ASC")
    List<TipoGasto> queryForDescricao(String descricao);

    @Query("SELECT * FROM tipo_gasto ORDER BY descricao ASC")
    List<TipoGasto> queryAll();

    @Query("SELECT count(*) FROM tipo_gasto")
    int total();
}

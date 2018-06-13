package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Gasto;

@Dao
public interface GastosDao {

    @Insert
    long insert(Gasto gasto);

    @Delete
    void delete(Gasto gasto);

    @Update
    void update(Gasto gasto);

    @Query("SELECT * FROM gastos WHERE id = :id")
    Gasto queryForId(long id);

    @Query("SELECT * FROM gastos WHERE pessoaId = :pessoaId")
    List<Gasto> queryForPessoaId(long pessoaId);

    @Query("SELECT * FROM gastos WHERE tipoGastoId = :id ORDER BY valor ASC")
    List<Gasto> queryForTipoContatoId(long id);

    @Query("SELECT * FROM gastos ORDER BY id ASC")
    List<Gasto> queryAll();
}

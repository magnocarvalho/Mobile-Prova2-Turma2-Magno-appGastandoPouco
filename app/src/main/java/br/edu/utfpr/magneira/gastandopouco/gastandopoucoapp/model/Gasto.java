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
        foreignKeys = {@ForeignKey(entity = TipoGasto.class,
                                   parentColumns = "id",
                                   childColumns = "tipoGastoId"),
                       @ForeignKey(entity = Pessoa.class,
                                   parentColumns = "id",
                                   childColumns = "pessoaId",
                                   onDelete = CASCADE)})
public class Gasto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private double valor;

    @ColumnInfo(index = true)
    private int pessoaId;

    public int getTipoGastoId() {
        return tipoGastoId;
    }

    @ColumnInfo(index = true)
    private int tipoGastoId;

    public void setValor( double valor) {
        this.valor = valor;
    }

    public void setTipoGastoId(int tipoGastoId) {
        this.tipoGastoId = tipoGastoId;
    }

    @Ignore
    private TipoGasto tipoGasto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }



    public int getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(int pessoaId) {
        this.pessoaId = pessoaId;
    }

    public int getTipoContatoId() {
        return tipoGastoId;
    }

    public void setTipoContatoId(int tipoContatoId) {
        this.tipoGastoId = tipoContatoId;
    }

    public TipoGasto getTipoGasto(){
        return tipoGasto;
    }

    public void setTipoGasto(TipoGasto tipoGasto){
        this.tipoGasto = tipoGasto;
    }

    public static Comparator<Gasto> comparador = new Comparator<Gasto>() {
        @Override
        public int compare(Gasto c1, Gasto c2) {

            if (c1.tipoGasto != null && c2.tipoGasto != null){

                int ordemTipo = c1.getTipoGasto().getDescricao().compareToIgnoreCase(c2.getTipoGasto().getDescricao());

                if (ordemTipo == 0){
                   return String.valueOf(c1.getValor()).compareToIgnoreCase( String.valueOf(c2.getValor()));

                }else{
                    return ordemTipo;
                }
            }else{
                return  String.valueOf(c1.getValor()).compareToIgnoreCase( String.valueOf(c2.getValor()));
            }
        }
    };

}

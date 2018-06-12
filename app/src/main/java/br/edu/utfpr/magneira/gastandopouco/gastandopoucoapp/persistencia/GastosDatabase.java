package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.concurrent.Executors;

import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.R;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Tipo;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Gasto;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Pessoa;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.TipoGasto;


@Database(entities = {Tipo.class, TipoGasto.class, Pessoa.class, Gasto.class}, version = 1)
@TypeConverters({br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.Converters.class})
public abstract class GastosDatabase extends RoomDatabase {

    public abstract TipoDao tipoDao();

    public abstract TipoGastoDao tipoGastoDao();

    public abstract PessoaDao pessoaDao();

    public abstract GastosDao gastosDao();

    private static br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase instance;

    public static br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase.class) {
                if (instance == null) {
                   Builder builder =  Room.databaseBuilder(context,
                                                           br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase.class,
                                                           "gastos.db");

                   builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    carregaTiposIniciais(context);
                                    carregaTiposContatosIniciais(context);
                                }
                            });
                        }
                   });

                   instance = (br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase) builder.build();
                }
            }
        }

        return instance;
    }
    private static void carregaTiposIniciais(final Context context){

        String[] descricoes = context.getResources().getStringArray(R.array.tipos_iniciais);

        for (String descricao : descricoes) {

            Tipo tipo = new Tipo(descricao);

            tipo.setDataCadastro(new Date());

            instance.tipoDao().insert(tipo);
        }
    }

    private static void carregaTiposContatosIniciais(final Context context){

        String[] descricoes = context.getResources().getStringArray(R.array.tipos_contatos_iniciais);

        for (String descricao : descricoes) {

            TipoGasto tipoContato = new TipoGasto(descricao);

            tipoContato.setDataCadastro(new Date());

            instance.tipoGastoDao().insert(tipoContato);
        }
    }




}

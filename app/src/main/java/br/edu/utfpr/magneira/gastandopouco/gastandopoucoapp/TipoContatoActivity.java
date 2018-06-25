package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.List;


import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Gasto;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.TipoGasto;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util.UtilsDate;
import  br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util.UtilGUI;
public class TipoContatoActivity extends AppCompatActivity {

    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText editTexDescricao;
    private TextView textViewDataCadastro;
    private EditText editTextDataCadastro;

    private int         modo;
    private TipoGasto tipoContato;

    public static void novo(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, TipoContatoActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, TipoGasto tipoContato){

        Intent intent = new Intent(activity, TipoContatoActivity.class);
        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, tipoContato.getId());
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editTexDescricao     = findViewById(R.id.editTextDescricao);
        editTextDataCadastro.setEnabled(false);
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO, NOVO);

        if (modo == ALTERAR){

            setTitle(R.string.alterar_tipo_contato);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    int id = bundle.getInt(ID);

               GastosDatabase database = GastosDatabase.getDatabase(TipoContatoActivity.this);

                    tipoContato = database.tipoGastoDao().queryForId(id);

                    TipoContatoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTexDescricao.setText(tipoContato.getDescricao());

                            String texto = UtilsDate.formatDate(TipoContatoActivity.this,
                                                                tipoContato.getDataCadastro());

                            editTextDataCadastro.setText(texto);
                        }
                    });
                }
            });

        }else{

            setTitle(R.string.novo_tipo_contato);

            tipoContato = new TipoGasto("");

            textViewDataCadastro.setVisibility(View.INVISIBLE);
            editTextDataCadastro.setVisibility(View.INVISIBLE);
        }
    }

    private void salvar(){

        final String descricao  = UtilGUI.validaCampoTexto(this,
                                                      editTexDescricao,
                                                      R.string.descricao_vazia);
        if (descricao == null){
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                GastosDatabase database = GastosDatabase.getDatabase(TipoContatoActivity.this);

                List<TipoGasto> lista = database.tipoGastoDao().queryForDescricao(descricao);

                if (modo == NOVO) {

                    if (lista.size() > 0){

                        TipoContatoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilGUI.avisoErro(TipoContatoActivity.this, R.string.descricao_usada);
                            }
                        });

                        return;
                    }

                    tipoContato.setDescricao(descricao);
                    tipoContato.setDataCadastro(new Date());

                    database.tipoGastoDao().insert(tipoContato);

                } else {

                    if (!descricao.equals(tipoContato.getDescricao())){

                        if (lista.size() >= 1){

                            TipoContatoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilGUI.avisoErro(TipoContatoActivity.this, R.string.descricao_usada);
                                }
                            });

                            return;
                        }

                        tipoContato.setDescricao(descricao);

                        database.tipoGastoDao().update(tipoContato);
                    }
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicao_detalhes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemCancelar:
                cancelar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

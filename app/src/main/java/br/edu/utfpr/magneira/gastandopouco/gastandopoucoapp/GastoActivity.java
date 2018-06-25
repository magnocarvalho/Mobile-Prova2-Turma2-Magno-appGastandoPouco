package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Gasto;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Tipo;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util.UtilGUI;

public class GastoActivity extends AppCompatActivity {

    public static final String ID_CONTATO            = "ID_CONTATO";
    public static final String VALOR                 = "VALOR";
    public static final String ID_TIPO_CONTATO       = "ID_TIPO_CONTATO";
    public static final String VALORES_USADOS        = "VALORES_USADOS";
    public static final String TIPOS_CONTATOS_USADOS = "TIPOS_CONTATOS_USADOS";

    public static final String MODO    = "MODO";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText editTextValor;

    private Spinner                   spinnerTipoGasto;
    private List<Tipo>  listaTipos;

    private int modo;
    private int idGasto;
    private int idTipoGasto;

    private int[]    tiposUsados;
    private String[] valoresUsados;
    private Gasto gastoObj;


    public static void novo(Activity activity, int requestCode, List<Gasto> lista){

        Intent intent = new Intent(activity, GastoActivity.class);

        intent.putExtra(MODO, NOVO);

        incorporaUsados(intent, lista);

        activity.startActivityForResult(intent, requestCode);
    }
    public static void novo(Activity activity, int requestCode){

        Intent intent = new Intent(activity, GastoActivity.class);
        intent.putExtra(MODO, NOVO);


        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Gasto contato, List<Gasto> lista){

        Intent intent = new Intent(activity, GastoActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID_CONTATO,      contato.getId());
        intent.putExtra(VALOR,           contato.getValor());
        intent.putExtra(ID_TIPO_CONTATO, contato.getTipoId());

        ArrayList<Gasto> cloneList = new ArrayList<>(lista);

        cloneList.remove(contato);

        incorporaUsados(intent, cloneList);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Gasto contato)
    {

        Intent intent = new Intent(activity, GastoActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID_CONTATO,      contato.getId());
        intent.putExtra(VALOR,           contato.getValor());
        intent.putExtra(ID_TIPO_CONTATO, contato.getTipoId());


        activity.startActivityForResult(intent, requestCode);

    }
    public static void alterar(Activity activity, int requestCode,  int gastoID, String gastoValor, int GastoTipoId){

        Intent intent = new Intent(activity, GastoActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID_CONTATO,     gastoID);
        intent.putExtra(VALOR,           gastoValor);
        intent.putExtra(ID_TIPO_CONTATO, GastoTipoId);

        activity.startActivityForResult(intent, requestCode);
    }

    private static Intent incorporaUsados(Intent intent, List<Gasto> lista){

        if (lista != null && lista.size() > 0){

            int[]    tiposUsados   = new int[lista.size()];
            String[] valoresUsados = new String[lista.size()];

            for (int cont = 0; cont < lista.size(); cont++){

                Gasto c = lista.get(cont);

                tiposUsados[cont]   = c.getTipoId();
                valoresUsados[cont] = Double.toString(c.getValor());
            }

            intent.putExtra(VALORES_USADOS, valoresUsados);
            intent.putExtra(TIPOS_CONTATOS_USADOS, tiposUsados);
        }

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextValor      = findViewById(R.id.editTextValor);
        spinnerTipoGasto = findViewById(R.id.spinnerTipoContato);
        carregaTipos();

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO, NOVO);

        tiposUsados   = bundle.getIntArray(TIPOS_CONTATOS_USADOS);
        valoresUsados = bundle.getStringArray(VALORES_USADOS);

        if (modo == ALTERAR){

            setTitle(R.string.alterar_contato);

            idGasto     = bundle.getInt(ID_CONTATO);
            idTipoGasto = bundle.getInt(ID_TIPO_CONTATO);
            spinnerTipoGasto.setSelection(idTipoGasto);
            editTextValor.setText(String.valueOf(bundle.getDouble(VALOR)));

        }else{

            setTitle(R.string.novo_gasto);

            idGasto = -1;
        }
        gastoObj = new Gasto();

    }

    private int posicaoTipoGastoId(int TipoGastoId){

        for (int pos = 0; pos < listaTipos.size(); pos++){

            Tipo t = listaTipos.get(pos);

            if (t.getId() == TipoGastoId){
                return pos;
            }
        }

        return -1;
    }

//    private void carregaTiposGasto(){
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                GastosDatabase database = GastosDatabase.getDatabase(GastoActivity.this);
//
//                listaTiposGasto = database.tipoDao().queryAll();
//
//
//                GastoActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        spinnerAdapter = new ArrayAdapter<>(GastoActivity.this,
//                                android.R.layout.simple_list_item_1,
//                                listaTiposGasto);
//
//                        spinnerTipoGasto.setAdapter(spinnerAdapter);
//                        spinnerTipoGasto.setSelection(posicaoTipoGastoId(idTipoGasto));
//                    }
//                });
//            }
//        });
//    }
    private void carregaTipos(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GastosDatabase database = GastosDatabase.getDatabase(GastoActivity.this);

                listaTipos = database.tipoDao().queryAll();

                GastoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Tipo> spinnerAdapter = new ArrayAdapter<>(GastoActivity.this,
                                android.R.layout.simple_list_item_1,
                                listaTipos);

                        spinnerTipoGasto.setAdapter(spinnerAdapter);
                    }
                });
            }
        });
    }



    private void salvar(){

        final String valor  = editTextValor.getEditableText().toString();
        final Tipo tipoGasto = (Tipo) spinnerTipoGasto.getSelectedItem();
        if (tipoGasto == null){
            UtilGUI.avisoErro(this, R.string.tipo_contato_vazio);
            return;
        }
        if (tiposUsados != null){
            for (int cont = 0; cont < tiposUsados.length; cont++){
                if (tipoGasto.getId() == tiposUsados[cont] &&
                    valor.equalsIgnoreCase(valoresUsados[cont])){
                    UtilGUI.avisoErro(this, R.string.contato_valor_repetido);
                    return;
                }
            }
        }
        final Intent intent = new Intent();
        intent.putExtra(MODO,  modo);
        intent.putExtra(ID_CONTATO, idGasto);
        intent.putExtra(VALOR, valor);
        intent.putExtra(ID_TIPO_CONTATO, tipoGasto.getId());

        gastoObj.setTipoId(tipoGasto.getId());
        gastoObj.setValor(Double.parseDouble(valor));
        gastoObj.setTipoGastoS(tipoGasto.getDescricao());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GastosDatabase database = GastosDatabase.getDatabase(GastoActivity.this);
                if (modo == NOVO) {

                    database.gastosDao().insert(gastoObj);
                }
                if (modo == ALTERAR)
                {
                    gastoObj.setId(idGasto);

                    database.gastosDao().update(gastoObj);
                }
                setResult(Activity.RESULT_OK);
            }
        });
        finish();
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicao_detalhes, menu);

        menu.getItem(0).setIcon(R.drawable.ic_check_mark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemCancelar:
            case android.R.id.home:
                cancelar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

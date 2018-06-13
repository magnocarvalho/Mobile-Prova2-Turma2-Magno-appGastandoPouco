package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Gasto;

import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util.UtilGUI;

public class PrincipalActivity extends AppCompatActivity {

    private static final int REQUEST_NOVA_PESSOA    = 1;
    private static final int REQUEST_ALTERAR_PESSOA = 2;

    private ListView listViewPessoa;
    private ArrayAdapter<Gasto> listaAdapter;
    private List<Gasto> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_princiapal);

        listViewPessoa = findViewById(R.id.listViewIt);

        listViewPessoa.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Gasto pessoa = (Gasto) parent.getItemAtPosition(position);

                GastoActivity.alterar(PrincipalActivity.this,
                        REQUEST_ALTERAR_PESSOA,
                        pessoa);
            }
        });

        carregaGastos();

        registerForContextMenu(listViewPessoa);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == REQUEST_NOVA_PESSOA || requestCode == REQUEST_ALTERAR_PESSOA)
                && resultCode == Activity.RESULT_OK){

            carregaGastos();
        }
    }
    private void carregaGastos(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GastosDatabase database = GastosDatabase.getDatabase(PrincipalActivity.this);

                lista = database.gastosDao().queryAll();

                PrincipalActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaAdapter = new ArrayAdapter<>(PrincipalActivity.this,
                                android.R.layout.simple_list_item_1,
                                lista);

                        listViewPessoa.setAdapter(listaAdapter);
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_pessoas, menu);
        return true;
    }
    private void excluirGasto(final Gasto pessoa){

        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + pessoa.getId() +  "\n" + pessoa.getValor();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        GastosDatabase database =
                                                GastosDatabase.getDatabase(PrincipalActivity.this);

                                        database.gastosDao().delete(pessoa);

                                        PrincipalActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                listaAdapter.remove(pessoa);
                                            }
                                        });
                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilGUI.confirmaAcao(this, mensagem, listener);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemNovo:
                verificaTipos();
                return true;

            case R.id.menuItemTipos:
                TiposActivity.abrir(this);
                return true;


            case R.id.menuItemSobre:
                Sobre.abrir(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void verificaTipos(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GastosDatabase database = GastosDatabase.getDatabase(PrincipalActivity.this);

                int total = database.tipoDao().total();

                if (total == 0){

                    PrincipalActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilGUI.avisoErro(PrincipalActivity.this, R.string.nenhum_tipo);
                        }
                    });

                    return;
                }


                total = database.tipoGastoDao().total();

                if (total == 0){

                    PrincipalActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilGUI.avisoErro(PrincipalActivity.this, R.string.nenhum_tipo_contato);
                        }
                    });

                    return;
                }

                GastoActivity.novo(PrincipalActivity.this, REQUEST_NOVA_PESSOA);

            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Gasto pessoa = (Gasto) listViewPessoa.getItemAtPosition(info.position);

        switch(item.getItemId()){

            case R.id.menuItemAbrir:
                GastoActivity.alterar(this,
                        REQUEST_ALTERAR_PESSOA,
                        pessoa);
                return true;

            case R.id.menuItemApagar:
                excluirGasto(pessoa);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public static void abrir(Activity activity){

        Intent intent = new Intent(activity, PrincipalActivity.class);

        activity.startActivity(intent);
    }
}

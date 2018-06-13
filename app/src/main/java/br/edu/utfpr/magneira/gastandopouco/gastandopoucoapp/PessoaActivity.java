package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Database;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Gasto;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Pessoa;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.Tipo;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.model.TipoGasto;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.persistencia.GastosDatabase;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util.UtilString;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util.UtilGUI;
import br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util.UtilsDate;



public class PessoaActivity extends AppCompatActivity {

    private static final int REQUEST_NOVO_Gasto    = 1;
    private static final int REQUEST_ALTERAR_Gasto = 2;

    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText    editTextNome;
    private EditText    editTextDataNascimento;
    private Spinner     spinnerTipo;
    private List<Tipo>  listaTipos;

    private ListView              listViewGastos;
    private List<Gasto>         listaGastos;
    private List<Gasto>         listaGastosRemovidos;
    private ArrayAdapter<Gasto> listaAdapter;

//    private TextView textViewDataCadastro;
//    private EditText editTextDataCadastro;

    private int      modo;
    private Pessoa   pessoa;
    private Calendar calendarDataNascimento;

    private int heightMinListViewGastos;

    private List<TipoGasto> listaTiposGasto;

    public static void nova(Activity activity, int requestCode){

        Intent intent = new Intent(activity, PessoaActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Pessoa pessoa){

        Intent intent = new Intent(activity, PessoaActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, pessoa.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoa);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTextNome           = findViewById(R.id.editTextNome);
//        editTextDataNascimento = findViewById(R.id.editTextDataNascimento);
        spinnerTipo            = findViewById(R.id.spinnerTipo);
         listViewGastos       = findViewById(R.id.listViewContatos);
//        textViewDataCadastro = findViewById(R.id.textViewDataCadastro);
//        editTextDataCadastro = findViewById(R.id.editTextDataCadastro);
//
//        editTextDataCadastro.setEnabled(false);

        listViewGastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Gasto gasto = (Gasto) parent.getItemAtPosition(position);

                GastoActivity.alterar(PessoaActivity.this,
                                        REQUEST_ALTERAR_Gasto,
                                        gasto,
                                        listaGastos);
            }
        });

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        heightMinListViewGastos = getResources().getInteger(R.integer.altura_min_lista_contatos);

        modo = bundle.getInt(MODO, NOVO);

        listaGastosRemovidos = new ArrayList<>();
        listaTiposGasto      = new ArrayList<>();

//        calendarDataNascimento = Calendar.getInstance();
//        calendarDataNascimento.add(Calendar.YEAR, -
//                                   getResources().getInteger(R.integer.anos_para_tras));

        carregaTipos();

        registerForContextMenu(listViewGastos);

//        editTextDataNascimento.setFocusable(false);
//        editTextDataNascimento.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                /* Para fazer o DatePicker aparecer em modo Spinner e não Calendar
//                   para SDKs iguais ou maiores que o API Level 21 foi utilizado um
//                   estilo customizado, que está na pasta values-v21.
//
//                   Versões anteriores já aparecem em modo Spinner por padrão.
//                 */
//                DatePickerDialog picker = new DatePickerDialog(PessoaActivity.this,
//                        R.style.CustomDatePickerDialogTheme,
//                        PessoaActivity.this,
//                        calendarDataNascimento.get(Calendar.YEAR),
//                        calendarDataNascimento.get(Calendar.MONTH),
//                        calendarDataNascimento.get(Calendar.DAY_OF_MONTH));
//
//                picker.show();
//            }
//        });

        if (modo == ALTERAR){

            setTitle(R.string.alterar_pessoa);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int id = bundle.getInt(ID);

                    GastosDatabase database = GastosDatabase.getDatabase(PessoaActivity.this);

                    pessoa = database.pessoaDao().queryForId(id);

                    listaGastos = database.gastosDao().queryForPessoaId(pessoa.getId());

                    for (Gasto c : listaGastos){

                        TipoGasto tipoGasto = getTipoGasto(c.getTipoContatoId());

                        if (tipoGasto == null){
                            tipoGasto = database.tipoGastoDao().queryForId(c.getTipoContatoId());
                            listaTiposGasto.add(tipoGasto);
                        }

                        c.setTipoGasto(tipoGasto);
                    }

                    Collections.sort(listaGastos, Gasto.comparador);

                    PessoaActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            editTextNome.setText(pessoa.getNome());

//                            calendarDataNascimento.setTime(pessoa.getDataNascimento());

//                            String textoData = UtilsDate.formatDate(PessoaActivity.this,
//                                                                    pessoa.getDataNascimento());

//                            editTextDataNascimento.setText(textoData);

                            int posicao = posicaoTipo(pessoa.getTipoId());
                            spinnerTipo.setSelection(posicao);

//                            textoData = UtilsDate.formatDate(PessoaActivity.this,
//                                                             pessoa.getDataCadastro());

//                            editTextDataCadastro.setText(textoData);

                            criaAdapterListGastos();

                            UtilGUI.setListViewHeightBasedOnChildren(listViewGastos,
                                                                      heightMinListViewGastos);
                        }
                    });
                }
            });

        }else{

            setTitle(R.string.nova_pessoa);

            pessoa = new Pessoa("");

            listaGastos = new ArrayList<>();

            criaAdapterListGastos();

//            textViewDataCadastro.setVisibility(View.INVISIBLE);
//            editTextDataCadastro.setVisibility(View.INVISIBLE);
        }
    }

    private int posicaoTipo(int tipoId){

        for (int pos = 0; pos < listaTipos.size(); pos++){

            Tipo t = listaTipos.get(pos);

            if (t.getId() == tipoId){
                return pos;
            }
        }

        return -1;
    }

    private void carregaTipos(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GastosDatabase database = GastosDatabase.getDatabase(PessoaActivity.this);

                listaTipos = database.tipoDao().queryAll();

                PessoaActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Tipo> spinnerAdapter = new ArrayAdapter<>(PessoaActivity.this,
                                android.R.layout.simple_list_item_1,
                                listaTipos);

                        spinnerTipo.setAdapter(spinnerAdapter);
                    }
                });
            }
        });
    }

    private TipoGasto getTipoGasto(int tipoId){

        for (TipoGasto t : listaTiposGasto){

            if (t.getId() == tipoId){
                return t;
            }
        }

        return null;
    }

    private Gasto getGasto(int tipoId){

        for (Gasto c : listaGastos){

            if (c.getId() == tipoId){
                return c;
            }
        }

        return null;
    }

    private void criaAdapterListGastos(){

        listaAdapter = new ArrayAdapter<>(PessoaActivity.this,
                android.R.layout.simple_list_item_1,
                listaGastos);

        listViewGastos.setAdapter(listaAdapter);
    }

    private void salvar(){
        String nome  = UtilGUI.validaCampoTexto(this,
                                                 editTextNome,
                                                 R.string.nome_vazio);
        if (nome == null){
            return;
        }

//        String txtDataNascimento = UtilGUI.validaCampoTexto(this,
//                                                             editTextDataNascimento,
//                                                             R.string.data_nascimento_vazia);
//        if (txtDataNascimento == null){
//            return;
//        }

//        int idade = UtilsDate.totalAnos(calendarDataNascimento);


        pessoa.setNome(nome);
//        pessoa.setDataNascimento(calendarDataNascimento.getTime());

        Tipo tipo = (Tipo) spinnerTipo.getSelectedItem();
        if (tipo != null){
            pessoa.setTipoId(tipo.getId());
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                GastosDatabase database = GastosDatabase.getDatabase(PessoaActivity.this);

                if (modo == NOVO) {

//                    pessoa.setDataCadastro(new Date());

                    int novoId = (int) database.pessoaDao().insert(pessoa);

                    pessoa.setId(novoId);

                } else {

                    database.pessoaDao().update(pessoa);
                }

                for (Gasto c : listaGastosRemovidos){

                    if (c.getId() != 0){
                        database.gastosDao().delete(c);
                    }
                }

                for (Gasto c : listaGastos){

                    if (c.getPessoaId() != pessoa.getId()){
                        c.setPessoaId(pessoa.getId());
                    }

                    if (c.getId() == 0){
                        database.gastosDao().insert(c);
                    }else{
                        database.gastosDao().update(c);
                    }
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    public void novoGasto(View view){
        GastoActivity.novo(this, REQUEST_NOVO_Gasto, listaGastos);
    }

    private void excluirGasto(final Gasto Gasto){

        String mensagem = getString(R.string.deseja_realmente_apagar)
                          + "\n" + Gasto.getValor();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                listaGastos.remove(Gasto);
                                listaGastosRemovidos.add(Gasto);

                                UtilGUI.setListViewHeightBasedOnChildren(listViewGastos,
                                                                         heightMinListViewGastos);

                                listaAdapter.notifyDataSetChanged();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilGUI.confirmaAcao(this, mensagem, listener);
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK){

            Bundle bundle = data.getExtras();

            int idGasto     = bundle.getInt(GastoActivity.ID_CONTATO);
            int idTipoGasto = bundle.getInt(GastoActivity.ID_TIPO_CONTATO);
            String valor      = bundle.getString(GastoActivity.VALOR);

            final Gasto Gasto;

            if (requestCode == REQUEST_NOVO_Gasto) {

                Gasto = new Gasto();

                listaGastos.add(Gasto);

            } else {

                Gasto = getGasto(idGasto);
            }

            Gasto.setValor(Double.parseDouble(valor));
            Gasto.setTipoContatoId(idTipoGasto);


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    GastosDatabase database = GastosDatabase.getDatabase(PessoaActivity.this);

                    for (Gasto c : listaGastos){

                        TipoGasto tipoGasto = getTipoGasto(c.getTipoContatoId());

                        if (tipoGasto == null){
                            tipoGasto = database.tipoGastoDao().queryForId(c.getTipoContatoId());
                            listaTiposGasto.add(tipoGasto);
                        }

                        c.setTipoGasto(tipoGasto);
                    }

                    Collections.sort(listaGastos, Gasto.comparador);

                    PessoaActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilGUI.setListViewHeightBasedOnChildren(listViewGastos,
                                                                      heightMinListViewGastos);

                            listaAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        }
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

        Gasto gasto = (Gasto) listViewGastos.getItemAtPosition(info.position);

        switch(item.getItemId()){

            case R.id.menuItemAbrir:
                GastoActivity.alterar(this,
                        REQUEST_ALTERAR_Gasto,
                        gasto,
                        listaGastos);
                return true;

            case R.id.menuItemApagar:
                excluirGasto(gasto);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
////        calendarDataNascimento.set(year, month, dayOfMonth);
//
////        String textoData = UtilsDate.formatDate(this, calendarDataNascimento.getTime());
//
////        editTextDataNascimento.setText(textoData);
//    }
}

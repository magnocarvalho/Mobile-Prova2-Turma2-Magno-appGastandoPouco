package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

public class Sobre extends AppCompatActivity {

    private static final String ARQUIVO = "preferencias_cor";
    private static final String COR     = "COR";

    private int opcao = Color.BLUE;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        text = findViewById(R.id.textAluno);
    }
    private void mudaCorFundo(){
        text.setTextColor(opcao);
    }

    private void lerPreferenciaCor(){
        SharedPreferences shared =
                getSharedPreferences(ARQUIVO,
                        Context.MODE_PRIVATE);

        opcao = shared.getInt(COR, opcao);

        mudaCorFundo();
    }
    private void salvarPreferenciaCor(int novoValor){

        SharedPreferences shared =
                getSharedPreferences(ARQUIVO,
                        Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(COR, novoValor);

        editor.commit();

        opcao = novoValor;

        mudaCorFundo();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sobre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuPrincipal:
                PrincipalActivity.abrir(this);
                return true;

            case R.id.menuItemTipos:
                TiposActivity.abrir(this);
                return true;

            case R.id.menuItemSobre:
                    return true;
            case R.id.menuItemAzul:
                salvarPreferenciaCor(Color.BLUE);
                return true;
            case R.id.menuItemVermelho:
                salvarPreferenciaCor(Color.RED);
                return true;
            case R.id.menuItemAmarelo:
                salvarPreferenciaCor(Color.YELLOW);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch(opcao){
            case Color.BLUE:
                menu.getItem(0).setChecked(true);
                return true;

            case Color.RED:
                menu.getItem(1).setChecked(true);
                return true;

            case Color.YELLOW:
                menu.getItem(2).setChecked(true);
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.item_selecionado, menu);
    }
    public static void abrir(Activity activity){

        Intent intent = new Intent(activity, Sobre.class);

        activity.startActivity(intent);
    }

}

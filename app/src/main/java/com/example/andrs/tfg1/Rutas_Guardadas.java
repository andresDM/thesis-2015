package com.example.andrs.tfg1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;



public class Rutas_Guardadas extends ActionBarActivity {

    static ArrayList<String> asd=new ArrayList<>();
    private String[] datos=new String[]{};
    private TextView eliminar;
    private SharedPreferences preferencias;
    ArrayList<String> rutas;
    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas2);



       preferencias=getSharedPreferences("mispreferencias",MODE_PRIVATE);

        ListView ListadoRutas=(ListView)findViewById(R.id.ListadoRutas);

        //Colección de objetos. keySet nos da una colección de las keys(nombres)    Constructor ArrayList(Collection<? extends E> collection) crea una instancia conteniendo los elementos de la colección devuelta por keySet

        rutas=new ArrayList<String>(preferencias.getAll().keySet());


        adaptador= new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,rutas);

        ListadoRutas.setAdapter(adaptador);


        registerForContextMenu(ListadoRutas);

        ListadoRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Rutas_Guardadas.this, MapsActivity.class);
                intent.putExtra("Cargar ruta", true);
                intent.putExtra("Nombre ruta", adaptador.getItem(position));
                startActivity(intent);
            }
        });


    }
    String claveRuta;
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rutas, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        claveRuta=adaptador.getItem(info.position);

        /**Toast.makeText(getBaseContext(),
                 claveRuta, Toast.LENGTH_LONG)
                .show();*/

        switch (item.getItemId()) {
            case R.id.ContextEliminar:
                SharedPreferences.Editor editor=preferencias.edit();
                editor.remove(claveRuta);
                editor.commit();
                finish();
                startActivity(getIntent());

                return true;
            case R.id.ContextCompartir:

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }




}

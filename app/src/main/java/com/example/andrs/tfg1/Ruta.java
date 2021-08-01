package com.example.andrs.tfg1;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;


/**
 * Created by Andres on 23/05/2015.
 */
public class Ruta {

    private Context contexto;
    private int i;
    //private ArrayList<LatLng> coordenadas=new ArrayList<LatLng>();
    private String cadena_de_coordenadas;
    private String coordenada;



    public Ruta(String nombre,Context contexto,ArrayList<Location> Location) {
        this.contexto = contexto;

        if (Location.size()!=0) {
            Gson gson=new Gson();
            String formatoJSON=gson.toJson(Location);


            SharedPreferences preferencias=contexto.getSharedPreferences("mispreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= preferencias.edit();
            editor.putString(nombre,formatoJSON);
            editor.commit();
        }
        else{

            Toast.makeText(contexto,
                    "No existen coordenadas", Toast.LENGTH_LONG)
                    .show();
        }




        /**
        for(i=0;i<Location.size();i++){
            Location.get(i).toString();
        }*/

            /**
            try {
                OutputStreamWriter fichero = new OutputStreamWriter(contexto.openFileOutput(nombre, Context.MODE_PRIVATE));
                fichero.write(nombre);
                fichero.close();
                Rutas_Guardadas.asd.add(nombre);
            } catch (Exception exception) {

                Log.e("Ficheros", "Error al escribir el fichero");
            }/**
           try{
               BufferedReader reader=new BufferedReader(new InputStreamReader(contexto.openFileInput(nombre)));
               String text=reader.readLine();
               reader.close();
               Toast.makeText(contexto,
                       text, Toast.LENGTH_LONG)
                       .show();
           }
           catch(Exception ex){
               Log.e("Ficheros","Error al leer fichero de memoria interna");

           }*/

    }



    public Ruta(){}

}













package com.example.andrs.tfg1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.AlertDialog;



import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Button playBtn;
    private Button stopBtn;
    private Button borrarBtn;


    private TextView nombre;
    private EditText nombretxt;
    private Boolean cargar_ruta;
    private SharedPreferences preferences;


    private ArrayList<Location> listGeopoints = new ArrayList<Location>();
    int i=0;
    int j=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        cargar_ruta=getIntent().getBooleanExtra("Cargar ruta", false);
        setUpMapIfNeeded();

        if(cargar_ruta){
            preferences=getSharedPreferences("mispreferencias", MODE_PRIVATE);

            String rutaJSON=new String(preferences.getString(getIntent().getStringExtra("Nombre ruta"), "Fallo al cargar la ruta"));

            Type tipoObjeto= new TypeToken<ArrayList <Location>>(){}.getType();
            Gson gson=new Gson();
            ArrayList<Location> coordenadas=gson.fromJson(rutaJSON,tipoObjeto);


            for(j=0;j<coordenadas.size();j++){
                if(j>0) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(coordenadas.get(j - 1).getLatitude(), coordenadas.get(j - 1).getLongitude()), new LatLng(coordenadas.get(j).getLatitude(), coordenadas.get(j).getLongitude()))
                            .width(5)
                            .color(Color.RED));
                }
            }

            LatLng location=new LatLng(coordenadas.get(0).getLatitude(),coordenadas.get(0).getLongitude());
            CameraUpdate asd=CameraUpdateFactory.newLatLngZoom(location,17);
            mMap.animateCamera(asd);


        }


        playBtn=(Button)findViewById(R.id.PlayBtn);
        stopBtn=(Button)findViewById(R.id.StopBtn);
        borrarBtn=(Button)findViewById(R.id.BorrarBtn);


        // Asigno una vista al mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Activo mi localizacion en el mapa
        if(cargar_ruta==false) {
            LatLng location = new LatLng(0, 0);
            CameraUpdate posicion = CameraUpdateFactory.newLatLng(location);
            mMap.animateCamera(posicion);
        }
        // Asigno un nivel de zoom


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.setMyLocationEnabled(true);

                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    public void onMyLocationChange(Location pos) {
                        // TODO Auto-generated method stub

                        listGeopoints.add(i, pos);

                        DecimalFormat f = new DecimalFormat("###.#####");
                        String lat = f.format(pos.getLatitude()).replace(",", ".");
                        String lon = f.format(pos.getLongitude()).replace(",", ".");

                        double alt = pos.getAltitude();
                        double error = pos.getAccuracy();
                        double vel = pos.getSpeed();

                        // Muevo la camara a mi posicion usando Lat y Lon pasando la lat y long a tipo double

                        CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), 14);
                        mMap.animateCamera(cam);

                        if(i>0) {
                            mMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(listGeopoints.get(i - 1).getLatitude(), listGeopoints.get(i - 1).getLongitude()), new LatLng(listGeopoints.get(i).getLatitude(), listGeopoints.get(i).getLongitude()))
                                    .width(5)
                                    .color(Color.RED));
                        }

                        // Notifico con un mensaje al usuario de su Lat y Lon

                        Toast.makeText(MapsActivity.this,
                                "Lat: " + lat + "\nLon: " + lon + "\nAlt: " + alt + "\nError: " + error + "\nVelocidad: " + vel+"i: "+i, Toast.LENGTH_LONG)
                                    .show();
                        i++;
                        }
                    });

            }
        });


        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    mMap.setMyLocationEnabled(false);
                }
            });

        borrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                listGeopoints.clear();
                i = 0;
            }
        });


    }


    public void funcionIniciarAlertDialog(View v){

        String infService=Context.LAYOUT_INFLATER_SERVICE;

        LayoutInflater li=(LayoutInflater)getApplicationContext().getSystemService(infService);

        View inflador=li.inflate(R.layout.menu_contextual,null);

       nombre=(TextView)inflador.findViewById(R.id.nombreTxt);
       nombretxt=(EditText)inflador.findViewById(R.id.editNombre);


        AlertDialog.Builder guardarmenu=new AlertDialog.Builder(this);
        guardarmenu.setTitle("Indica nombre de ruta");

        guardarmenu.setView(inflador);

        guardarmenu.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int boton) {

                Ruta ruta = new Ruta(nombretxt.getText().toString(), MapsActivity.this,listGeopoints);

            }
        });
        guardarmenu.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int boton) {
            }
        });

        guardarmenu.show();

    }

    public void iniciarRutasGuardadas(View view){
        Intent intent=new Intent(this,Rutas_Guardadas.class);
        startActivity(intent);
    }




    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }



    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }



    private void setUpMap() {
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

}

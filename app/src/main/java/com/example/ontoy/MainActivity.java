package com.example.ontoy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap mapin;
    Marker marker;
    LocationBroadcastReciever reciever;
    double latitud=0;
    double longitud=0;
    double latitud2=0;
    double longitud2=0;
    int earth_Radius = 6371000;
    double latENES=19.650321;
    double longENES=-101.223360;


    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        reciever = new LocationBroadcastReciever();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            startService();

        }
        else{

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaF);
        mapFragment.getMapAsync(this );
    }

    void startService (){
        IntentFilter filter = new IntentFilter("LOCALIZAR");
        registerReceiver(reciever, filter);
        Intent intent = new Intent(MainActivity.this, Localizar.class);
        startService(intent);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    startService();
                }
                else{
                    Toast.makeText(this,"La aplicación necesita permiso para funcionar.",Toast.LENGTH_LONG).show();
                }


        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapin=googleMap;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(reciever);
    }

    public void onClick(View view) {
        double latrad= latitud * Math.PI/180;
        double latrad2 = latitud2 * Math.PI/180;
        double latdif = (latitud2-latitud) * Math.PI/180;
        double longdif = (longitud2-longitud) * Math.PI/180;

        double a = Math.sin(latdif/2) * Math.sin(latdif/2) +
                Math.cos(latrad) * Math.cos(latrad2) *
                        Math.sin(longdif/2) * Math.sin(longdif/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double velocidad= earth_Radius * c;

        latrad2 = latENES * Math.PI/180;
        latdif = (latENES-latitud) * Math.PI/180;
        longdif = (longENES-longitud) * Math.PI/180;

        a = Math.sin(latdif/2) * Math.sin(latdif/2) +
                Math.cos(latrad) * Math.cos(latrad2) *
                        Math.sin(longdif/2) * Math.sin(longdif/2);
        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earth_Radius * c;

        velocidad=velocidad/10;
        if (velocidad>0.1) {
            velocidad=velocidad*3600;
            double tiempo = dist / velocidad;
            if (tiempo<1e-5){
                Toast.makeText(MainActivity.this,"Espera unos segundos...",Toast.LENGTH_LONG).show();
            }
            else {
                long horas = Math.round(Math.floor(tiempo));
                long minutos = Math.round(Math.floor((tiempo - horas) * 60));
                long segundos = Math.round(Math.floor((((tiempo - horas) * 60) - minutos) * 60));
                dist = Math.round(dist);
                dist = dist / 1000;

                Toast.makeText(MainActivity.this, "Estas a " + dist + " kilómetros.", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "A este paso llegas a la ENES en aproximadamente " + horas + " horas " + minutos + " minutos " + segundos + " segundos", Toast.LENGTH_LONG).show();
            }
        }
        else{
            dist = Math.round(dist);
            dist = dist / 1000;
            Toast.makeText(MainActivity.this,"Si no te mueves nunca llegaras! Estas a "+dist+" kilómetros de la ENES!",Toast.LENGTH_LONG).show();

        }
        return;


}

    public class LocationBroadcastReciever extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("LOCALIZAR")){
                latitud2=latitud;
                longitud2=longitud;
                latitud = intent.getDoubleExtra("Latitud",0f);
                longitud = intent.getDoubleExtra("Longitud",0f);
                if(mapin!=null){
                    LatLng latLng=new LatLng(latitud,longitud);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    if (marker!=null){
                        marker.setPosition(latLng);
                    }
                    else{
                        marker=mapin.addMarker(markerOptions);
                    }

                    //Toast.makeText(MainActivity.this,"Latitud: "+latitud+" Longitud: "+longitud,Toast.LENGTH_LONG).show();

                }

            }
        }
    }
}
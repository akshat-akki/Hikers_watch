package com.example.hikerawatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView lati;
    TextView longi;
    TextView adres;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void updateLocation(Location location)
    {
        lati.setText(Double.toString(location.getLatitude()));
        longi.setText(Double.toString(location.getLongitude()));
        String add="";
        Geocoder geo=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> l = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(l!=null && l.size()>0)
            {
                if(l.get(0).getThoroughfare()!=null)
                {
                    add+=l.get(0).getThoroughfare()+"\n";
                }
                if(l.get(0).getLocality()!=null)
                {
                    add+=l.get(0).getLocality()+" ";
                }
                if(l.get(0).getPostalCode()!=null)
                {
                    add+=l.get(0).getPostalCode()+" ";
                }
                if(l.get(0).getAdminArea()!=null)
                {
                    add+=l.get(0).getAdminArea()+" ";
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(add.length()<=0)
            add="Address not found :(";
        adres.setText(add);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lati=findViewById(R.id.lati);
        longi=findViewById(R.id.longi);
        adres=findViewById(R.id.address);
        locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",Double.toString(location.getLatitude()));
                updateLocation(location);
              }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnown=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnown!=null)
            {
                Log.i("Location",Double.toString(lastKnown.getLatitude()));
                updateLocation(lastKnown);
            }

        }
    }
}
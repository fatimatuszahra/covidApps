package com.example.covidapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class shoMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double getLat, getLong;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getRefenence;
    private String getKey = "";
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sho_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getDatabase = FirebaseDatabase.getInstance();
        getRefenence = getDatabase.getReference();


        Intent myIntent = getIntent();
        String firstKeyName = myIntent.getStringExtra("username");
        final String qrCode = myIntent.getStringExtra("qrCode");
        if(!firstKeyName.isEmpty()) {
            Log.i("firstKeyName", firstKeyName);
            Log.i("qrCode", qrCode);
            getKey = firstKeyName;
            getDatabase = FirebaseDatabase.getInstance();
            getRefenence = getDatabase.getReference();

            getRefenence.child("covidApps").child("korban").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        LatLng newLoc;
                        CovidClass myCov = postSnapshot.getValue(CovidClass.class);

                        if(myCov.getUserName().equals(getKey))
                        {
                            newLoc = new LatLng(Double.valueOf(myCov.getLat()), Double.valueOf(myCov.getLon()));
                            mMap.addMarker(new MarkerOptions().position(newLoc).title(myCov.getUserName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red));
                            //marker.setPosition(newLoc);
                        }
                        else if(myCov.getCode().equals(qrCode))
                        {
                            newLoc = new LatLng(Double.valueOf(myCov.getLat()), Double.valueOf(myCov.getLon()));
                            mMap.addMarker(new MarkerOptions().position(newLoc).title(myCov.getUserName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.man));
                        }
                        else
                        {
                            newLoc = new LatLng(Double.valueOf(myCov.getLat()), Double.valueOf(myCov.getLon()));
                            mMap.addMarker(new MarkerOptions().position(newLoc).title(myCov.getUserName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rec));
                            //marker.setPosition(newLoc);
                        }

                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc ));
                        //mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng baseLocation = new LatLng(-7.9879006, 112.7032945);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(baseLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(baseLocation,18f));
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);

        //mMap.getUiSettings().setZoomGesturesEnabled(false);


    }
}

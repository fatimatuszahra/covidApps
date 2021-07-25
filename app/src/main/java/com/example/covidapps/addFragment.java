package com.example.covidapps;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import static android.widget.Toast.LENGTH_SHORT;


/**
 * A simple {@link Fragment} subclass.
 */
public class addFragment extends Fragment {


    private SupportMapFragment mSupportMapFragment;
    private Button btnAdd;
    private EditText userName, kode, keterangan;
    private Double getLat, getLong;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getRefenence;
    public addFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
        }
        if (mSupportMapFragment != null)
        {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override public void onMapReady(final GoogleMap googleMap) {
                    if (googleMap != null) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Tambahkan Makam?");


                        //googleMap.getUiSettings().setAllGesturesEnabled(true);
                        googleMap.getUiSettings().setZoomGesturesEnabled(false);


                        LatLng baseLocation = new LatLng(-7.9879006, 112.7032945);
                        //googleMap.addMarker(new MarkerOptions().position(baseLocation).title("center")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rec));

                        getDatabase = FirebaseDatabase.getInstance();
                        getRefenence = getDatabase.getReference();

                        getRefenence.child("covidApps").child("korban").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                    CovidClass myCov = postSnapshot.getValue(CovidClass.class);
                                    LatLng newLoc = new LatLng(Double.valueOf(myCov.getLat()), Double.valueOf(myCov.getLon()));
                                    googleMap.addMarker(new MarkerOptions().position(newLoc).title(myCov.getUserName())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rec));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        CameraPosition cameraPosition = new CameraPosition.Builder().target(baseLocation).zoom(18.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);


                        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                            @Override
                            public void onMapLongClick(final LatLng latLng) {

                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getLat = latLng.latitude;
                                        getLong = latLng.longitude;
                                        googleMap.addMarker(new MarkerOptions()
                                                .position(latLng).title("Your Location Here"))
                                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rec));

                                    }
                                });
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }
                        });
                    }

                }
            });
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAdd = (Button)view.findViewById(R.id.btnPush);
        userName = (EditText)view.findViewById(R.id.userName);
        kode = (EditText) view.findViewById(R.id.kode);
        keterangan = (EditText) view.findViewById(R.id.keterangan);
        getDatabase = FirebaseDatabase.getInstance();
        getRefenence = getDatabase.getReference();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String myCode = kode.getText().toString();
                String myAdditional = keterangan.getText().toString();
                String myLat = getLat.toString();
                String myLon = getLong.toString();
                getRefenence.child("covidApps").child("korban").child(user).child("userName").setValue(user);
                getRefenence.child("covidApps").child("korban").child(user).child("code").setValue(myCode);
                getRefenence.child("covidApps").child("korban").child(user).child("additional").setValue(myAdditional);
                getRefenence.child("covidApps").child("korban").child(user).child("lat").setValue(myLat);
                getRefenence.child("covidApps").child("korban").child(user).child("lon").setValue(myLon);

                Toast.makeText(getActivity(),"Success", LENGTH_SHORT).show();

                userName.setText("");
                kode.setText("");
                keterangan.setText("");
                getLat = 0.0;
                getLong = 0.0;

            }
        });

    }
}

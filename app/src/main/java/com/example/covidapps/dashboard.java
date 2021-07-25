package com.example.covidapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.covidapps.MainActivity.CodeKey;
import static com.example.covidapps.MainActivity.MyPREFERENCES;
import static com.example.covidapps.MainActivity.NameKey;

public class dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnAdd, btnSearch, btnScan;
    private EditText search;
    //a List of type hero for holding list items
    List<CovidClass> covidList;

    //the listview
    ListView listView;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getRefenence;
    private EditText resultQr;
    private TextView userHeader;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        search = (EditText)findViewById(R.id.search);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnScan = (Button)findViewById(R.id.btnScan);
        resultQr = (EditText)findViewById(R.id.resultScan);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        View headerView = navigationView.getHeaderView(0);
        userHeader = (TextView) headerView.findViewById(R.id.userHeader);

        Intent myIntent = getIntent();
        if (myIntent.hasExtra("qrCode")) {
            String myQr = myIntent.getStringExtra("qrCode");
            if(!myQr.isEmpty()) {
                resultQr.setText(myQr);
            }
        } else {
            // Do something else
        }

        String mySessionName = sharedpreferences.getString(NameKey,"");
        if(!mySessionName.equals("admin"))
        {
            btnAdd.setVisibility(View.GONE);
        }
        userHeader.setText(mySessionName);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(dashboard.this, pindai.class);
                dashboard.this.startActivity(myIntent);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String korban = search.getText().toString();

                getRefenence.child("covidApps").child("korban").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(korban))
                        {
                            //Toast.makeText(getApplicationContext(), "Dapat", Toast.LENGTH_SHORT).show();
                            covidList.clear();
                            listView.setAdapter(null);
                            getRefenence.child("covidApps").child("korban").child(korban).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    CovidClass myCov = dataSnapshot.getValue(CovidClass.class);
                                    covidList.add(new CovidClass(myCov.getUserName(),myCov.getCode(), myCov.getLat(), myCov.getLon(), myCov.getAdditional()));
                                    //creating the adapter
                                    ListAdapter adapter = new ListAdapter(getApplicationContext(), R.layout.custom_list, covidList);

                                    //attaching adapter to the listview
                                    listView.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Tdk Dapat", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout lyt = (LinearLayout) findViewById(R.id.valueContent);
                lyt.removeAllViews();

                addFragment adding = new addFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.valueContent, adding).commit();
            }
        });

        //initializing objects
        covidList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);

        getDatabase = FirebaseDatabase.getInstance();
        getRefenence = getDatabase.getReference();

        getRefenence.child("covidApps").child("korban").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    CovidClass myCov = postSnapshot.getValue(CovidClass.class);
                    covidList.add(new CovidClass(myCov.getUserName(),myCov.getCode(), myCov.getLat(), myCov.getLon(), myCov.getAdditional()));
                    //creating the adapter
                    ListAdapter adapter = new ListAdapter(getApplicationContext(), R.layout.custom_list, covidList);

                    //attaching adapter to the listview
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_data) {
            LinearLayout lyt = (LinearLayout) findViewById(R.id.valueContent);
            lyt.removeAllViews();

            Intent myIntent = new Intent(dashboard.this, dashboard.class);
            dashboard.this.startActivity(myIntent);
            finish();
        } else if (id == R.id.nav_pencarian) {

        } else if (id == R.id.nav_logout) {
            SharedPreferences mSettings = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            mSettings.edit().clear();
            Intent myIntent = new Intent(dashboard.this, MainActivity.class);
            dashboard.this.startActivity(myIntent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

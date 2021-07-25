package com.example.covidapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText username, password;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getReference;
    private TextView btnRegister;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String NameKey = "nameKey";
    public static final String CodeKey = "codeKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        getDatabase = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();
        btnRegister = (TextView)findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, register.class);
                MainActivity.this.startActivity(myIntent);
                finish();
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myUsername = username.getText().toString();
                final String myPassword = password.getText().toString();
                Log.i("userName", myUsername);
                getReference.child("covidApps").child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(myUsername))
                        {
                            Log.i("validation", myUsername);
                            cekValidation(myUsername, myPassword);
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
    }
    public void cekValidation(final String myEMail, final String myPw){
        getReference.child("covidApps").child("users").child(myEMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LoginClass myData = dataSnapshot.getValue(LoginClass.class);
                String userCheck = myData.getUsername();
                String passCheck = myData.getPassword();
                if(myEMail.equals(userCheck) && myPw.equals(passCheck)){

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.putString(NameKey, myEMail);
                    editor.apply();
                    editor.commit();

                    Intent myIntent = new Intent(MainActivity.this, dashboard.class);
                    MainActivity.this.startActivity(myIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

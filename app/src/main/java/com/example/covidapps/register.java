package com.example.covidapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private TextView btnLogin;
    private EditText userName, password;
    private Button btnRegister;
    private FirebaseDatabase getDatabase;
    private DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnLogin = (TextView)findViewById(R.id.btnLogin);
        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        getDatabase = FirebaseDatabase.getInstance();
        getReference = getDatabase.getReference();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsername = userName.getText().toString();
                String getPassword = password.getText().toString();
                getReference.child("covidApps").child("users").child(getUsername).child("username").setValue(getUsername);
                getReference.child("covidApps").child("users").child(getUsername).child("password").setValue(getPassword);

                Intent myIntent = new Intent(register.this, MainActivity.class);
                register.this.startActivity(myIntent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(register.this, MainActivity.class);
                register.this.startActivity(myIntent);
                finish();
            }
        });

    }
}

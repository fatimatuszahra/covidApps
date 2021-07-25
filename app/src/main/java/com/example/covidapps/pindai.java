package com.example.covidapps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.example.covidapps.MainActivity.CodeKey;
import static com.example.covidapps.MainActivity.MyPREFERENCES;
import static com.example.covidapps.MainActivity.NameKey;

public class pindai extends AppCompatActivity {

    private IntentIntegrator qrScan;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindai);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.i("hasil", result.toString());
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                Log.i("hasil scan", result.getContents());

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(CodeKey, result.getContents());
                editor.apply();
                editor.commit();

                Intent myIntent = new Intent(pindai.this, dashboard.class);
                myIntent.putExtra("qrCode", result.getContents());
                pindai.this.startActivity(myIntent);
                finish();


            }
        } else {
            Log.i("result kosong", "oke");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

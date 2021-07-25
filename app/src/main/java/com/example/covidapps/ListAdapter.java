package com.example.covidapps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.example.covidapps.MainActivity.CodeKey;
import static com.example.covidapps.MainActivity.MyPREFERENCES;
import static com.example.covidapps.MainActivity.NameKey;


public class ListAdapter extends ArrayAdapter<CovidClass> {
    //the list values in the List of type hero
    List<CovidClass> covidList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    private FirebaseDatabase getDatabase;
    private DatabaseReference getRefenence;
    SharedPreferences sharedpreferences;

    public ListAdapter(@NonNull Context context, int resource, List<CovidClass> covidList) {
        super(context, resource, covidList);
        this.context = context;
        this.resource = resource;
        this.covidList = covidList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        TextView textViewName = view.findViewById(R.id.namaKorban);
        TextView textViewKet = view.findViewById(R.id.keteranganKorban);
        Button buttonEdit = view.findViewById(R.id.btnEdit);
        Button buttonDelete = view.findViewById(R.id.btnDel);
        Button buttonShow = view.findViewById(R.id.btnShow);

        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String mySessionName = sharedpreferences.getString(NameKey,"");
        if(!mySessionName.equals("admin"))
        {
            buttonEdit.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        }

        //getting the hero of the specified position
        CovidClass covClass = covidList.get(position);

        //adding values to the list item
        textViewName.setText(covClass.getUserName());
        textViewKet.setText(covClass.getAdditional());

        //adding a click listener to the button to remove item from the list
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                removeCov(position);
            }
        });
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoKorbanMaps(position);
            }
        });

        //finally returning the view
        return view;
    }
    private void gotoKorbanMaps(final int position)
    {
        String mySessionCode = sharedpreferences.getString(CodeKey,"");
        Log.i("session Code", mySessionCode);

        Intent myIntent = new Intent(getContext(), shoMap.class);
        myIntent.putExtra("username", covidList.get(position).userName);
        myIntent.putExtra("qrCode", mySessionCode);
        getContext().startActivity(myIntent);

    }
    //this method will remove the item from the list
    private void removeCov(final int position) {


        getDatabase = FirebaseDatabase.getInstance();
        getRefenence = getDatabase.getReference();

        String dataUserName = covidList.get(position).userName;
        getRefenence.child("covidApps").child("korban").child(dataUserName).removeValue();
        covidList.remove(position);
        notifyDataSetChanged();
    }
}

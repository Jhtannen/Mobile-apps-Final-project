package com.example.culpe.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivityCollege extends AppCompatActivity {
    String text;
    DatabaseHelper myDatabase;
    ArrayList<String> stateAbr = new ArrayList<>();
    ArrayList<String> city = new ArrayList<>();
    ArrayList<String> schoolName = new ArrayList<>();
    ArrayList<String> schoolUrl = new ArrayList<>();
    ArrayList<String> satAvg = new ArrayList<>();
    String dbname = "colleges.db";
    StringBuffer bufToPass = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_college);

        myDatabase = new DatabaseHelper(this);

       // addData();
        // Do not know why its adding data everytime it loads even when commented out so we did a work around that somehow works

    }
    public void search(View view)
    {
        EditText input = findViewById(R.id.input);
        String  toSearch = input.getText().toString();
        Cursor res;
        EditText min = findViewById(R.id.min);
        EditText max = findViewById(R.id.max);
        Switch s = findViewById(R.id.satScore);

        int minn = 0;
        int maxx = 0;


        if(s.isChecked())
        {
            minn = Integer.parseInt(min.getText().toString());
            maxx = Integer.parseInt(max.getText().toString());
            res = myDatabase.getDataWithSatAvg(toSearch,minn,maxx);
        }
        else {
             res = myDatabase.getDataNoSatAvg(toSearch);

        }
        //will be parsed from user-input
        StringBuffer buf = new StringBuffer();
        while(res.moveToNext())
        {
            buf.append("School Name:"+ res.getString(1)+"\n");
            buf.append("City:"+ res.getString(2)+"\n");
            buf.append("State:"+ res.getString(3)+"\n");
            buf.append("Website:"+ res.getString(4)+"\n");
            buf.append("Sat Average:"+ res.getString(5)+"\n\n");
        }
        bufToPass = buf;
        //sendMessage(view);
        showMessage("Schools",buf.toString());
    }
    public void showMessage(String Title,String Message)
    {
        final SpannableString s = new SpannableString(Message);
        Linkify.addLinks(s, Linkify.ALL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);

        builder.show();

    }
    public void addData()
    {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("stateAbr.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                    stateAbr.add(line);
                }
                reader.close();
            }
         catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("city.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                city.add(line);
            }
            reader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("schoolName.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                schoolName.add(line);
                Log.d("line:",line);
            }
            reader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("schoolUrl.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                schoolUrl.add(line);
            }
            reader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("satAvg.txt")));
            String line;
            while ((line = reader.readLine()) != null) {

                satAvg.add(line);
            }
            reader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        for(int i = 0; i < stateAbr.size();i++) {
            myDatabase.insertData(schoolName.get(i), city.get(i), stateAbr.get(i), schoolUrl.get(i), satAvg.get(i));
        }
    }

    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, display.class);
       // EditText editText = findViewById(R.id.input);
      //  String message = editText.getText().toString();

        Bundle bundle = new Bundle();
      //  bundle.putString("state",message);
        bundle.putString("data",bufToPass.toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
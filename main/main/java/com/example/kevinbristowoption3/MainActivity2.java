package com.example.kevinbristowoption3;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private EditText weightInput;
    private EditText goal;
    Switch optSwitch;
    MyDatabase database;
    static int goalWeight;
    private RecyclerView recyclerView;
    private WeightAdapter adapter;
    private ArrayList<WeightInput> weightList;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI elements
        weightInput = findViewById(R.id.weightInput);
        addButton = findViewById(R.id.addButton);
        optSwitch = findViewById((R.id.switch1));
        goal = findViewById(R.id.editTextNumber);
        database = new MyDatabase(this);
        goalWeight = database.getGoal(MainActivity.usr);
        goal.setText(String.valueOf(goalWeight));

        recyclerView = findViewById(R.id.weightRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weightList = new ArrayList<>();

        loadData();  // Load data into RecyclerView


        // Set state of SMS switch
        if (SmsResponse.smsOpt.equals("true")) {
            optSwitch.setChecked(true);
        } else {
            optSwitch.setChecked(false);
        }

        // Turn SMS on or off depending on switch state
        optSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optSwitch.isChecked()) {
                    database.getOptSms(MainActivity.usr, true);
                } else {
                    database.getOptSms(MainActivity.usr, false);
                }
            }
        });


    }

    private void loadData() {
        Cursor res = database.getAllData(MainActivity.usr);
        weightList.clear();

        if (res.getCount() == 0) {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add data to list
        while (res.moveToNext()) {
            //String user = res.getString(0);
            String date = res.getString(2);
            int weight = res.getInt(3);
            weightList.add(new WeightInput(date, weight));
        }

        adapter = new WeightAdapter(weightList, database);
        recyclerView.setAdapter(adapter);

    }

    public void clickAddGoal (View view) {
        String weightStr = goal.getText().toString();
        int weight = Integer.parseInt(weightStr);
        database.updateData(MainActivity.usr, weight);
        goalWeight = weight;
        goal.setText(weightStr);
    }

    public void clickAddWeight(View view) {
        String weightStr = weightInput.getText().toString();
        int weight = Integer.parseInt(weightStr);
        LocalDate today = LocalDate.now();  // Get the current date
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Log.d("Add Weight", formattedDate);

        database.addWeights(MainActivity.usr, weight, formattedDate, goalWeight);
        Log.d("Add Weight", "yooooo");
        //adapter.notifyDataSetChanged();
        Log.d("Add Weight", "hi");
        loadData();
        Log.d("Add Weight", "bye");
    }


}
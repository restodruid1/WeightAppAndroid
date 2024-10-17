package com.example.kevinbristowoption3;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private Button addButton;
    private Button addGoalButton;

    MyDatabase database;
    static int goalWeight;
    private RecyclerView recyclerView;
    private WeightAdapter adapter;
    private ArrayList<WeightInput> weightList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI elements
        weightInput = findViewById(R.id.weightInput);
        goal = findViewById(R.id.editTextNumber);
        database = new MyDatabase(this);
        goalWeight = database.getGoal(MainActivity.usr);
        addButton = findViewById(R.id.addButton);
        addGoalButton = findViewById(R.id.button4);
        goal.setText(String.valueOf(goalWeight));


        recyclerView = findViewById(R.id.weightRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weightList = new ArrayList<>();

        // Load data into RecyclerView
        loadData();

        // Ask permission for SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, 1);
        }

        addButton.setEnabled(false);
        addGoalButton.setEnabled(false);

        weightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    addButton.setEnabled(true);
                } else {
                    addButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    addGoalButton.setEnabled(true);
                } else {
                    addGoalButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loadData() {
        Cursor res = database.getAllData(MainActivity.usr);
        weightList.clear();

        if (res.getCount() == 0) {
            //Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
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

    // Write input goal weight to database
    public void clickAddGoal (View view) {
        String weightStr = goal.getText().toString();
        int weight = Integer.parseInt(weightStr);
        database.updateData(MainActivity.usr, weight);
        goalWeight = weight;
        Toast.makeText(this, "Goal Weight Added!", Toast.LENGTH_SHORT).show();
        goal.setText(weightStr);
        addGoalButton.setEnabled(false);
    }


    // Write input weight to database
    public void clickAddWeight(View view) {
        Toast.makeText(this, "Weight Added!", Toast.LENGTH_SHORT).show();

        String weightStr = weightInput.getText().toString();
        int weight = Integer.parseInt(weightStr);
        LocalDate today = LocalDate.now();  // Get the current date
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        boolean smsTrue = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;

        weightInput.setText("");
        addButton.setEnabled(false);

        database.addWeights(MainActivity.usr, weight, formattedDate, goalWeight);

        // Send sms achievement message if opted in
        if (weight <= goalWeight && smsTrue) {
            SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(SubscriptionManager.getDefaultSubscriptionId());
            smsManager.sendTextMessage("5554", null, "You reached your goal!", null, null);
            Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        }
        loadData();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //sendSms();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
package com.example.kevinbristowoption3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SmsResponse extends AppCompatActivity {
    Button buttonYes1;
    Button buttonNo1;
    MyDatabase database;
    static String smsOpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sms_response);
        Log.d("SMSRESPONSE", "SMSssssssssssssssssss");
        buttonYes1 = findViewById(R.id.buttonYes);
        buttonNo1 = findViewById(R.id.buttonNo);
        //Log.d("SMSRESPONSE", "SMSssssssssssssssssss");
        database = new MyDatabase(this);

        Log.d("SMSRESPONSE", "SMS executed");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void clickYes(View view) {
        database.getOptSms(MainActivity.usr, true);
        smsOpt = "true";
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
    public void clickNo(View view) {
        database.getOptSms(MainActivity.usr, false);
        smsOpt = "false";
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}
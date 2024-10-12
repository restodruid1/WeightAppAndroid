package com.example.kevinbristowoption3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.kevinbristowoption3.MyDatabase;

public class MainActivity extends AppCompatActivity {

    Button login;
    Button createAccount;
    Button info;
    EditText username;
    EditText password;
    TextView message;
    MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.buttonLogin);
        createAccount = findViewById(R.id.buttonCreateAccount);
        info = findViewById(R.id.buttonInfo);
        message = findViewById(R.id.infoPopUp);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        database = new MyDatabase(this);



        //login.setEnabled(false);
        //createAccount.setEnabled(false);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty() || password.getText().toString().isEmpty()) {
                    login.setEnabled(false);
                    createAccount.setEnabled(false);
                }else {
                    login.setEnabled(true);
                    createAccount.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty() || login.getText().toString().isEmpty()) {
                    login.setEnabled(false);
                    createAccount.setEnabled(false);
                }else {
                    login.setEnabled(true);
                    createAccount.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void clickInfo(View view) {
        String text = message.getText().toString();
        Log.d("MainActivity", "Debugging: value of text = " + text);
        if (text.isEmpty()){
            message.setText(getString(R.string.infoMessage));
            //message.setText("To create an account: Enter username and password then press 'create account'!");
        }else {
            message.setText("");
        }

    }

    public void clickLogin(View view) {
        String usernameCheck = username.getText().toString();
        String passwordCheck = password.getText().toString();
        Log.d("MainActivity", "Debugging: value of username = " + usernameCheck + passwordCheck);
        if (database.getAccount(usernameCheck, passwordCheck)){
            Log.d("Database pass", "Username and password found");
        }else{
            Log.d("Database fail", "Username and password not found");
        }
    }

    public void clickCreateAccount(View view) {
        String usernameCheck = username.getText().toString();
        String passwordCheck = password.getText().toString();

        if (database.addAccount(usernameCheck, passwordCheck)) {
            Log.d("DatabaseActivity", "Account added " + usernameCheck +" " + passwordCheck);
        } else{
            Log.d("DatabaseActivity", "Account already exists " + usernameCheck + passwordCheck);
        }

    }

}
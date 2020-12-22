package com.androiddreams.muzik.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

import com.androiddreams.muzik.R;
import com.google.android.material.button.MaterialButton;

public class AuthActivity extends AppCompatActivity {

    private String endpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        MaterialButton button = findViewById(R.id.button);
        Intent intent = getIntent();
        endpoint = intent.getStringExtra("KEY_AUTH_TYPE");
        button.setText(endpoint);
        if (endpoint.equals("login"))
            button.setBackgroundColor(getResources().getColor(R.color.colorLoginButton));
    }
}
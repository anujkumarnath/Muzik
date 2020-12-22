package com.androiddreams.muzik.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.androiddreams.muzik.R;
import com.google.android.material.button.MaterialButton;

public class AuthPromptActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_prompt);
        fragmentManager = getSupportFragmentManager();
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnSignup = findViewById(R.id.btnSignup);
        View.OnClickListener onClickListener = new ButtonClickListener();
        btnLogin.setOnClickListener(onClickListener);
        btnSignup.setOnClickListener(onClickListener);
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), AuthActivity.class);
            switch (view.getId()) {
                case R.id.btnLogin:
                    intent.putExtra("KEY_AUTH_TYPE", "login");
                    break;
                case R.id.btnSignup:
                    intent.putExtra("KEY_AUTH_TYPE", "signup");
                    break;
            }
            startActivity(intent);
        }
    }
}
package com.androiddreams.muzik.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androiddreams.muzik.MainActivity;
import com.androiddreams.muzik.R;
import com.androiddreams.muzik.models.AuthRequest;
import com.androiddreams.muzik.models.AuthResponse;
import com.androiddreams.muzik.network.APIClient;
import com.androiddreams.muzik.network.ServerInterface;
import com.google.android.material.button.MaterialButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        MaterialButton button = findViewById(R.id.button);
        Intent intent = getIntent();
        String auth_type = intent.getStringExtra("KEY_AUTH_TYPE");
        button.setText(auth_type);
        if (auth_type.equals("login"))
            button.setBackgroundColor(getResources().getColor(R.color.colorLoginButton));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (!isEmailValid(email)) {
                    Toast.makeText(view.getContext(), "Malformed email address", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!auth_type.equals("login") && !isPasswordValid(password)) {
                    Toast.makeText(view.getContext(), "Password doesn't meet requirement", Toast.LENGTH_LONG).show();
                    return;
                }

                AuthRequest authRequest = new AuthRequest();
                authRequest.setEmail(email);
                authRequest.setPassword(password);

                ServerInterface serverInterface = APIClient.getClient().create(ServerInterface.class);
                Call<AuthResponse> authRequestCall = serverInterface.authenticate(auth_type, authRequest);
                authRequestCall.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(AuthActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            if (response.body().getMessage().equals("auth_successful")) {
                                SharedPreferences sp = getSharedPreferences("login_prefs", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username", authRequest.getEmail());
                                editor.putBoolean("is_logged_in", true);
                                editor.apply();
                                Intent mainActivityIntent = new Intent(AuthActivity.this, MainActivity.class);
                                startActivity(mainActivityIntent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
    }

    private boolean isPasswordValid(final String password){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean isEmailValid(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
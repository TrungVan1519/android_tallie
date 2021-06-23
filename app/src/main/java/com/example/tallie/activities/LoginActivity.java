package com.example.tallie.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tallie.R;
import com.example.tallie.models.User;
import com.example.tallie.services.UserService;
import com.example.tallie.utils.RetrofitClient;
import com.example.tallie.utils.SharedPreferencesHandler;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    UserService userService = RetrofitClient.getInstance("https://tallie.herokuapp.com/").create(UserService.class);

    EditText txtUsername, txtPassword;
    CheckBox ckbRememberMe;
    Button btnLogin;
    TextView txtForgotPassword, txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (SharedPreferencesHandler.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        ckbRememberMe = findViewById(R.id.ckbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtSignUp = findViewById(R.id.txtSignUp);

        // TODO: handle e
        btnLogin.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (txtUsername.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Username or password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Call<String> callback = userService.loginUser(
                        new User(txtUsername.getText().toString(),
                                txtPassword.getText().toString())
                );

                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String jwt = ckbRememberMe.isChecked() ? response.body() : "";
                            SharedPreferencesHandler.saveAppData(LoginActivity.this, jwt);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            try {
                                assert response.errorBody() != null;
                                Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "onResponse: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                });
                break;
            case R.id.txtSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.txtForgotPassword:
                Toast.makeText(this, "Forgot password is not available", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
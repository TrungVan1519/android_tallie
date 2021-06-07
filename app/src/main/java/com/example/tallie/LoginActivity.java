package com.example.tallie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tallie.utils.SharedPreferencesHandler;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
            startIntent(MainActivity.class);
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

                if (ckbRememberMe.isChecked()) {
                    SharedPreferencesHandler.saveAppData(this, txtUsername.getText().toString(), txtPassword.getText().toString());
                } else {
                    SharedPreferencesHandler.saveAppData(this, "", "");
                }

                startIntent(MainActivity.class);
                break;
            case R.id.txtSignUp:
                startIntent(SignUpActivity.class);
                break;
            case R.id.txtForgotPassword:
                Toast.makeText(this, "Forgot password is not available", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void startIntent(Class<? extends AppCompatActivity> target) {
        startActivity(new Intent(this, target));
        finish();
    }
}
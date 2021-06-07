package com.example.tallie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtName, txtUsername, txtEmail, txtPassword, txtPhone;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        txtName = findViewById(R.id.txtName);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtPhone = findViewById(R.id.txtPhone);
        btnSignUp = findViewById(R.id.btnSignUp);

        // TODO: handle e
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (txtName.getText().toString().isEmpty()
                || txtUsername.getText().toString().isEmpty()
                || txtEmail.getText().toString().isEmpty()
                || txtPassword.getText().toString().isEmpty()
                || txtPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "field is empty. Check them before trying again", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtRegister, txtForgotPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        mAuth = FirebaseAuth.getInstance();
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }

        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Forgotpassword.class));
            }
        });
    }

    private void loginUser() {

        String email, password;
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Enter valid email....");
            return;
        }
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            edtPassword.setError("Password must be 6 digit long....");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();

                } else {
                    if (edtEmail.getText().toString().equals("admin@gmail.com")) {
                        startActivity(new Intent(LoginActivity.this, AdminShopActivity.class));
                        Toast.makeText(LoginActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivity.this, ShopActivity.class));
                        Toast.makeText(LoginActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

}
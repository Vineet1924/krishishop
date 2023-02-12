package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    EditText edtreset;
    Button btnreset;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        edtreset = findViewById(R.id.edtEmail);
        btnreset = findViewById(R.id.btnReset);

        mAuth = FirebaseAuth.getInstance();
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });



    }

    private void resetPassword() {
        String email = edtreset.getText().toString().trim();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(Forgotpassword.this, "Send Reset Link Failed..", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Forgotpassword.this, LoginActivity.class));
                    Toast.makeText(Forgotpassword.this, "Reset Link Sent ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResellerLoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    Button btnLogin;
    TextView txtRegister,txtForgotPassword;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    ArrayList<String> ArrayEmail = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reseller_login);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResellerLoginActivity.this, ResellerRegisterActivity.class));
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayEmail.clear();
                databaseReference = firebaseDatabase.getReference("Users");
                Query query = databaseReference.orderByChild("usertype").equalTo("Reseller");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ArrayEmail.add(ds.child("email").getValue().toString());
                        }
                        Log.i("emails",ArrayEmail.toString());
                        loginUser();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResellerLoginActivity.this, Forgotpassword.class ));
            }
        });
    }

    private void loginUser() {

        String email,password;
        email = edtEmail.getText().toString();
        password =edtPassword.getText().toString();
        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Enter valid email....");
            return;
        }
        if (TextUtils.isEmpty(password) && password.length() <6){
            edtPassword.setError("Password must be 6 digit long....");
            return;
        }

        for(int i=0; i<ArrayEmail.size();i++){
            if(ArrayEmail.get(i).equals(email)){
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(ResellerLoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()){
                            Toast.makeText(ResellerLoginActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(ResellerLoginActivity.this, ResellerShopActivity.class));
                            Toast.makeText(ResellerLoginActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }
    }
}
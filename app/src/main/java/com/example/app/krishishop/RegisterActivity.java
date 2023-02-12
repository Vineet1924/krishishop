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


import com.example.app.krishishop.Model.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtCreatePassword;
    private Button btnRegister;
    private TextView click_txt;
    private FirebaseAuth mAuth ;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtCreatePassword = findViewById(R.id.edtCreatePassword);
        btnRegister = findViewById(R.id.btnRegister);
        click_txt = findViewById(R.id.click_txt);
        mAuth = FirebaseAuth.getInstance();

        click_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser(edtEmail.getText().toString(),edtCreatePassword.getText().toString());
            }
        });


    }

    private void registerNewUser(String email, String password) {



        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Enter valid email....");
            return;
        }
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            edtCreatePassword.setError("Password must be 6 digit long....");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String RegisteredUserID = currentUser.getUid();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Register register = new Register();
                            register.setUsername(edtUsername.getText().toString());
                            register.setEmail(edtEmail.getText().toString());
                            register.setCreatePassword(edtCreatePassword.getText().toString());
                            register.setUsertype("User");

                            if(edtEmail.getText().toString().isEmpty()){
                                edtEmail.setError("Enter the Email");
                                if(edtCreatePassword.getText().toString().isEmpty()){
                                    edtCreatePassword.setError("Enter the Password");
                                }
                            }else{
                                if(edtEmail.getText().toString().trim().matches(emailPattern)){
                                    if(edtCreatePassword.getText().toString().length()>=6){
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(mAuth.getUid());
                                        databaseReference.setValue(register).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }else{
                                        edtCreatePassword.setError("Not Valid Password");
                                    }
                                }else {
                                    edtEmail.setError("Not Valid Email");
                                    return;
                                }
                            }


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(RegisterActivity.this, "*** Something Wrong Please Try Again ***", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

}
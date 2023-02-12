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

import com.example.app.krishishop.Model.Farmer;
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

public class FarmerRegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPassword, edtMobileno;
    private Button btnRegister;
    private TextView click_text;
    private FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtMobileno = findViewById(R.id.edtMobileNo);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtCreatePassword);
        btnRegister = findViewById(R.id.btnRegister);
        click_text = findViewById(R.id.click_txt);
        mAuth = FirebaseAuth.getInstance();
        click_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmerRegisterActivity.this, FarmerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewFarmer(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });


    }

    public void registerNewFarmer(String email, String password) {


        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Enter valid email....");
            return;
        }
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            edtPassword.setError("Password must be 6 digit long....");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(FarmerRegisterActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();

                } else {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String RegisteredUserID = currentUser.getUid();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(RegisteredUserID);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Farmer farmer = new Farmer();
                            farmer.setEmail(edtEmail.getText().toString());
                            farmer.setMobileno(edtMobileno.getText().toString());
                            farmer.setPassword(edtPassword.getText().toString());
                            farmer.setUsername(edtUsername.getText().toString());
                            farmer.setUsertype("Farmer");

                            if(edtEmail.getText().toString().isEmpty()){
                                edtEmail.setError("Enter the Email");
                                if(edtPassword.getText().toString().isEmpty()){
                                    edtPassword.setError("Enter the Password");
                                }
                            }else{
                                if(edtEmail.getText().toString().trim().matches(emailPattern)){
                                    if(edtPassword.getText().toString().length()>=6){
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(mAuth.getUid());
                                        databaseReference.setValue(farmer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(FarmerRegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }else {
                                        edtPassword.setError("Not Valid Password");
                                    }
                                }else {
                                    edtEmail.setError("Not Valid Email");
                                    return;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(FarmerRegisterActivity.this, "*** Something Wrong Please Try Again ***", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}

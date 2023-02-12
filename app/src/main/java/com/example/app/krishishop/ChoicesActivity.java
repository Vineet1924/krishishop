package com.example.app.krishishop;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;


public class ChoicesActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView heading;
    ImageView imageView;
    Button choiceAdmin,choiceReseller, choiceFarmer, choiceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
        initView();

        mAuth = FirebaseAuth.getInstance();

        choiceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoicesActivity.this,LoginActivity.class));
            }
        });
        choiceReseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoicesActivity.this,ResellerLoginActivity.class));
            }
        });
        choiceFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoicesActivity.this,FarmerLoginActivity.class));
            }
        });
        choiceUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoicesActivity.this,LoginActivity.class));
            }
        });
    }

    private void initView() {
        heading = findViewById(R.id.heading);
        imageView = findViewById(R.id.imgLogin);
        choiceAdmin = findViewById(R.id.btnChoice1);
        choiceReseller = findViewById(R.id.btnChoice2);
        choiceFarmer = findViewById(R.id.btnChoice3);
        choiceUser = findViewById(R.id.btnChoice4);
    }
}
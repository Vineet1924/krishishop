package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashActivity extends AppCompatActivity {

    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        animationView = findViewById(R.id.mainAnimation);
        animationView.setAnimation("farmer.json");
        animationView.playAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userType = ""+snapshot.child("usertype").getValue();
                            switch (userType){
                                case "Reseller":
                                    startActivity(new Intent(SplashActivity.this,ResellerShopActivity.class));
                                    finish();
                                    break;
                                case "Farmer":
                                    startActivity(new Intent(SplashActivity.this,FarmerShopActivity.class));
                                    finish();
                                    break;
                                case "User":
                                    startActivity(new Intent(SplashActivity.this,ShopActivity.class));
                                    finish();
                                    break;
                                case "Admin":
                                    startActivity(new Intent(SplashActivity.this,AdminShopActivity.class));
                                    finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    startActivity(new Intent(SplashActivity.this,BlankUserActivity.class));
                    finish();
                }

            }
        },4000);

    }


}
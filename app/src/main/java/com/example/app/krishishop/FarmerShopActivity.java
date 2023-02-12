package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class FarmerShopActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_shop);

        mAuth = FirebaseAuth.getInstance();

        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_farmer);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_cart,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.cart){
            Intent intent = new Intent(FarmerShopActivity.this,CartActivity.class);
            startActivity(intent);
        }else if(id == R.id.weather){
            Intent intent = new Intent(FarmerShopActivity.this,WeatherActivity.class);
            startActivity(intent);
        }else if(id == R.id.delivery){
            Intent intent = new Intent(FarmerShopActivity.this,DeliveryActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_farmer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.Home:
                fragment = new HomeFragment();
                break;
            case R.id.Sell:
                fragment = new SellFragment();
                break;
            case R.id.Help:
                fragment = new HelpFragment();
                break;
            case R.id.Profile:
                fragment = new FarmerProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }

}
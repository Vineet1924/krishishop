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

public class AdminShopActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop);

        mAuth=FirebaseAuth.getInstance();

        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_admin);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_cart,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_admin, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.cart){
            Intent intent = new Intent(AdminShopActivity.this,CartActivity.class);
            startActivity(intent);
        }else if(id == R.id.weather){
            Intent intent = new Intent(AdminShopActivity.this,WeatherActivity.class);
            startActivity(intent);
        }else if(id == R.id.delivery){
            Intent intent = new Intent(AdminShopActivity.this,DeliveryActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.Home:
                fragment = new HomeFragment();
                break;
            case R.id.Remove:
                fragment = new RemoveFragment();
                break;
            case R.id.Help:
                fragment = new HelpFragment();
                break;
            case R.id.Profile:
                fragment = new ProfileFragment();
                break;
        }

        return loadFragment(fragment);
    }


}
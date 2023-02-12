package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.app.krishishop.Model.VeritcalProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ViewFarmingProduct extends AppCompatActivity {

    TextView txtProductName, txtPrice, txtRating, txtDescription, txtCount;
    RoundedImageView imgProduct;
    Button btnAddToCart;
    ImageView imgAdd, imgRemove;

    int totalQuantity = 1;
    int totalPrice = 0;

    VeritcalProduct veritcalProduct = null;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_farming_product);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Object object = getIntent().getSerializableExtra("detailFarming");
        if (object instanceof VeritcalProduct) {
            veritcalProduct = (VeritcalProduct) object;
        }

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        txtProductName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtPrice);
        txtRating = findViewById(R.id.txtRating);
        txtDescription = findViewById(R.id.txtDescription);
        imgProduct = findViewById(R.id.imgProduct);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtCount = findViewById(R.id.txtCount);
        imgAdd = findViewById(R.id.imgAdd);
        imgRemove = findViewById(R.id.imgRemove);

        if (veritcalProduct != null) {
            Glide.with(getApplicationContext()).load(veritcalProduct.getImg_url()).into(imgProduct);
            txtProductName.setText(veritcalProduct.getName());
            txtRating.setText(veritcalProduct.getRating());
            txtDescription.setText(veritcalProduct.getDescription());
            txtPrice.setText("Price : ₹ " + veritcalProduct.getPrice() + "/kg");

            totalPrice = Integer.parseInt(veritcalProduct.getPrice()) * totalQuantity;
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 10) {
                    totalQuantity++;
                    txtCount.setText(String.valueOf(totalQuantity));
                    totalPrice = Integer.parseInt(veritcalProduct.getPrice()) * totalQuantity;

                }
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1) {
                    totalQuantity--;
                    txtCount.setText(String.valueOf(totalQuantity));
                    totalPrice = Integer.parseInt(veritcalProduct.getPrice()) * totalQuantity;

                }
            }
        });
    }

    private void addToCart() {

        if (firebaseUser == null) {
            Intent intent = new Intent(ViewFarmingProduct.this, ChoicesActivity.class);
            finish();
            startActivity(intent);
        } else {
            String saveCurrentDate, saveCurrentTime;
            Calendar calForDate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            final HashMap<String, Object> cartMap = new HashMap<>();

            cartMap.put("productName", veritcalProduct.getName());
            cartMap.put("productPrice", txtPrice.getText().toString());
            cartMap.put("productDate", saveCurrentDate);
            cartMap.put("productTime", saveCurrentTime);
            cartMap.put("totalQuantity", txtCount.getText().toString());
            cartMap.put("totalPrice", totalPrice);

            firestore.collection("AddToCart").document("CurrentUser")
                    .collection(firebaseUser.getUid()).add(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(ViewFarmingProduct.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }

    }

}
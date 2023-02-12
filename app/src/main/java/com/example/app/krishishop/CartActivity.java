package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.app.krishishop.Model.MyCartModel;
import com.example.app.krishishop.adapter.MyCartAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    TextView txtTotalPriceCart;
    Button btnBuyNow;
    RecyclerView recyclerCart;
    MyCartAdapter myCartAdapter;
    List<MyCartModel> myCartModelList;

    ConstraintLayout constraint1;
    NestedScrollView NestedScrollView;
    RelativeLayout rel;

    ArrayList<String> productName = new ArrayList<>();
    ArrayList<String> productQuantity = new ArrayList<>();
    ArrayList<String> purchaseDate = new ArrayList<>();

    double total = 0.0;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @SuppressLint({"RestrictedApi", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        constraint1 = findViewById(R.id.constraint1);
        NestedScrollView = findViewById(R.id.NestedScrollView);
        rel = findViewById(R.id.rel);

        txtTotalPriceCart = findViewById(R.id.txtTotalPriceCart);
        recyclerCart = findViewById(R.id.recyclerCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);


        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        myCartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(this,myCartModelList);
        recyclerCart.setAdapter(myCartAdapter);

        db.collection("AddToCart").document("CurrentUser")
                .collection(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(!task.getResult().isEmpty()){

                    constraint1.setVisibility(View.GONE);
                    NestedScrollView.setVisibility(View.VISIBLE);
                    rel.setVisibility(View.VISIBLE);

                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();

                        MyCartModel myCartModel = documentSnapshot.toObject(MyCartModel.class);

                        myCartModel.setDocumentId(documentId);
                        myCartModelList.add(myCartModel);
                        myCartAdapter.notifyDataSetChanged();
                    }
                    calculateTotalAmount(myCartModelList);
                }
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetails(myCartModelList);
                Intent i = new Intent(CartActivity.this,BuyNowActivity.class);
                String temp = Double.toString(total);
                i.putExtra("total",temp);
                i.putExtra("productNames",productName);
                i.putExtra("productQuantity",productQuantity);
                i.putExtra("purchaseDate",purchaseDate);
                startActivity(i);
                finish();
            }
        });

    }

    private void calculateTotalAmount(List<MyCartModel> myCartModelList) {

        double totalAmount = 0.0;
        for(MyCartModel myCartModel : myCartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }

        txtTotalPriceCart.setText("₹ "+ totalAmount);
        total = totalAmount;
    }

    private void productDetails(List<MyCartModel> myCartModelList){

        for(MyCartModel myCartModel : myCartModelList){
            productName.add(myCartModel.getProductName());
            productQuantity.add(myCartModel.getTotalQuantity());
            purchaseDate.add(myCartModel.getProductDate());
        }
    }

}
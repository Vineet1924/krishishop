package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.app.krishishop.Model.DeliveryModel;
import com.example.app.krishishop.adapter.DeliveryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class DeliveryActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    RecyclerView recyclerDelivery;
    DeliveryAdapter deliveryAdapter;
    List<DeliveryModel> deliveryModelList;

    RelativeLayout rel;

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
        setContentView(R.layout.activity_delivery);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        recyclerDelivery = findViewById(R.id.recyclerDelivery);
        rel = findViewById(R.id.rel);

        recyclerDelivery.setLayoutManager(new LinearLayoutManager(this));

        deliveryModelList = new ArrayList<>();
        deliveryAdapter = new DeliveryAdapter(this,deliveryModelList);
        recyclerDelivery.setAdapter(deliveryAdapter);

        fetchData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void fetchData(){
        firestore.collection("Delivery").document("currentUser")
                .collection(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                            String documentId = documentSnapshot.getId();

                            DeliveryModel deliveryModel = documentSnapshot.toObject(DeliveryModel.class);
                            deliveryModel.setDocumentId(documentId);

                            rel.setVisibility(View.GONE);
                            recyclerDelivery.setVisibility(View.VISIBLE);

                            deliveryModelList.add(deliveryModel);
                            deliveryAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
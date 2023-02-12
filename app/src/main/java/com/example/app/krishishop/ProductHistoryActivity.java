package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.app.krishishop.Model.HistoryModel;
import com.example.app.krishishop.adapter.HistoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductHistoryActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    ImageView bg;
    RecyclerView recyclerHistory;
    List<HistoryModel> historyModelList;
    HistoryAdapter historyAdapter;

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
        setContentView(R.layout.activity_product_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerHistory = findViewById(R.id.recyclerHistory);
        bg = findViewById(R.id.bg);

        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));

        historyModelList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyModelList);
        recyclerHistory.setAdapter(historyAdapter);

        db.collection("History").document("currentUser")
                .collection(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(!task.getResult().isEmpty()){
                            bg.setVisibility(View.GONE);
                            recyclerHistory.setVisibility(View.VISIBLE);
                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                HistoryModel historyModel = documentSnapshot.toObject(HistoryModel.class);
                                historyModelList.add(historyModel);
                                historyAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
    }
}
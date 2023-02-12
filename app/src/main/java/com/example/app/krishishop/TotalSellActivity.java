package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app.krishishop.Model.AllHistory;
import com.example.app.krishishop.adapter.AllHistoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TotalSellActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    ImageView bg;
    RecyclerView recyclerTotalSell;

    List<AllHistory> allHistoryList;
    AllHistoryAdapter allHistoryAdapter;

    Button btnSearchDate;
    DatePicker datePicker;
    EditText search_box;

    SwipeRefreshLayout refLay;

    ArrayList<String> dates = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_sell);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        refLay = findViewById(R.id.refLay);
        recyclerTotalSell = findViewById(R.id.recyclerTotalSell);
        btnSearchDate = findViewById(R.id.btnSearchDate);
        datePicker = findViewById(R.id.datePicker);
        search_box = findViewById(R.id.search_box);
        bg = findViewById(R.id.bg);

        recyclerTotalSell.setLayoutManager(new LinearLayoutManager(this));

        allHistoryList = new ArrayList<>();
        allHistoryAdapter = new AllHistoryAdapter(allHistoryList,this);
        recyclerTotalSell.setAdapter(allHistoryAdapter);

        db.collection("History").document("totalSell")
                .collection("admin").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(!task.getResult().isEmpty()){
                            bg.setVisibility(View.GONE);
                            recyclerTotalSell.setVisibility(View.VISIBLE);
                            search_box.setVisibility(View.VISIBLE);
                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                AllHistory allHistory = documentSnapshot.toObject(AllHistory.class);
                                allHistoryList.add(allHistory);
                                allHistoryAdapter.notifyDataSetChanged();
                            }
                            productDates();
                        }

                    }
                });

        btnSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
            }
        });

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,monthOfYear,dayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("MM dd, yyyy");
                String date = format.format(calendar.getTime());
                Toast.makeText(TotalSellActivity.this, date, Toast.LENGTH_SHORT).show();

                db.collection("History").document("totalSell")
                        .collection("admin").whereEqualTo("purchaseDate",date).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty()){
                                    bg.setVisibility(View.VISIBLE);
                                    recyclerTotalSell.setVisibility(View.GONE);
                                    search_box.setVisibility(View.GONE);
                                }else{
                                    bg.setVisibility(View.GONE);
                                    recyclerTotalSell.setVisibility(View.VISIBLE);
                                    search_box.setVisibility(View.VISIBLE);
                                    allHistoryList.clear();
                                    allHistoryAdapter.notifyDataSetChanged();
                                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                        AllHistory allHistory = documentSnapshot.toObject(AllHistory.class);
                                        allHistoryList.add(allHistory);
                                        allHistoryAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        });

                datePicker.setVisibility(View.GONE);
            }
        });

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                db.collection("History").document("totalSell")
                        .collection("admin").whereEqualTo("productName",s.toString()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty()){
                                    recyclerTotalSell.setVisibility(View.GONE);
                                }else{
                                    bg.setVisibility(View.GONE);
                                    recyclerTotalSell.setVisibility(View.VISIBLE);
                                    search_box.setVisibility(View.VISIBLE);
                                    allHistoryList.clear();
                                    allHistoryAdapter.notifyDataSetChanged();
                                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                        AllHistory allHistory = documentSnapshot.toObject(AllHistory.class);
                                        allHistoryList.add(allHistory);
                                        allHistoryAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        });

            }
        });

        refLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refLay.setRefreshing(false);
                        finish();
                        startActivity(getIntent());
                    }
                }, 2000);
            }
        });

    }


    private void productDates(){

        for(int i=0;i<allHistoryList.size();i++){
            dates.add(allHistoryList.get(i).getPurchaseDate());
        }
        Log.i("dates",dates.toString());
    }
}
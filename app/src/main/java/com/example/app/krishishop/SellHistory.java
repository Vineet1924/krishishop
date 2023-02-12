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

import com.example.app.krishishop.Model.SellHistoryModel;
import com.example.app.krishishop.adapter.SellHistoryAdapter;
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

public class SellHistory extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ImageView bg;
    RecyclerView recyclerSell;

    List<SellHistoryModel> sellHistoryModelList;
    SellHistoryAdapter sellHistoryAdapter;

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
        setContentView(R.layout.activity_sell_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        refLay = findViewById(R.id.refLay);
        recyclerSell = findViewById(R.id.recyclerSell);
        btnSearchDate = findViewById(R.id.btnSearchDate);
        datePicker = findViewById(R.id.datePicker);
        search_box = findViewById(R.id.search_box);
        bg = findViewById(R.id.bg);

        recyclerSell.setLayoutManager(new LinearLayoutManager(this));

        sellHistoryModelList = new ArrayList<>();
        sellHistoryAdapter = new SellHistoryAdapter(sellHistoryModelList,this);
        recyclerSell.setAdapter(sellHistoryAdapter);

        db.collection("History").document("sellHistory")
                .collection(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(!task.getResult().isEmpty()){
                            bg.setVisibility(View.GONE);
                            recyclerSell.setVisibility(View.VISIBLE);
                            search_box.setVisibility(View.VISIBLE);
                            for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                SellHistoryModel model = documentSnapshot.toObject(SellHistoryModel.class);
                                sellHistoryModelList.add(model);
                                sellHistoryAdapter.notifyDataSetChanged();
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
                Toast.makeText(SellHistory.this, date, Toast.LENGTH_SHORT).show();

                db.collection("History").document("sellHistory")
                        .collection(mAuth.getCurrentUser().getUid()).whereEqualTo("sellDate",date).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty()){
                                    bg.setVisibility(View.VISIBLE);
                                    recyclerSell.setVisibility(View.GONE);
                                    search_box.setVisibility(View.GONE);
                                }else{
                                    bg.setVisibility(View.GONE);
                                    recyclerSell.setVisibility(View.VISIBLE);
                                    search_box.setVisibility(View.VISIBLE);
                                    sellHistoryModelList.clear();
                                    sellHistoryAdapter.notifyDataSetChanged();
                                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                        SellHistoryModel sellHistoryModel = documentSnapshot.toObject(SellHistoryModel.class);
                                        sellHistoryModelList.add(sellHistoryModel);
                                        sellHistoryAdapter.notifyDataSetChanged();
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
                db.collection("History").document("sellHistory")
                        .collection(mAuth.getCurrentUser().getUid()).whereEqualTo("productName",s.toString()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty()){
                                    recyclerSell.setVisibility(View.GONE);
                                }else{
                                    bg.setVisibility(View.GONE);
                                    recyclerSell.setVisibility(View.VISIBLE);
                                    search_box.setVisibility(View.VISIBLE);
                                    sellHistoryModelList.clear();
                                    sellHistoryAdapter.notifyDataSetChanged();
                                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                        SellHistoryModel sellHistoryModel = documentSnapshot.toObject(SellHistoryModel.class);
                                        sellHistoryModelList.add(sellHistoryModel);
                                        sellHistoryAdapter.notifyDataSetChanged();
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

        for(int i=0;i<sellHistoryModelList.size();i++){
            dates.add(sellHistoryModelList.get(i).getSellDate());
        }
        Log.i("dates",dates.toString());
    }
}
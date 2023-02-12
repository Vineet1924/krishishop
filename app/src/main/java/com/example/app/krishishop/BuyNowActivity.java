package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BuyNowActivity extends AppCompatActivity implements PaymentResultListener {

    Button btnBuyNow;
    TextView txtPrice;
    EditText edtName, edtPhone, edtAddress, edtPinCode;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    RelativeLayout rel1,rel2;
    LottieAnimationView animationView;

    ArrayList<String> productName = new ArrayList<>();
    ArrayList<String> productQuantity = new ArrayList<>();
    ArrayList<String> purchaseDate = new ArrayList<>();

    String pNames = "";

    Double FinalAmount;

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
        setContentView(R.layout.activity_buy_now);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        rel1 = findViewById(R.id.rel1);
        rel2 = findViewById(R.id.rel2);
        animationView = findViewById(R.id.mainAnimation);

        btnBuyNow = findViewById(R.id.btnBuyNow);
        txtPrice = findViewById(R.id.txtPrice);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtPinCode = findViewById(R.id.edtPinCode);

        Intent i = getIntent();
        String str = i.getStringExtra("total");
        FinalAmount = Double.parseDouble(str);
        txtPrice.setText("₹ " + str);

        productName = i.getStringArrayListExtra("productNames");
        productQuantity = i.getStringArrayListExtra("productQuantity");
        purchaseDate = i.getStringArrayListExtra("purchaseDate");
        Log.i("name", productName.toString());
        Log.i("quantity",productQuantity.toString());
        Log.i("date",purchaseDate.toString());


        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String phoneNumber = edtPhone.getText().toString();
                String address = edtAddress.getText().toString();
                String pinCode = edtPinCode.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    edtName.setError("please enter your name");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber) && phoneNumber.length() != 10) {
                    edtPhone.setError("please enter your phone number");
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    edtAddress.setError("please enter your delivery address");
                    return;
                }
                if (TextUtils.isEmpty(pinCode) && pinCode.length() != 6) {
                    edtPinCode.setError("please enter your pin code");
                    return;
                }

                Checkout checkout = new Checkout();

                checkout.setKeyID("rzp_test_nzlMAByiktQ47R");
                checkout.setImage(R.drawable.icon);

                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", name);

                    // put description
                    object.put("description", "checkout");

                    // to set theme color
                    object.put("theme.color", "#38c172");

                    // put the currency
                    object.put("currency", "INR");

                    // put amount
                    object.put("amount", FinalAmount * 100);

                    // put mobile number
                    object.put("prefill.contact", phoneNumber);

                    // put email
                    object.put("prefill.email", mAuth.getCurrentUser().getEmail());

                    // open razorpay to checkout activity
                    checkout.open(BuyNowActivity.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        if (mAuth.getCurrentUser() != null) {

            String saveCurrentDate, saveCurrentTime;
            Calendar calForDate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());



            final HashMap<String, Object> deliveryProduct = new HashMap<>();

            deliveryProduct.put("paymentId", s);
            deliveryProduct.put("totalPaid", FinalAmount);
            deliveryProduct.put("status", "paid");
            deliveryProduct.put("paymentDate",saveCurrentDate);
            deliveryProduct.put("paymentTime",saveCurrentTime);
            deliveryProduct.put("name",edtName.getText().toString());
            deliveryProduct.put("phoneNumber",edtPhone.getText().toString());
            deliveryProduct.put("pinCode",edtPinCode.getText().toString());
            deliveryProduct.put("address",edtAddress.getText().toString());
            for(int i=0;i<productName.size();i++){
                pNames += productName.get(i)+",";
            }
            deliveryProduct.put("products",pNames);


            firestore.collection("Delivery").document("currentUser")
                    .collection(mAuth.getCurrentUser().getUid()).add(deliveryProduct)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            addHistory(saveCurrentDate);
                            addAdminHistory(saveCurrentDate);
                            rel2.setVisibility(View.GONE);
                            rel1.setVisibility(View.VISIBLE);
                            animationView.setAnimation("paymentDone.json");
                            animationView.playAnimation();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(BuyNowActivity.this,DeliveryActivity.class));
                                    rel1.setVisibility(View.GONE);
                                    rel2.setVisibility(View.VISIBLE);
                                    finish();
                                }
                            },3000);
                        }
                    });
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        if (s.contains("payment_cancelled")) {
            rel2.setVisibility(View.GONE);
            rel1.setVisibility(View.VISIBLE);
            animationView.setAnimation("paymentFaild.json");
            animationView.playAnimation();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rel1.setVisibility(View.GONE);
                    rel2.setVisibility(View.VISIBLE);
                    finish();
                }
            },3000);
        } else {
            Toast.makeText(this, "Payment error due to :" + s, Toast.LENGTH_LONG).show();
            rel2.setVisibility(View.GONE);
            rel1.setVisibility(View.VISIBLE);
            animationView.setAnimation("paymentFaild.json");
            animationView.playAnimation();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rel1.setVisibility(View.GONE);
                    rel2.setVisibility(View.VISIBLE);
                    finish();
                }
            },3000);
        }
    }

    private void addHistory(String saveCurrentDate){

        for(int i=0;i<productName.size();i++){

            final HashMap<String, Object> history = new HashMap<>();

            history.put("productName", productName.get(i));
            history.put("productQuantity",productQuantity.get(i));
            history.put("purchaseDate",saveCurrentDate);

            firestore.collection("History").document("currentUser")
                    .collection(mAuth.getCurrentUser().getUid()).add(history)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Log.d("status", "history added");
                        }
                    });
        }

    }

    private void addAdminHistory(String saveCurrentDate){

        for(int i=0;i<productName.size();i++){

            final HashMap<String, Object> TotalHistory = new HashMap<>();

            TotalHistory.put("productName", productName.get(i));
            TotalHistory.put("productQuantity",productQuantity.get(i));
            TotalHistory.put("purchaseDate",saveCurrentDate);
            TotalHistory.put("userID",mAuth.getCurrentUser().getUid());

            firestore.collection("History").document("totalSell")
                    .collection("admin").add(TotalHistory)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Log.d("status", "history added");
                        }
                    });
        }

    }
}
package com.example.app.krishishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.app.krishishop.Model.DeliveryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewOrder extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    TextView OrderID,Status,StatusDescription;
    LottieAnimationView animationView;

    DeliveryModel deliveryModel = null;

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
        setContentView(R.layout.activity_view_order);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Object object = getIntent().getSerializableExtra("order");
        if (object instanceof DeliveryModel) {
            deliveryModel = (DeliveryModel) object;
        }

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        OrderID = findViewById(R.id.OrderID);
        Status = findViewById(R.id.Status);
        StatusDescription = findViewById(R.id.StatusDescription);
        animationView = findViewById(R.id.mainAnimation);

        if(deliveryModel != null){
            OrderID.setText(deliveryModel.getDocumentId());
            Status.setText(deliveryModel.getStatus());
        }

        deliveryProcess();

    }

    private void deliveryProcess(){

        switch (Status.getText().toString()){

            case "paid" :
                animationView.setAnimation("orderProcessing.json");
                animationView.playAnimation();
                StatusDescription.setText("Your order is placed successfully ☺\n\nYour order is under processing after it will ready to departure..");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Status.setText("processed");
                        animationView.setAnimation("departure.json");
                        animationView.playAnimation();
                        StatusDescription.setText("Your order is successfully processed 🥳🎉🎊\n\nNow your order is ready for delivery..\nYou will get your product soon..");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Status.setText("on the way");
                                animationView.setAnimation("delivery.json");
                                animationView.playAnimation();
                                StatusDescription.setText("Your order is now on the way 🚚...\n\nIn short time we'll in front in front of your door..");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Status.setText("Delivered");
                                        animationView.setAnimation("delivered.json");
                                        animationView.playAnimation();
                                        StatusDescription.setText("Your order is delivered successfully 🥳\n\nThank you for purchasing our products..\n\t\tSee you soon..");
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                firestore.collection("Delivery").document("currentUser")
                                                        .collection(mAuth.getCurrentUser().getUid())
                                                        .document(OrderID.getText().toString()).delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                finish();
                                                            }
                                                        });
                                            }
                                        },20000);
                                    }
                                },10000);
                            }
                        },5000);
                    }
                },5000);
                break;
        }

    }
}
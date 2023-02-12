package com.example.app.krishishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.krishishop.CartActivity;
import com.example.app.krishishop.Model.MyCartModel;
import com.example.app.krishishop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCartModel> myCartModels;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    public MyCartAdapter(Context context, List<MyCartModel> myCartModels) {
        this.context = context;
        this.myCartModels = myCartModels;
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(myCartModels.get(position).getProductName());
        holder.price.setText(myCartModels.get(position).getProductPrice());
        holder.quantity.setText(myCartModels.get(position).getTotalQuantity());
        holder.totalPrice.setText(String.valueOf(myCartModels.get(position).getTotalPrice()));

        holder.imgProductRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("AddToCart").document("CurrentUser")
                        .collection(mAuth.getCurrentUser().getUid())
                        .document(myCartModels.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    myCartModels.remove(myCartModels.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context,CartActivity.class);
                                    ((CartActivity)context).finish();
                                    context.startActivity(intent);

                                }else{
                                    Toast.makeText(context, "Error"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return myCartModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,price,quantity,totalPrice;
        ImageView imgProductRemoveCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtProductNameCart);
            price = itemView.findViewById(R.id.txtPriceCart);
            quantity = itemView.findViewById(R.id.txtTotalQuantityCart);
            totalPrice = itemView.findViewById(R.id.txtTotalPriceCart);
            imgProductRemoveCart = itemView.findViewById(R.id.imgProductRemoveCart);

        }
    }

}

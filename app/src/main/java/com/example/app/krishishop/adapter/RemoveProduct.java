package com.example.app.krishishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.krishishop.Model.RemoveProductModel;
import com.example.app.krishishop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RemoveProduct extends RecyclerView.Adapter<RemoveProduct.ViewHolder> {

    Context context;
    List<RemoveProductModel> list;
    FirebaseFirestore firestore;

    public RemoveProduct(Context context, List<RemoveProductModel> list) {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RemoveProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.remove_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RemoveProduct.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imgProduct);
        holder.txtProductName.setText(list.get(position).getName());
        holder.txtPrice.setText("₹ " + list.get(position).getPrice());

        holder.imgRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("ResellerProducts")
                        .document(list.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            list.remove(list.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                firestore.collection("FarmingProducts")
                        .document(list.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct, imgRemoveBtn;
        TextView txtProductName, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgRemoveBtn = itemView.findViewById(R.id.imgRemoveBtn);

        }
    }
}

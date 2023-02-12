package com.example.app.krishishop.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.krishishop.Model.VeritcalProduct;
import com.example.app.krishishop.R;
import com.example.app.krishishop.ViewFarmingProduct;

import java.util.List;

public class VerticalProductAdapter extends RecyclerView.Adapter<VerticalProductAdapter.ViewHolder> {

    private Context context;
    private List<VeritcalProduct> veritcalProducts;

    public VerticalProductAdapter(Context context, List<VeritcalProduct> veritcalProducts) {
        this.context = context;
        this.veritcalProducts = veritcalProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.farming_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(veritcalProducts.get(position).getImg_url()).into(holder.imgProduct);
        holder.txtProductName.setText(veritcalProducts.get(position).getName());
        holder.txtRating.setText(veritcalProducts.get(position).getRating());
        holder.txtPrice.setText(veritcalProducts.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewFarmingProduct.class);
                intent.putExtra("detailFarming",veritcalProducts.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return veritcalProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtProductName,txtRating,txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}

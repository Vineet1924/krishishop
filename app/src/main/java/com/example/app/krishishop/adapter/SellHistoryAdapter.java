package com.example.app.krishishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.krishishop.Model.SellHistoryModel;
import com.example.app.krishishop.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SellHistoryAdapter extends RecyclerView.Adapter<SellHistoryAdapter.ViewHolder> {

    List<SellHistoryModel> sellHistoryModelList;
    Context context;

    public SellHistoryAdapter(List<SellHistoryModel> sellHistoryModelList, Context context) {
        this.sellHistoryModelList = sellHistoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public SellHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sell_history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SellHistoryAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(sellHistoryModelList.get(position).getProductImage()).into(holder.imgProduct);
        holder.pName.setText(sellHistoryModelList.get(position).getProductName());
        holder.pPrice.setText(sellHistoryModelList.get(position).getProductPrice());
        holder.pDate.setText(sellHistoryModelList.get(position).getSellDate());
    }

    @Override
    public int getItemCount() {
        return sellHistoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imgProduct;
        TextView pName,pPrice,pDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            pDate = itemView.findViewById(R.id.pDate);
        }
    }
}

package com.example.app.krishishop.adapter;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.krishishop.Model.DeliveryModel;
import com.example.app.krishishop.R;
import com.example.app.krishishop.ViewOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {

    Context context;
    List<DeliveryModel> deliveryModels;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    public DeliveryAdapter(Context context, List<DeliveryModel> deliveryModels) {
        this.context = context;
        this.deliveryModels = deliveryModels;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }



    @NonNull
    @Override
    public DeliveryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_background,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txtOrderId.setText(deliveryModels.get(position).getDocumentId());
        holder.PaymentId.setText(deliveryModels.get(position).getPaymentId());
        holder.PaymentDate.setText(deliveryModels.get(position).getPaymentDate());
        holder.PaymentTime.setText(deliveryModels.get(position).getPaymentTime());
        holder.Status.setText(deliveryModels.get(position).getStatus());
        holder.Total.setText(deliveryModels.get(position).getTotalPaid().toString());

        holder.btn_Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                }else {
                    generatePDF(deliveryModels.get(position).getDocumentId(),
                            deliveryModels.get(position).getPaymentId(),
                            deliveryModels.get(position).getPaymentDate(),
                            deliveryModels.get(position).getPaymentTime(),
                            deliveryModels.get(position).getTotalPaid().toString(),
                            deliveryModels.get(position).getName(),
                            deliveryModels.get(position).getAddress(),
                            deliveryModels.get(position).getPinCode(),
                            deliveryModels.get(position).getPhoneNumber(),
                            deliveryModels.get(position).getProducts()
                    );

                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewOrder.class);
                intent.putExtra("order",deliveryModels.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtOrderId,PaymentId,Status,PaymentDate,PaymentTime,Total;
        Button btn_Delivery;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            PaymentId = itemView.findViewById(R.id.PaymentId);
            PaymentDate = itemView.findViewById(R.id.PaymentDate);
            PaymentTime = itemView.findViewById(R.id.PaymentTime);
            Status = itemView.findViewById(R.id.Status);
            Total = itemView.findViewById(R.id.Total);
            btn_Delivery = itemView.findViewById(R.id.btn_Delivery);
        }
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions((Activity) context, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @SuppressLint("ResourceAsColor")
    private void generatePDF(String orderId, String paymentId, String paymentDate,
                             String paymentTime, String total, String name, String address,
                             String pinCode, String phone, String products) {

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();

        Typeface plain = ResourcesCompat.getFont(context,R.font.urbanistbold);
        paint.setTypeface(plain);

        paint.setTextSize(25);

        page.getCanvas().drawText("KrishiShop",20,40,paint);

        paint.setStrokeWidth(2);

        page.getCanvas().drawLine(20,60,290,60,paint);

        paint.setTextSize(15);

        page.getCanvas().drawText("*** Bill Details ***",105,85,paint);

        paint.setTextSize(10);
        paint.setStrokeWidth(1);

        page.getCanvas().drawLine(20,120,290,120,paint);
        page.getCanvas().drawText("** Personal Details **", 20,135,paint);
        page.getCanvas().drawLine(20,140,290,140,paint);


        page.getCanvas().drawText("Name : ",20,160,paint);
        page.getCanvas().drawText(name,55,160,paint);

        page.getCanvas().drawText("Delivery Address : ",20,180,paint);
        page.getCanvas().drawText(address,102,180,paint);

        page.getCanvas().drawText("Pin Code :",20,200,paint);
        page.getCanvas().drawText(pinCode,65,200,paint);

        page.getCanvas().drawText("Phone Number : ",20,220,paint);
        page.getCanvas().drawText(phone,95,220,paint);

        page.getCanvas().drawLine(20,240,290,240,paint);
        page.getCanvas().drawText("** Product Details **",20,255,paint);
        page.getCanvas().drawLine(20,260,290,260,paint);

        page.getCanvas().drawText("Order Id : ",20,280,paint);
        page.getCanvas().drawText(orderId,63,280,paint);

        page.getCanvas().drawText("Payment Id : ",20,300,paint);
        page.getCanvas().drawText(paymentId,78,300,paint);

        page.getCanvas().drawText("Payment Date : ",20,320,paint);
        page.getCanvas().drawText(paymentDate,92,320,paint);

        page.getCanvas().drawText("Payment Time : ",20,340,paint);
        page.getCanvas().drawText(paymentTime,93,340,paint);

        page.getCanvas().drawText("Product Name : ",20,360,paint);
        page.getCanvas().drawText(products,93,360,paint);

        page.getCanvas().drawLine(20,540,290,540,paint);
        page.getCanvas().drawText("Total Paid : ",20,560,paint);
        page.getCanvas().drawText("₹ "+ total,250,560,paint);

        pdfDocument.finishPage(page);

        String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/"+orderId+".pdf";
        File file = new File(stringFilePath);

        try{

            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF downloaded on path :\n/Downloads/"+orderId+".pdf", Toast.LENGTH_LONG).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

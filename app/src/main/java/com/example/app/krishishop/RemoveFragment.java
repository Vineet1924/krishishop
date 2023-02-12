package com.example.app.krishishop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app.krishishop.Model.RemoveProductModel;
import com.example.app.krishishop.adapter.RemoveProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RemoveFragment extends Fragment {

    RecyclerView recyclerRemoveProduct;

    FirebaseFirestore db;

    List<RemoveProductModel> removeProductModelList;
    RemoveProduct removeProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //first recycler view
        View view = inflater.inflate(R.layout.fragment_remove,container,false);

        db = FirebaseFirestore.getInstance();

        recyclerRemoveProduct = view.findViewById(R.id.recyclerRemoveProduct);


        recyclerRemoveProduct.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        removeProductModelList = new ArrayList<>();
        removeProduct = new RemoveProduct(getActivity(), removeProductModelList);
        recyclerRemoveProduct.setAdapter(removeProduct);

        db.collection("FarmingProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String documentId = document.getId();

                                RemoveProductModel product = document.toObject(RemoveProductModel.class);

                                product.setDocumentId(documentId);
                                removeProductModelList.add(product);
                                removeProduct.notifyDataSetChanged();

                            }
                        } else {

                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

        db.collection("ResellerProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String documentId = document.getId();

                                RemoveProductModel product = document.toObject(RemoveProductModel.class);

                                product.setDocumentId(documentId);
                                removeProductModelList.add(product);
                                removeProduct.notifyDataSetChanged();

                            }
                        } else {

                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

        return view;

    }

}
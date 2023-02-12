package com.example.app.krishishop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.app.krishishop.Model.Product;
import com.example.app.krishishop.Model.VeritcalProduct;
import com.example.app.krishishop.adapter.ProductAdapter;
import com.example.app.krishishop.adapter.VerticalProductAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    NestedScrollView nestedScrollView;
    ProgressBar progressBar;

    RecyclerView recyclerFarmingProducts, recyclerResellerProduct;
    FirebaseFirestore db;

    //search product
    EditText search_box;
    private List<VeritcalProduct> VList;
    private RecyclerView rec_search;
    private VerticalProductAdapter VAdapter;
    private List<Product> HList;
    private ProductAdapter HAdapter;


    //vertical products
    List<VeritcalProduct> veritcalProductList;
    VerticalProductAdapter verticalProductAdapter;

    //horizontal products
    List<Product> products;
    ProductAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //first recycler view
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        db = FirebaseFirestore.getInstance();


        recyclerFarmingProducts = view.findViewById(R.id.recyclerFarmingProducts);
        recyclerResellerProduct = view.findViewById(R.id.recyclerResellerProduct);

        nestedScrollView = view.findViewById(R.id.NestedScrollView);
        progressBar = view.findViewById(R.id.ProgressBar);

        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);

        //vertical products
        recyclerFarmingProducts.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        veritcalProductList = new ArrayList<>();
        verticalProductAdapter = new VerticalProductAdapter(getActivity(), veritcalProductList);
        recyclerFarmingProducts.setAdapter(verticalProductAdapter);

        db.collection("FarmingProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                VeritcalProduct veritcalProduct = document.toObject(VeritcalProduct.class);
                                veritcalProductList.add(veritcalProduct);
                                verticalProductAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                nestedScrollView.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

        recyclerResellerProduct.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), products);
        recyclerResellerProduct.setAdapter(productAdapter);

        db.collection("ResellerProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Product product = document.toObject(Product.class);
                                products.add(product);
                                productAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                nestedScrollView.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(getActivity(), "Error" + task.getException(), Toast.LENGTH_LONG).show();

                        }
                    }
                });


        //search view
        search_box = view.findViewById(R.id.search_box);
        rec_search = view.findViewById(R.id.rec_search);

//        VList = new ArrayList<>();
//        VAdapter = new VerticalProductAdapter(getContext(),VList);
        HList = new ArrayList<>();
        HAdapter = new ProductAdapter(getContext(),HList);

        rec_search.setLayoutManager(new LinearLayoutManager(getContext()));
        rec_search.setAdapter(HAdapter);
        rec_search.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().isEmpty()){
                    HList.clear();
                    HAdapter.notifyDataSetChanged();
                }else{
                    searchProductV(s.toString());
                    rec_search.setVisibility(View.VISIBLE);
                    if(HList.isEmpty()){
                        searchProductH(s.toString());
                        rec_search.setVisibility(View.VISIBLE);
                    }
                }

            }
        });


        return view;

    }

    private void searchProductV(String name){

        if(!name.isEmpty()){

            db.collection("FarmingProducts").whereEqualTo("name",name).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful() && task.getResult() != null){
                                HList.clear();
                                HAdapter.notifyDataSetChanged();
                                for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                    Product product = doc.toObject(Product.class);
                                    HList.add(product);
                                    HAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });

        }

    }

    private void searchProductH(String name){

        if(!name.isEmpty()){
            db.collection("ResellerProducts").whereEqualTo("name",name).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && task.getResult() != null){
                                HList.clear();
                                HAdapter.notifyDataSetChanged();
                                for(DocumentSnapshot snapshot : task.getResult().getDocuments()){
                                    Product product = snapshot.toObject(Product.class);
                                    HList.add(product);
                                    HAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

    }

}
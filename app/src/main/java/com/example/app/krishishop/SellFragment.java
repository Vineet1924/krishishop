package com.example.app.krishishop;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.app.krishishop.Model.NewProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;


public class SellFragment extends Fragment {

    EditText edtPName,edtPPrice,edtPDescription,edtPType;
    Button btnSelectPic,btnUploadProduct;
    ImageView imgProductUpload,imgHistory;
    RadioGroup radioGrp;
    RadioButton radioFarmer,radioReseller;

    final int PICK_IMAGE_REQUEST = 22;
    int selectedId;
    private Uri filePath;
    private String identity;

    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth mAuth;

    Double[] tempNumbers = {1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0};
    String rating = tempNumbers[new Random().nextInt(tempNumbers.length)].toString();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        mAuth = FirebaseAuth.getInstance();

        edtPName = view.findViewById(R.id.edtPName);
        edtPPrice = view.findViewById(R.id.edtPPrice);
        btnSelectPic = view.findViewById(R.id.btnSelectPic);
        imgProductUpload = view.findViewById(R.id.imgProductUpload);
        edtPDescription = view.findViewById(R.id.edtPDescription);
        edtPType = view.findViewById(R.id.edtPType);
        btnUploadProduct = view.findViewById(R.id.btnUploadProduct);
        radioGrp = view.findViewById(R.id.radioGrp);
        radioFarmer = view.findViewById(R.id.radioFarmer);
        radioReseller = view.findViewById(R.id.radioReseller);
        imgHistory = view.findViewById(R.id.imgHistory);

        btnSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        imgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SellHistory.class));
            }
        });

        btnUploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });

        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedId = radioGrp.getCheckedRadioButtonId();
                if(selectedId ==  radioFarmer.getId()){
                    identity = "Farmer";
                    Toast.makeText(getActivity(), identity, Toast.LENGTH_SHORT).show();
                }else if(selectedId == radioReseller.getId()){
                    identity = "Reseller";
                    Toast.makeText(getActivity(), identity, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imgProductUpload.setImageURI(filePath);
            imgProductUpload.setVisibility(View.VISIBLE);
            btnUploadProduct.setVisibility(View.VISIBLE);
        }
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void UploadImage() {

        Random generator = new Random();
        int x = generator.nextInt();

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            if(identity == "Farmer"){

                StorageReference picsRef = storageReference.child("Vproduct/"+x+".jpg");
                picsRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Story posted", Toast.LENGTH_SHORT);

                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        UploadProduct(uri.toString());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Story couldn't be posted", Toast.LENGTH_SHORT);
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });

            }else if(identity == "Reseller"){

                StorageReference picsRef = storageReference.child("Hproduct/"+x+".jpg");
                picsRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Story posted", Toast.LENGTH_SHORT);

                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        UploadProduct(uri.toString());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Story couldn't be posted", Toast.LENGTH_SHORT);
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });

            }

        }
    }

    private void UploadProduct(String url) {

        String ProductName = edtPName.getText().toString();
        String ProductDescription = edtPDescription.getText().toString();
        String ProductPrice = edtPPrice.getText().toString();
        String ProductType = edtPType.getText().toString();

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        if(TextUtils.isEmpty(ProductName)){
            edtPName.setError("Please Enter Product Name");
        }else if(TextUtils.isEmpty(ProductDescription)){
            edtPDescription.setError("Please Enter Product Description");
        }else if(TextUtils.isEmpty(ProductPrice)){
            edtPPrice.setError("Please Enter Product Price");
        }else if(TextUtils.isEmpty(ProductType)){
            edtPType.setError("Please Enter Product Type");
        }else{

            if(identity == "Farmer"){

                CollectionReference reference = db.collection("FarmingProducts");
                NewProduct newProduct = new NewProduct(ProductName,ProductDescription,ProductPrice,ProductType,url,rating);
                reference.add(newProduct).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Product Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Product Not Added", Toast.LENGTH_SHORT).show();
                    }
                });

                CollectionReference referenceHistory = db.collection("History").document("sellHistory").collection(mAuth.getCurrentUser().getUid());
                final HashMap<String, Object> sellHistory = new HashMap<>();

                sellHistory.put("productName",newProduct.getName());
                sellHistory.put("productPrice",newProduct.getPrice());
                sellHistory.put("productImage",newProduct.getImg_url());
                sellHistory.put("sellDate",saveCurrentDate);

                referenceHistory.add(sellHistory).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("status","Successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("status","Failed");

                    }
                });

            }else if(identity == "Reseller"){

                CollectionReference reference = db.collection("ResellerProducts");

                NewProduct newProduct = new NewProduct(ProductName,ProductDescription,ProductPrice,ProductType,url,rating);

                reference.add(newProduct).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Product Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Product Not Added", Toast.LENGTH_SHORT).show();
                    }
                });

                CollectionReference referenceHistory = db.collection("History").document("sellHistory").collection(mAuth.getCurrentUser().getUid());
                final HashMap<String, Object> sellHistory = new HashMap<>();

                sellHistory.put("productName",newProduct.getName());
                sellHistory.put("productPrice",newProduct.getPrice());
                sellHistory.put("productImage",newProduct.getImg_url());
                sellHistory.put("sellDate",saveCurrentDate);

                referenceHistory.add(sellHistory).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("status","Successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("status","Failed");

                    }
                });
            }

        }
    }

}
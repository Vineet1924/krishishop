package com.example.app.krishishop;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ResellerProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private Uri filePath;

    TextView ProfileUsername,ProfileEmail,ProfilePassword,ProfileCity,ProfilePhone,ProfileStoreId,txtChangeProfileImg;
    Button btnLogOut,btnHistory;
    CircularImageView imgProfile;
    ImageButton iconUsername,iconPassword,iconCity,iconPhone,iconStoreId;

    final int PICK_IMAGE_REQUEST = 22;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reseller_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        ProfileEmail = view.findViewById(R.id.ProfileEmail);
        ProfilePassword = view.findViewById(R.id.ProfilePassword);
        ProfileUsername = view.findViewById(R.id.ProfileUsername);
        ProfileCity = view.findViewById(R.id.ProfileCity);
        ProfilePhone = view.findViewById(R.id.ProfilePhone);
        ProfileStoreId = view.findViewById(R.id.ProfileStoreId);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnHistory = view.findViewById(R.id.btnHistory);
        imgProfile = view.findViewById(R.id.imgProfile);
        txtChangeProfileImg = view.findViewById(R.id.txtChangeProfileImg);
        iconStoreId = view.findViewById(R.id.iconStoreId);
        iconPhone = view.findViewById(R.id.iconPhone);
        iconCity = view.findViewById(R.id.iconCity);
        iconUsername = view.findViewById(R.id.iconUsername);
        iconPassword = view.findViewById(R.id.iconPassword);

        databaseReference = firebaseDatabase.getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String email = ""+ds.child("email").getValue();
                    String password = ""+ds.child("password").getValue();
                    String username = ""+ds.child("username").getValue();
                    String city = ""+ds.child("city").getValue();
                    String phone = ""+ds.child("mobileno").getValue();
                    String storeId = ""+ds.child("storeid").getValue();

                    ProfileUsername.setText(username);
                    ProfileEmail.setText(email);
                    ProfileCity.setText(city);
                    ProfilePassword.setText(password);
                    ProfilePhone.setText(phone);
                    ProfileStoreId.setText(storeId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(),ChoicesActivity.class));
                getActivity().finish();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ProductHistoryActivity.class);
                startActivity(i);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        txtChangeProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });

        iconPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Forgotpassword.class));
            }
        });

        iconUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomUsername();
            }
        });

        iconCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCutomCity();
            }
        });

        iconPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomPhone();
            }
        });

        iconStoreId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomStoreId();
            }
        });

        StorageReference profileref = storageReference.child("images/"+firebaseUser.getUid()+".jpg");
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "No Profile Picture Found", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imgProfile.setImageURI(filePath);
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
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference picsRef = storageReference.child("images/"+firebaseUser.getUid()+".jpg");
            picsRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Story posted", Toast.LENGTH_SHORT);
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

    private void ShowCustomUsername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter new username");

        final View customUser = getLayoutInflater().inflate(R.layout.custom_dialogbox, null);
        builder.setView(customUser);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editUsername = customUser.findViewById(R.id.dialogEditText);
                HashMap hashMap = new HashMap();
                hashMap.put("username",editUsername.getText().toString());
                databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getActivity(), "Username Updated", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowCutomCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter new city name");

        final View customCity = getLayoutInflater().inflate(R.layout.custom_dialogbox, null);
        builder.setView(customCity);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editCity = customCity.findViewById(R.id.dialogEditText);
                HashMap hashMap = new HashMap();
                hashMap.put("city",editCity.getText().toString());
                databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getActivity(), "City Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowCustomPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter new mobile number");

        final View customPhone = getLayoutInflater().inflate(R.layout.custom_dialogbox, null);
        builder.setView(customPhone);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editPhone = customPhone.findViewById(R.id.dialogEditText);
                HashMap hashMap = new HashMap();
                hashMap.put("mobileno",editPhone.getText().toString());
                databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getActivity(), "Mobile-no Updated", Toast.LENGTH_SHORT).show();
                    }
                });            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void ShowCustomStoreId() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter new store-id");

        final View customStoreId = getLayoutInflater().inflate(R.layout.custom_dialogbox, null);
        builder.setView(customStoreId);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editStoreId = customStoreId.findViewById(R.id.dialogEditText);
                HashMap hashMap = new HashMap();
                hashMap.put("storeid",editStoreId.getText().toString());
                databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getActivity(), "Store ID Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
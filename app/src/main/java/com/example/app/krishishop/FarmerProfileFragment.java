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


public class FarmerProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private Uri filePath;

    TextView txtChangeProfileImg, ProfileUsername, ProfileEmail, ProfilePassword, ProfilePhone;
    CircularImageView imgProfile;
    Button btnLogOut, btnHistory;
    ImageButton iconUsername, iconPassword, iconPhone;

    final int PICK_IMAGE_REQUEST = 22;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farmer_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        ProfileEmail = view.findViewById(R.id.ProfileEmail);
        ProfilePassword = view.findViewById(R.id.ProfilePassword);
        ProfileUsername = view.findViewById(R.id.ProfileUsername);
        ProfilePhone = view.findViewById(R.id.ProfilePhone);
        imgProfile = view.findViewById(R.id.imgProfile);
        txtChangeProfileImg = view.findViewById(R.id.txtChangeProfileImg);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnHistory = view.findViewById(R.id.btnHistory);
        iconPhone = view.findViewById(R.id.iconPhone);
        iconPassword = view.findViewById(R.id.iconPassword);
        iconUsername = view.findViewById(R.id.iconUsername);

        databaseReference = firebaseDatabase.getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = "" + ds.child("email").getValue();
                    String password = "" + ds.child("password").getValue();
                    String username = "" + ds.child("username").getValue();
                    String phone = "" + ds.child("mobileno").getValue();

                    ProfileUsername.setText(username);
                    ProfileEmail.setText(email);
                    ProfilePassword.setText(password);
                    ProfilePhone.setText(phone);
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
                startActivity(new Intent(getActivity(), ChoicesActivity.class));
                getActivity().finish();

            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductHistoryActivity.class);
                startActivity(intent);
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
                startActivity(new Intent(getActivity(), Forgotpassword.class));
            }
        });

        iconPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomPhone();
            }
        });

        iconUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomUsername();
            }
        });

        StorageReference profileref = storageReference.child("images/" + firebaseUser.getUid() + ".jpg");
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

            StorageReference picsRef = storageReference.child("images/" + firebaseUser.getUid() + ".jpg");
            picsRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Profile image uploaded", Toast.LENGTH_SHORT);
                            txtChangeProfileImg.setVisibility(View.GONE);
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
                hashMap.put("username", editUsername.getText().toString());
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
                hashMap.put("mobileno", editPhone.getText().toString());
                databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(getActivity(), "Mobile-no Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
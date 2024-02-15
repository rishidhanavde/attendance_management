package com.rishiprojects.attendancemanagement.admin_files.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rishiprojects.attendancemanagement.R;
import com.rishiprojects.attendancemanagement.admin_files.Homepage;
import com.rishiprojects.attendancemanagement.admin_files.login.AdminSignup;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {
    private ImageView updateAdminImage;
    private TextInputLayout textInputName, textInputUsername, textInputEmail;
    private String nameInput, emailInput, usernameInput;
    private String adminName, adminEmail, adminUsername, adminImage;
    ProgressBar progressBar;

    private StorageReference storageReference;
    FirebaseUser user;
    DatabaseReference reference;
    String userID;

    private Bitmap bitmap = null;
    String downloadUrl = "";
    private final int REQ = 1;
    private final int REQ_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_profile);
        adminName = getIntent().getStringExtra("adminName");
        adminEmail = getIntent().getStringExtra("adminEmail");
        adminUsername = getIntent().getStringExtra("adminUsername");
        adminImage = getIntent().getStringExtra("adminImage");

        progressBar = findViewById(R.id.progressBar);
        textInputName = findViewById(R.id.text_input_name);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputEmail = findViewById(R.id.text_input_email);
        updateAdminImage = findViewById(R.id.update_admin_image);
        updateAdminImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminImage == null) {
                    updateProfile();
                } else {
                    askForProfileRemoval();
                }
            }
        });

        MaterialButton updateAdminButton = findViewById(R.id.update_admin);
        updateAdminButton.setOnClickListener(v -> validateInfo());

        Objects.requireNonNull(textInputName.getEditText()).setText(adminName);
        Objects.requireNonNull(textInputEmail.getEditText()).setText(adminEmail);
        Objects.requireNonNull(textInputUsername.getEditText()).setText(adminUsername);

        try {
            Picasso.get().load(adminImage).into(updateAdminImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "An error occured", Toast.LENGTH_SHORT).show();
            finish();
        }
        assert user != null;
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Admin");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void askForProfileRemoval() {
        //We have to add an AlertBox here which will ask do you want to remove or change profile.
        //If remove, it will be deleted from database. If change, the gallery will open
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Profile");
        builder.setMessage("Do you want to update profile or remove it?");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateProfile();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adminImage = null;
                updateAdminImage.setImageDrawable(getResources().getDrawable(R.drawable.blank_profile_pic));
                bitmap = null;
            }
        });
        builder.show();
    }

    private void updateProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Update Profile Picture");
        builder.setItems(new CharSequence[]{"Take a Photo", "Choose from Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        //Take Photo
                        openCamera();
                        break;
                    case 1:
                        //Choose From Gallery
                        openGallery();
                        break;
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQ_CAMERA);
        }
    }

    private void openGallery() {
        Intent pkImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pkImg, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            //Choose from Gallery
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateAdminImage.setImageBitmap(bitmap);
        } else if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {
            //Take Photo
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            updateAdminImage.setImageBitmap(imageBitmap);
            //now, the imageBitmap can be uploaded in 'bitmap'
            bitmap = imageBitmap;
        }
    }

    public boolean validateName() {
        nameInput = Objects.requireNonNull(textInputName.getEditText()).getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else {
            textInputName.setError(null);
            return true;
        }
    }

    public boolean validateEmail() {
        emailInput = Objects.requireNonNull(textInputEmail.getEditText()).getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!emailInput.equals(adminEmail)) {
            textInputEmail.setError("Field can't be changed");
            textInputEmail.getEditText().setText(adminEmail);
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    public boolean validateUsername() {
        usernameInput = Objects.requireNonNull(textInputUsername.getEditText()).getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username is too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    public void validateInfo() {
        progressBar.setVisibility(View.VISIBLE);
        if (!validateName() | !validateEmail() | !validateUsername()) {
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (usernameInput.equals(adminUsername)) {
            validateBitmap();
        } else {
            checkUsernameAvailability(usernameInput);
        }
    }

    private void checkUsernameAvailability(final String username) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");

        adminRef.orderByChild("Username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The username is already in use
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfile.this, "Username is already taken. Please choose a different username.", Toast.LENGTH_SHORT).show();
                    textInputUsername.setError("Username already taken");
                } else {
                    // The username is available, proceed with user registration
                    validateBitmap();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the database query is canceled
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfile.this, "Error checking username availability.", Toast.LENGTH_SHORT).show();
                Log.e("CheckUsername", "Error checking username availability", databaseError.toException());
            }
        });
    }

    public void validateBitmap() {
        if (bitmap == null) {
            dialogBox(this);
        } else {
            updateImage();
        }
    }

    private void dialogBox(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("No Picture Selected");
        builder.setMessage("Are you sure you don't want to change the profile pic?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateData(adminImage);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressBar.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void updateImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Admins").child(finalImage + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(EditProfile.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    updateData(downloadUrl);
                                }
                            });
                        }
                    });
                }
               else {
                   Toast.makeText(EditProfile.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private void updateData(String imageInput) {
        HashMap hm = new HashMap();
        hm.put("Name", nameInput);
        hm.put("Email", emailInput);
        hm.put("Username", usernameInput);
        hm.put("Image", imageInput);

        reference.child(userID).updateChildren(hm).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfile.this, "Info Updated Successfully!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfile.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
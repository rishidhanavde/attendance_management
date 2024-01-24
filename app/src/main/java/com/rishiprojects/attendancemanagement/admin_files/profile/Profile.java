package com.rishiprojects.attendancemanagement.admin_files.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishiprojects.attendancemanagement.LoginAs;
import com.rishiprojects.attendancemanagement.R;
import com.rishiprojects.attendancemanagement.admin_files.User;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    TextView AdminName, AdminUsername, AdminEmail;
    EditText Admin_UID;
    ImageView AdminProfilePic;

    MaterialButton EditProfile, Logout;
    String adminName, adminUsername, adminEmail, adminUID, adminImage;

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        AdminName = findViewById(R.id.admin_name);
        AdminUsername = findViewById(R.id.admin_username);
        AdminEmail = findViewById(R.id.admin_email);
        AdminProfilePic = findViewById(R.id.admin_profile_pic);
        Admin_UID = findViewById(R.id.admin_unique_key);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Admin");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    adminName = userProfile.Name;
                    adminUsername = userProfile.Username;
                    adminImage = userProfile.Image;
                    adminEmail = userProfile.Email;
                    adminUID = userProfile.UID;

                    AdminName.setText(adminName);
                    AdminUsername.setText(adminUsername);
                    AdminEmail.setText(adminEmail);
                    Admin_UID.setText(adminUID);
                    try {
                        Picasso.get().load(adminImage).into(AdminProfilePic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Profile.this, "Database Error!!", Toast.LENGTH_SHORT).show();
            }
        });


        Logout = findViewById(R.id.admin_logout);
        Logout.setOnClickListener(v -> logout(this));
        EditProfile = findViewById(R.id.edit_profile);
        EditProfile.setOnClickListener(v -> editProfile());
    }

    private void editProfile() {
        Intent intent = new Intent(Profile.this, EditProfile.class);
        intent.putExtra("adminName", adminName);
        intent.putExtra("adminEmail", adminEmail);
        intent.putExtra("adminUsername", adminUsername);
        intent.putExtra("adminImage", adminImage);
        startActivity(intent);
    }

    private void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(activity, "Logout Successful!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profile.this, LoginAs.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
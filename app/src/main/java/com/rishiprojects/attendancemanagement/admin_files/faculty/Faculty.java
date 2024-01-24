package com.rishiprojects.attendancemanagement.admin_files.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishiprojects.attendancemanagement.R;
import com.rishiprojects.attendancemanagement.admin_files.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Faculty extends AppCompatActivity {
    FloatingActionButton Fab;
    LinearLayout AdminInfo1,PleaseWait, NoDataFound;
    RecyclerView TeachersList;
    TeacherAdapter adapter;
    TextView AdminNameInFaculty, AdminUsernameInFaculty, AdminEmailInFaculty;
    ImageView AdminPhotoInFaculty;
    MaterialButton AdminUpdateButtonInFaculty;
    String adminName, adminEmail, adminUsername, adminImage;
    List<Teacher> list;
    DatabaseReference reference, dbRef, adRef;
    String userID;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_faculty);

        ProgressBar = findViewById(R.id.progress_bar);
        ProgressBar.setVisibility(View.VISIBLE);

        AdminInfo1 = findViewById(R.id.admin_info_1);
        PleaseWait = findViewById(R.id.please_wait);
        NoDataFound = findViewById(R.id.no_data_found);
        TeachersList = findViewById(R.id.teachers_list);

        AdminNameInFaculty = findViewById(R.id.admin_name_in_faculty);
        AdminUsernameInFaculty = findViewById(R.id.admin_username_in_faculty);
        AdminEmailInFaculty = findViewById(R.id.admin_email_in_faculty);
        AdminPhotoInFaculty = findViewById(R.id.admin_photo_in_faculty);
        AdminUpdateButtonInFaculty = findViewById(R.id.admin_update_button_in_faculty);
        AdminUpdateButtonInFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We will take the page to Edit Profile. Just the problem is how will we get the data?
                Toast.makeText(Faculty.this, "Work to be done", Toast.LENGTH_SHORT).show();
            }
        });

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Admin/" + userID);
        adRef = FirebaseDatabase.getInstance().getReference("Admin");

        Fab = findViewById(R.id.fab);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Faculty.this, AddTeacher.class));
            }
        });

        adminInfo();
        TeachersList();
    }

    private void adminInfo() {
        adRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    PleaseWait.setVisibility(View.GONE);
                    AdminInfo1.setVisibility(View.VISIBLE);

                    adminName = userProfile.Name;
                    adminUsername = userProfile.Username;
                    adminEmail = userProfile.Email;
                    adminImage = userProfile.Image;

                    AdminNameInFaculty.setText(adminName);
                    AdminUsernameInFaculty.setText(adminUsername);
                    AdminEmailInFaculty.setText(adminEmail);
                    try {
                        Picasso.get().load(adminImage).into(AdminPhotoInFaculty);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void TeachersList() {
        dbRef = reference.child("Teacher");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                if (!snapshot.exists()) {
                    NoDataFound.setVisibility(View.VISIBLE);
                    TeachersList.setVisibility(View.GONE);
                    ProgressBar.setVisibility(View.GONE);
                } else {
                    NoDataFound.setVisibility(View.GONE);
                    TeachersList.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Teacher data = snapshot1.getValue(Teacher.class);
                        list.add(data);
                    }
                    TeachersList.setHasFixedSize(true);
                    TeachersList.setLayoutManager(new LinearLayoutManager(Faculty.this));
                    adapter = new TeacherAdapter(list, Faculty.this);
                    TeachersList.setAdapter(adapter);
                    ProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Faculty.this, error.getMessage(), Toast.LENGTH_LONG).show();
                ProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
package com.rishiprojects.attendancemanagement.admin_files;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rishiprojects.attendancemanagement.LoginAs;
import com.rishiprojects.attendancemanagement.R;
import com.rishiprojects.attendancemanagement.admin_files.attendance.DownloadAttendance;
import com.rishiprojects.attendancemanagement.admin_files.attendance.ViewAttendance;
import com.rishiprojects.attendancemanagement.admin_files.classroom.ViewClassrooms;
import com.rishiprojects.attendancemanagement.admin_files.faculty.Faculty;
import com.rishiprojects.attendancemanagement.admin_files.profile.Profile;
import com.rishiprojects.attendancemanagement.admin_files.students.AddStudents;

public class Homepage extends AppCompatActivity {
    CardView profile, faculty, viewAttendance, viewClassrooms, addStudents, downloadAttendance;
    Button LogoutButton;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_homepage);

        profile = findViewById(R.id.Profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, Profile.class));
            }
        });

        faculty = findViewById(R.id.Faculty);
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, Faculty.class));
            }
        });

        addStudents = findViewById(R.id.AddStudent);
        addStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, AddStudents.class));
            }
        });

        viewClassrooms = findViewById(R.id.ViewClassroom);
        viewClassrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, ViewClassrooms.class));
            }
        });

        viewAttendance = findViewById(R.id.ViewAttendance);
        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, ViewAttendance.class));
            }
        });

        downloadAttendance = findViewById(R.id.DownloadAttendance);
        downloadAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, DownloadAttendance.class));
            }
        });


        LogoutButton = findViewById(R.id.logout_button);
        LogoutButton.setOnClickListener(v -> logout(this));
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
                Intent intent = new Intent(Homepage.this, LoginAs.class);
                startActivity(intent);
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
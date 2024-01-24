package com.rishiprojects.attendancemanagement.teacher_files;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rishiprojects.attendancemanagement.R;
import com.rishiprojects.attendancemanagement.teacher_files.attendance.ViewAttendance;
import com.rishiprojects.attendancemanagement.teacher_files.classroom.Classrooms;
import com.rishiprojects.attendancemanagement.teacher_files.profile.Profile;
import com.rishiprojects.attendancemanagement.teacher_files.take_attendance.TakeAttendance;

public class Homepage extends AppCompatActivity {
    CardView profile, classrooms, takeAttendance, viewAttendance, downloadAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_homepage);

        profile = findViewById(R.id.teachProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, Profile.class));
            }
        });

        classrooms = findViewById(R.id.teachClassrooms);
        classrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, Classrooms.class));
            }
        });

        takeAttendance = findViewById(R.id.takeAttendance);
        takeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, TakeAttendance.class));
            }
        });

        viewAttendance = findViewById(R.id.viewAttendance);
        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, ViewAttendance.class));
            }
        });
    }
}
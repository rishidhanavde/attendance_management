package com.rishiprojects.attendancemanagement.admin_files.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.rishiprojects.attendancemanagement.R;

public class ViewAttendance extends AppCompatActivity {

    MaterialButton ViewAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_attendance);

        ViewAttendance = findViewById(R.id.admin_view_attendance);
        ViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewAttendance.this, Attendance.class));
            }
        });
    }
}
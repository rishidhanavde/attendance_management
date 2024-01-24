package com.rishiprojects.attendancemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rishiprojects.attendancemanagement.admin_files.login.AdminLogin;
import com.rishiprojects.attendancemanagement.teacher_files.TeacherLogin;

public class LoginAs extends AppCompatActivity {

    Button LoginAsAdmin, LoginAsTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginas);

        LoginAsAdmin = findViewById(R.id.admin_login);
        LoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAs.this, AdminLogin.class));
                finish();
            }
        });

        LoginAsTeacher = findViewById(R.id.teacher_login);
        LoginAsTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAs.this, TeacherLogin.class));
                finish();
            }
        });
    }
}
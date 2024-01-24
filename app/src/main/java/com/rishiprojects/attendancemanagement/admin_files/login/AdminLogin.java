package com.rishiprojects.attendancemanagement.admin_files.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.rishiprojects.attendancemanagement.ForgotPw;
import com.rishiprojects.attendancemanagement.LoginAs;
import com.rishiprojects.attendancemanagement.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rishiprojects.attendancemanagement.admin_files.Homepage;

public class AdminLogin extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    String emailInput, passwordInput;
    ProgressBar progressBar;
    MaterialButton LoginButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        mAuth = FirebaseAuth.getInstance();

        textInputEmail = findViewById(R.id.Temail);
        textInputPassword = findViewById(R.id.Tpassword);
        progressBar = findViewById(R.id.progressBar);
        LoginButton = (MaterialButton) findViewById(R.id.loginbtn);
        LoginButton.setOnClickListener(v -> loginBtn());
    }

    public boolean validateEmail() {
        emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid Email");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    private void loginBtn() {
        if (!validateEmail() | !validatePassword())
            return;

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AdminLogin.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminLogin.this, Homepage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                user.sendEmailVerification();
                                textInputEmail.setError("Please verify Email to proceed");
                                Toast.makeText(AdminLogin.this, "Check Email to verify!!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            textInputEmail.setError("Email ID or Password Incorrect");
                            textInputPassword.getEditText().setText("");
                            Toast.makeText(AdminLogin.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void regisPg(View view) {
        Intent intent = new Intent(AdminLogin.this, AdminSignup.class);
        startActivity(intent);
    }

    public void forgotPw(View view) {
        Intent intent = new Intent(AdminLogin.this, ForgotPw.class);
        startActivity(intent);
    }
}
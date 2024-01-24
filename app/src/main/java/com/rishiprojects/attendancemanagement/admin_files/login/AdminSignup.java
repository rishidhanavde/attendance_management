package com.rishiprojects.attendancemanagement.admin_files.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rishiprojects.attendancemanagement.R;
import com.rishiprojects.attendancemanagement.admin_files.User;

public class AdminSignup extends AppCompatActivity {
    private TextInputLayout textInputName;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputConfirmPassword;
    private ProgressBar progressBar;
    String downloadUrl = "";
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference MyRef;
    String userID;
    MaterialButton RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signup);
        mAuth = FirebaseAuth.getInstance();

        textInputName = findViewById(R.id.signup_name);
        textInputUsername = findViewById(R.id.signup_username);
        textInputEmail = findViewById(R.id.signup_email);
        textInputPassword = findViewById(R.id.signup_password);
        textInputConfirmPassword = findViewById(R.id.signup_confirm_password);
        progressBar = findViewById(R.id.progressBar);
    }

    String nameInput, emailInput, usernameInput, passwordInput;

    public boolean validateEmail() {
        emailInput = textInputEmail.getEditText().getText().toString().trim();
        nameInput = textInputName.getEditText().getText().toString().trim();

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

    public boolean validateUsername() {
        usernameInput = textInputUsername.getEditText().getText().toString().trim();

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


    public boolean validatePassword() {
        passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String confirmPasswordInput = textInputConfirmPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 5) {
            textInputPassword.setError("Password must be at least 5 characters");
            return false;
        } else if (!passwordInput.equals(confirmPasswordInput)) {
            textInputConfirmPassword.setError("Password does not match");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void register(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        checkUsernameAvailability(usernameInput);
    }

    private void checkUsernameAvailability(final String username) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");

        adminRef.orderByChild("Username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The username is already in use
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminSignup.this, "Username is already in use. Please choose a different username.", Toast.LENGTH_SHORT).show();
                    textInputUsername.setError("Username already taken");
                } else {
                    // The username is available, proceed with user registration
                    checkEmailAvailability(emailInput);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the database query is canceled
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminSignup.this, "Error checking username availability.", Toast.LENGTH_SHORT).show();
                Log.e("CheckUsername", "Error checking username availability", databaseError.toException());
            }
        });
    }

    private void checkEmailAvailability(final String email) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");

        adminRef.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The username is already in use
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminSignup.this, "Email ID is already in use. Please use a different Email.", Toast.LENGTH_SHORT).show();
                    textInputEmail.setError("Email Id already in use");
                } else {
                    // The username is available, proceed with user registration
                    createUserWithEmailAndPassword();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if the database query is canceled
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminSignup.this, "Error checking username availability.", Toast.LENGTH_SHORT).show();
                Log.e("CheckUsername", "Error checking username availability", databaseError.toException());
            }
        });
    }

    private void createUserWithEmailAndPassword() {
        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName("admin").build();
                    firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.e("UserType", "User type successfully set");
                                    }
                                }
                            });
                    User user = new User(nameInput, emailInput, usernameInput, downloadUrl, userID);
                    FirebaseDatabase.getInstance().getReference("Admin")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(AdminSignup.this, "Registered Successfully!!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(AdminSignup.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminSignup.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
                    Log.d("SignInError", task.getException().getMessage());
                }
            }
        });
    }

//    public void register(View v) {
//        if (!validateEmail() | !validateUsername() | !validatePassword()) {
//            return;
//        }
//        progressBar.setVisibility(View.VISIBLE);
//
//        checkUsernameAvailability(usernameInput);
//        if (!checkUsernameAvailability(usernameInput)) {
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setDisplayName("admin").build();
//                    firebaseUser.updateProfile(profileUpdates)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d("UserType", "User type successfully set");
//                                    }
//                                }
//                            });
//                    User user = new User(nameInput, emailInput, usernameInput, downloadUrl, userID);
//                    FirebaseDatabase.getInstance().getReference("Admin")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        progressBar.setVisibility(View.GONE);
//                                        FirebaseAuth.getInstance().signOut();
//                                        Toast.makeText(AdminSignup.this, "Registered Successfully!!", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    } else {
//                                        progressBar.setVisibility(View.GONE);
//                                        Toast.makeText(AdminSignup.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                } else {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(AdminSignup.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
//                    Log.d("SignInError", task.getException().getMessage());
//                }
//            }
//        });
//    }
}

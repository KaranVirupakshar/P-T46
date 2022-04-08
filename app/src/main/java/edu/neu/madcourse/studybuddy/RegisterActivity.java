package edu.neu.madcourse.studybuddy;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import util.CustomSnackBar;
import util.User;


public class RegisterActivity extends AppCompatActivity {

    // Variables in XML
    private EditText inputUsername, inputPassword, inputRetypePassword, inputFirstName,
            inputLastName, inputLocation;
    private TextView signupHeader;
    private Button btnSignUp;
    private ProgressBar progressBar;

    // Snackbar for displaying feedback
    CustomSnackBar snackBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        System.out.println("Starting register activity");

        btnSignUp = findViewById(R.id.sign_up_button);
        inputUsername = findViewById(R.id.username_input);
        inputPassword = findViewById(R.id.password);
        inputRetypePassword = findViewById(R.id.retype_password);
        inputFirstName = findViewById(R.id.firstName);
        inputLastName = findViewById(R.id.lastName);
        inputLocation = findViewById(R.id.location);
        progressBar = findViewById(R.id.progressBar);
        this.signupHeader = findViewById(R.id.signUpHeader);

        snackBar = new CustomSnackBar();

        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String retype_password = inputRetypePassword.getText().toString().trim();
                final String firstName = inputFirstName.getText().toString().trim();
                final String lastName = inputLastName.getText().toString().trim();
                final String location = inputLocation.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    snackBar
                            .display(v, getApplicationContext(), "Please enter First Name!", R.color.lightBlue);
                    return;
                } else if (TextUtils.isEmpty(lastName)) {
                    snackBar
                            .display(v, getApplicationContext(), "Please enter Last Name!", R.color.lightBlue);
                    return;
                } else if (TextUtils.isEmpty(username)) {
                    snackBar.display(v, getApplicationContext(), "Please enter username!", R.color.lightBlue);
                    return;
                } else if (TextUtils.isEmpty(location)) {
                    snackBar.display(v, getApplicationContext(), "Please enter location!", R.color.lightBlue);
                    return;
                } else if (username.contains(" ")) {
                    snackBar.display(v, getApplicationContext(), "Username cannot contain spaces!", R.color.lightBlue);
                    return;
                } else if (!username.toLowerCase().equals(username)) {
                    snackBar.display(v, getApplicationContext(), "Username cannot contain uppercase letters!", R.color.lightBlue);
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    snackBar.display(v, getApplicationContext(), "Please enter password!", R.color.lightBlue);
                    return;
                } else if (firstName.length() > 25) {
                    snackBar.display(v, getApplicationContext(),
                            "First Name is too long, maximum 25 characters!", R.color.lightBlue);
                    return;
                } else if (lastName.length() > 25) {
                    snackBar.display(v, getApplicationContext(),
                            "Last Name is too long, maximum 25 characters!", R.color.lightBlue);
                    return;
                } else if (username.length() > 15) {
                    snackBar.display(v, getApplicationContext(),
                            "Username is too long, maximum 15 characters!", R.color.lightBlue);
                    return;
                } else if (location.length() > 25) {
                    snackBar.display(v, getApplicationContext(),
                            "Location is too long, maximum 25 characters!", R.color.lightBlue);
                    return;
                } else if (password.length() < 6) {
                    snackBar.display(v, getApplicationContext(),
                            "Password too short, enter minimum 6 characters!", R.color.lightBlue);
                    return;
                } else if (!password.equals(retype_password)) {
                    snackBar
                            .display(v, getApplicationContext(), "Passwords do not match!", R.color.lightBlue);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(username + "@studybuddy.com", password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    System.out.println("unsuc");
                                    snackBar.display(v, getApplicationContext(),
                                            "Error: Account creation failed:" + task.getException(), R.color.lightBlue);
                                }
                                else {
                                    System.out.println("suc1");

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    WriteBatch batch = db.batch();
                                    DocumentReference currentUser = db.collection("users").document(username);
                                    batch.set(currentUser,
                                            new User(
                                                    firstName.substring(0, 1).toUpperCase() + firstName.substring(1),
                                                    lastName.substring(0, 1).toUpperCase() + lastName.substring(1),
                                                    username,
                                                    "dummy@gmail.com",
                                                    location.substring(0, 1).toUpperCase() + location.substring(1),
                                                    String.valueOf(new Timestamp(new Date()).getSeconds())
                                            )
                                    );
                                    System.out.println("suc2");

                                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            snackBar.display(v, getApplicationContext(), "Account created successfully.",
                                                    R.color.lightBlue);
                                            System.out.println("comp1");

                                            progressBar.setVisibility(View.INVISIBLE);
                                            finish();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            snackBar.display(v, getApplicationContext(),
                                                    "Error: Failure writing to database.", R.color.lightBlue);
                                            System.out.println("fail1");

                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    System.out.println("suc3");
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }
}


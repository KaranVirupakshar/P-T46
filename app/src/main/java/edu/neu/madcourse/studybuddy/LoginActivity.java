package edu.neu.madcourse.studybuddy;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import util.CustomSnackBar;

public class LoginActivity extends AppCompatActivity {

    private TextView registerTextView, loginHeader, guestVisit;
    private EditText inputUsername, inputPassword;
    private ProgressBar progressBar;
    private Button btnLogin;

    private CustomSnackBar snackBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        snackBar = new CustomSnackBar();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.username_input);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        registerTextView = findViewById(R.id.btn_signup);
        registerTextView.setPaintFlags(registerTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnLogin = findViewById(R.id.btn_login);
        loginHeader = findViewById(R.id.loginHeader);
        //The guests can just browse through the groups, and maybe check details?
        guestVisit = findViewById(R.id.guestLogin);

        this.registerTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //Take the user to the new screen on clicking the text view.
        guestVisit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                String username = inputUsername.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    snackBar.display(v, getApplicationContext(),"Enter username!", R.color.black);
                    return;
                }
                else if (username.contains(" ")) {
                    snackBar.display(v, getApplicationContext(),"Username cannot contain spaces!", R.color.black);
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    snackBar.display(v, getApplicationContext(),"Enter password!", R.color.black);
                    return;
                }

                String email = username + "@studybuddy.com";
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        })
                        .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (password.length() < 6) {
                                    inputPassword.setError(getString(R.string
                                            .minimum_password));
                                } else {
                                    snackBar.display(v, getApplicationContext(),"Incorrect username or password entered");
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
    }
}

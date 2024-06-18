package com.example.memopad;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import androidx.annotation.NonNull;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText emailEditText;
    Button resetPasswordBtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailEditText = findViewById(R.id.email_edit_text);
        resetPasswordBtn = findViewById(R.id.reset_password_btn);
        progressBar = findViewById(R.id.progress_bar);

        resetPasswordBtn.setOnClickListener(v -> resetPassword());
    }

    void resetPassword() {
        String email = emailEditText.getText().toString();

        boolean isValidated = validateData(email);

        if(!isValidated) {
            return;
        }
        resetPasswordInFirebase(email);
    }

    void resetPasswordInFirebase(String email) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Utility.showToast(ForgotPasswordActivity.this, "Reset mail has been sent!");
                            finish();
                        } else {
                            Utility.showToast(ForgotPasswordActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    void changeInProgress (boolean inProgress) {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            resetPasswordBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            resetPasswordBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }

        return true;
    }
}
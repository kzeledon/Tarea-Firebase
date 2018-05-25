package com.example.karina.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarSignUp);

    }

    public void signUp(View view) {

        email = findViewById(R.id.emailtxtSU);
        password = findViewById(R.id.passwordtxtSU);

        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        if(TextUtils.isEmpty(emailStr)) {

            Toast.makeText(getApplicationContext(), "Ingresa un email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(passwordStr)) {

            Toast.makeText(getApplicationContext(), "Ingresa una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(view.VISIBLE);

        auth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "Usuario creado con éxito:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if(!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "La autenticación falló" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}

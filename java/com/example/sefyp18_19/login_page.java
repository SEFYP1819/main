package com.example.sefyp18_19;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_page extends AppCompatActivity {
    private Button register_btn, login_btn;
    private EditText user_email, user_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        register_btn=findViewById(R.id.Register_btn);
        login_btn=findViewById(R.id.Login_btn);
        user_email=findViewById(R.id.user_input_email);
        user_password=findViewById(R.id.user_input_password);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==register_btn)    {
                    startActivity(new Intent(login_page.this, Register_page.class));
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==login_btn)   {
                   Login();
                }
            }
        });
    }

    public void Login() {
        mAuth=FirebaseAuth.getInstance();
        String email=user_email.getText().toString().trim();
        String password=user_password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful())   {
                    Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(login_page.this, MainActivity.class));
                }
            }
        });
    }
}

package com.example.sefyp18_19;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Start extends AppCompatActivity {

    private Button register_btn, login_btn, debug_btn;
    private FirebaseAuth mAuth;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        count = 5;

        register_btn=findViewById(R.id.Register_btn);
        login_btn=findViewById(R.id.Login_btn);
        debug_btn=findViewById(R.id.debug_btn);
        debug_btn.setVisibility(View.VISIBLE);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==register_btn)    {
                    startActivity(new Intent(Start.this, Register_page.class));
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==login_btn)    {
                    startActivity(new Intent(Start.this, login_page.class));
                }
            }
        });

        debug_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==debug_btn)   {
                    count--;
                    if (count<=0)   {
                        Login();
                    } else {
                        Toast.makeText(Start.this, count+" times to go debug", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void Login() {
        mAuth= FirebaseAuth.getInstance();
        String email="kit1997330@gmail.com";
        String password="95769632qwer";

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful())   {
                    Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Start.this, debug_mode.class));
                }
            }
        });
    }
}

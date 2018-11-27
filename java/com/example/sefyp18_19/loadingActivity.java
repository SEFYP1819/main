package com.example.sefyp18_19;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class loadingActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;
    FirebaseAuth mAuth;
    FirebaseUser current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.progress);

        mAuth = FirebaseAuth.getInstance();
        current_user=mAuth.getCurrentUser();

        progressBar.setMax(100);

        progressAnimation();
    }

    public void progressAnimation(){
        loadingAnim anim = new loadingAnim(this, progressBar, textView, 0, 100);
        anim.setDuration(8000);
        progressBar.setAnimation(anim);
    }
}

package com.example.sefyp18_19;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.net.URLEncoder;

public class mat extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser current_user;

    Button btn;

    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();

        Intent intent = getIntent();

        index = intent.getStringExtra("index");


        btn = findViewById(R.id.btn);
        btn.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mat.this, loadingActivity.class));
            }
        });

        call("http://172.20.10.7/insert.php");

    }

    public void call(final String urlservice){
        class callmatlab extends AsyncTask<Void, Void, String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

                btn.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlservice);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    String UID = current_user.getUid();

                    String U_index = index;

                    con.setDoOutput(true);

                    con.setDoInput(true);

                    OutputStream outputStream = con.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("UID", "UTF-8")+"="+URLEncoder.encode(UID,"UTF-8")+"&"+URLEncoder.encode("index", "UTF-8")+"="+URLEncoder.encode(U_index,"UTF-8");

                    bufferedWriter.write(post_data);

                    bufferedWriter.flush();

                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    while ((result = bufferedReader.readLine()) != null)  {

                        sb.append(result + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
        }

        callmatlab c = new callmatlab();
        c.execute();
    }
}

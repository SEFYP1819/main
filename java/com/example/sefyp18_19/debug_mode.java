package com.example.sefyp18_19;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class debug_mode extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat;
    String dateString;
    long date;
    TextView datetime;

    Button login, reg, main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_mode);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                datetime = findViewById(R.id.date_time_view);
                                date = System.currentTimeMillis();
                                simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                dateString = simpleDateFormat.format(date);
                                datetime.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        login=findViewById(R.id.login);
        reg=findViewById(R.id.register);
        main=findViewById(R.id.main);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==login)   {
                    startActivity(new Intent(debug_mode.this, login_page.class));
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==reg)   {
                    startActivity(new Intent(debug_mode.this, Register_page.class));
                }
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==main)   {
                    startActivity(new Intent(debug_mode.this, mat.class));
                }
            }
        });


    }
}

package com.example.sefyp18_19;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;

public class showdata extends AppCompatActivity {

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);

        back = findViewById(R.id.back_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToProfile();
            }
        });

        LoadAdjustClose("http://172.20.10.7/load.php");
    }

    public void BackToProfile(){
        startActivity(new Intent(showdata.this, MainActivity.class));
    }

    public void LoadAdjustClose(final String urlservice){

        class Load extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {

                try {
                    display(s);
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {

                    URL url = new URL(urlservice);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");

                    con.setDoInput(true);

                    con.setDoOutput(true);

                    OutputStream outputStream = con.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    /*String post_data = URLEncoder.encode("ETF", "UTF-8")+"="+URLEncoder.encode("XLF", "UTF-8");

                    bufferedWriter.write(post_data);

                    bufferedWriter.flush();*/

                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(con.getInputStream())));

                    String json;

                    while ((json = bufferedReader.readLine()) != null){

                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                } catch(Exception e) {

                    return  null;
                }
            }
        }

        Load l = new Load();
        l.execute();
    }

    private void display(String json) throws JSONException {

        TextView q = findViewById(R.id.q);

        JSONArray jsonArray = new JSONArray(json);

        JSONObject last = jsonArray.getJSONObject(62);

        String h = last.getString("Adjusted");

        q.setText(h);

        LineChart adj = findViewById(R.id.AdjCloseChart);

        adj.setScaleEnabled(true);

        ArrayList<Entry> yValues = new ArrayList<>();

        for (int count = 0; count < 63; count++)    {
            JSONObject obj = jsonArray.getJSONObject(count);
            float x = count;
            yValues.add(new Entry(x, Float.parseFloat(obj.getString("Adjusted"))));
        }

        LineDataSet set1 = new LineDataSet(yValues, "Data set 1");

        set1.setFillAlpha(110);

        set1.setDrawCircles(false);

        set1.setColors(ColorTemplate.rgb("#66ff33"));

        set1.setValueTextColor(ColorTemplate.rgb("#ffffff"));

        set1.setLineWidth(2f);

        LineData lineData = new LineData();

        lineData.addDataSet(set1);

        adj.setData(lineData);

        adj.invalidate();

        adj.animateX(1000);

        Legend legend = adj.getLegend();

        legend.setEnabled(false);

        adj.getXAxis().setTextColor(ColorTemplate.rgb("#ffffff"));

        adj.getAxisRight().setTextColor(ColorTemplate.rgb("#ffffff"));

        adj.getAxisLeft().setTextColor(ColorTemplate.rgb("#ffffff"));
    }
}

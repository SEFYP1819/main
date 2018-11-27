package com.example.sefyp18_19;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileFragment extends Fragment {

    private Calendar calendar;
    private SimpleDateFormat dateFormat, monthFormat;
    private String date, month;
    private String current_user_id;
    private TextView date_view, name_view, view1, view2, view3, view4, view5, view6;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        view1 = getView().findViewById(R.id.RiskAversionIndex);
        view2 = getView().findViewById(R.id.RiskyRisk);
        view3 = getView().findViewById(R.id.RiskyReturn);
        view4 = getView().findViewById(R.id.RiskyWts_1);
        view5 = getView().findViewById(R.id.RiskyWts_2);
        view6 = getView().findViewById(R.id.RiskyWts_3);

        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==view4) {
                    show();
                }
            }
        });

        show_time();

        show_name();

        getJSON("http://172.20.10.7/fetch.php");
    }

    public void show_time() {
        date_view=getView().findViewById(R.id.date_view);
        calendar=Calendar.getInstance();
        dateFormat=new SimpleDateFormat("dd");
        monthFormat=new SimpleDateFormat("MMMM");
        date=dateFormat.format(calendar.getTime());
        month=monthFormat.format(calendar.getTime());
        date_view.setText(date+" "+month);
    }

    public void show_name() {
        mAuth=FirebaseAuth.getInstance();
        current_user=mAuth.getCurrentUser();
        name_view=getView().findViewById(R.id.name_view);

        if (current_user!=null) {
            current_user_id=current_user.getUid();
            DatabaseReference user_database= FirebaseDatabase.getInstance().getReference().child("User").child(current_user_id).child("First Name");

            user_database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name_view.setText("Hello, "+dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void getJSON(final String urlservice){

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                /*Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();*/

                try {
                    loadIntoListView(s);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {

                    URL url = new URL(urlservice);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    String UID = current_user_id;

                    con.setRequestMethod("POST");

                    con.setDoOutput(true);

                    con.setDoInput(true);

                    OutputStream outputStream = con.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("UID","UTF-8")+"="+URLEncoder.encode(UID,"UTF-8");

                    bufferedWriter.write(post_data);

                    bufferedWriter.flush();

                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while ((json = bufferedReader.readLine()) != null)  {

                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        JSONObject obj = jsonArray.getJSONObject(0);

        view1.setText(obj.getString("RiskAversionIndex"));

        view2.setText(obj.getString("RiskyRisk"));

        view3.setText(obj.getString("RiskyReturn"));

        view4.setText(obj.getString("RiskyWts_1"));

        view5.setText(obj.getString("RiskyWts_2"));

        view6.setText(obj.getString("RiskyWts_3"));

        float wts[]={0,0,0};

        String label[] = {"Risky Weight 1","Risky Weight 2", "Risky Weight 3"};

        wts[0] = Float.parseFloat(obj.getString("RiskyWts_1"));

        wts[1] = Float.parseFloat(obj.getString("RiskyWts_2"));

        wts[2] = Float.parseFloat(obj.getString("RiskyWts_3"));

        PieChart pie = getView().findViewById(R.id.chart);

        List<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < wts.length; i++){
            pieEntries.add(new PieEntry(wts[i],label[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Portfolio Weightings");

        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        dataSet.setValueTextColor(ColorTemplate.rgb("#000000"));

        PieData pieData = new PieData(dataSet);

        pie.setData(pieData);

        pie.animateY(1000);

        pie.setHoleRadius(5);

        pie.setHoleColor(121212);

        Legend legend = pie.getLegend();

        legend.setEnabled(false);

        pie.invalidate();

    }

    public void show() {
        startActivity(new Intent(getActivity(), showdata.class));
    }

}

package com.example.nirvoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowGraphActivity extends AppCompatActivity {

    private Float total,thisLoc;
    private float totPer,thiPer;
    private String sTotal,sThisLoc,sTotPer,sThiPer;
    private PieChart pieChart;
    private Button freshBtn;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private TextView this_loc,others;

    DatabaseReference databaseReference3;
    public List<CurrentLocation> locationList;
    double current_latitude, current_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);
        freshBtn = findViewById(R.id.btn_fresh);
        scrollView = findViewById(R.id.scrollView2);
        linearLayout = findViewById(R.id.grap_layout);
        this_loc = findViewById(R.id.this_loc);
        others = findViewById(R.id.others);


        databaseReference3 = FirebaseDatabase.getInstance().getReference("currentLocation");
        locationList = new ArrayList<>();

        String cur_lat = getIntent().getStringExtra("lat");
        //sTotal = MapActivity.stotal;
        String cur_lng = getIntent().getStringExtra("lng");
        //sThisLoc = MapActivity.sthisLoc;

        current_latitude = Double.parseDouble(cur_lat);
        current_longitude = Double.parseDouble(cur_lng);


        //linearLayout.setVisibility(View.VISIBLE);
        pieChart = findViewById(R.id.pie_chart);

        String cnt_req = getIntent().getStringExtra("cnt_req");
        String totl = getIntent().getStringExtra("tot");

        current_latitude = Double.parseDouble(cur_lat);
        current_longitude = Double.parseDouble(cur_lng);

        total = Float.parseFloat(totl);
        thisLoc = Float.parseFloat(cnt_req);

        thiPer = thisLoc/total;
        thiPer = (float) (thiPer*100.0);
        totPer = (total-thisLoc)/total;
        totPer = (float) totPer*100;

        sTotPer = Float.toString(totPer);
        sThiPer = Float.toString(thiPer);
        Log.d("graph", "Total: "+sTotal+" This: "+sThisLoc);
        Log.d("graph", "Total: "+sTotPer+" This: "+sThiPer);

        linearLayout.setVisibility(View.VISIBLE);
        //init();
        drawPieChart();

        freshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initAndDrawChart();

            }
        });

    }

    private void initAndDrawChart() {

        int cntOfReq = 0;

        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                locationList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CurrentLocation currentLocation = dataSnapshot1.getValue(CurrentLocation.class);
                    locationList.add(currentLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("loadLocation", "onClick: size " + (locationList.size()));


        for(int i = 0; i < locationList.size(); i++)
        {
            double end_lat = locationList.get(i).getLatitude();
            double end_lng = locationList.get(i).getLongitude();
            float results[] = new float[10];

            Location.distanceBetween(current_latitude, current_longitude, end_lat, end_lng, results);

            if(results[0] <= 1000)
            {
                cntOfReq++;
            }
            Log.d("loadLocation", "onClick: "+ locationList.get(i).getLatitude() + " " + locationList.get(i).getLongitude() + "Distance: " + results[0]);
        }

        String sTotal = String.valueOf(locationList.size());
        String sThisLoc = String.valueOf(cntOfReq);
        this_loc.setText(sThisLoc);
        others.setText(sTotal);

        total = Float.parseFloat(sTotal);
        thisLoc = Float.parseFloat(sThisLoc);


        thiPer = thisLoc/total;
        thiPer = (float) (thiPer*100.0);
        totPer = (total-thisLoc)/total;
        totPer = (float) totPer*100;

        sTotPer = Float.toString(totPer);
        sThiPer = Float.toString(thiPer);
        Log.d("graph", "Total: "+sTotal+" This: "+sThisLoc);
        Log.d("graph", "Total: "+sTotPer+" This: "+sThiPer);

        linearLayout.setVisibility(View.VISIBLE);
        //init();
        drawPieChart();

    }

    void init(){
        //        sTotal = getIntent().getStringExtra("total");
        sTotal = MapActivity.stotal;
//        sThisLoc = getIntent().getStringExtra("this");
        sThisLoc = MapActivity.sthisLoc;

        this_loc.setText(sThisLoc);
        others.setText(sTotal);
//        total = Float.parseFloat(sTotal);
//        thisLoc = Float.parseFloat(sThisLoc);


        //total = MapActivity.total;
        //thisLoc = MapActivity.thisLoc;


        thiPer = thisLoc/total;
        thiPer = (float) (thiPer*100.0);
        totPer = (total-thisLoc)/total;
        totPer = (float) totPer*100;

        sTotPer = Float.toString(totPer);
        sThiPer = Float.toString(thiPer);
        Log.d("graph", "Total: "+sTotal+" This: "+sThisLoc);
        Log.d("graph", "Total: "+sTotPer+" This: "+sThiPer);
    }


    private void drawPieChart(){

        // array of graph different colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(this, R.color.green));
        colors.add(ContextCompat.getColor(this, R.color.red));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(50);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setRotationAngle(0);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(totPer, ""));
        entries.add(new PieEntry(thiPer, ""));

        PieDataSet set = new PieDataSet(entries, " ");

        // colors according to the dataset
        set.setColors(colors);
//        set.setColors(ColorTemplate.createColors(ColorTemplate.MATERIAL_COLORS));
        PieData data = new PieData(set);
        data.setValueTextColor(Color.TRANSPARENT);
        pieChart.setData(data);


        // adding legends to the desigred positions
        Legend l = pieChart.getLegend();
        l.setEnabled(false);


        // calling method to set center text
        pieChart.setCenterText(generateCenterSpannableText());

        // animation and the center text color
        pieChart.animateY(2000);
        pieChart.setEntryLabelColor(Color.BLACK);


        pieChart.invalidate(); // refresh

    }


    // To display center text with textStyle
    private SpannableString generateCenterSpannableText() {
        if(sThiPer.equals("NaN")) sThiPer="0.0";
        SpannableString s = new SpannableString("RISK \n"+sThiPer+"%");
        s.setSpan(new RelativeSizeSpan(2f), 6, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 6, s.length(), 0);
        return s;
    }

}

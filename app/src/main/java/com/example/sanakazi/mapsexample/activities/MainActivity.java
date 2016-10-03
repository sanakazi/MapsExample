package com.example.sanakazi.mapsexample.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sanakazi.mapsexample.R;

public class MainActivity extends FragmentActivity {

Button btn_showMarkers,btn_showRoute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_showMarkers=(Button)findViewById(R.id.btn_showMarkers);
        btn_showRoute=(Button)findViewById(R.id.btn_showRoute);
        btn_showMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowMarkersActivity.class);
                startActivity(intent);
            }
        });

        btn_showRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowRoute.class);
                startActivity(intent);
            }
        });

    }
}
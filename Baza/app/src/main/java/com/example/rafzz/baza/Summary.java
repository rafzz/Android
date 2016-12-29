package com.example.rafzz.baza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Intent intent = getIntent();
        //String report = intent.getStringExtra("sum");

        TextView tv = (TextView) findViewById(R.id.summ);
        tv.setText(MainActivity.summaryReport);
    }
}

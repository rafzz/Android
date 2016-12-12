package com.example.rafzz.kalkulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        tv = (TextView) findViewById(R.id.textView4);
        tv.setTextSize(20);
        tv.setText(message);

        //ViewGroup layout = (ViewGroup) findViewById(R.id.activity_history);
        //layout.addView(tv);
    }


    public void finish(View view) {
        finish();
    }

    public void cls(){
        TextView ttv = (TextView) findViewById(R.id.textView4);
        ttv.setText("");
    }

    public static void clsH(){

    }
}

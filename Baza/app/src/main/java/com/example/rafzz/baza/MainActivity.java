package com.example.rafzz.baza;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int DODAJ_ACTIVITY_REQUEST_CODE = 1;

    private static boolean edit = false;
    public static boolean isEdit() { return edit; }
    public static void setEdit(boolean ifedit) { MainActivity.edit = ifedit; }


    private LinqBaza zb = new LinqBaza(this);

    public static String getSummaryReport() {return summaryReport; }

    public static void setSummaryReport(String summaryReport) {MainActivity.summaryReport = summaryReport; }

    private static String summaryReport = "";


    private ArrayList<TextView> listTV = new ArrayList<TextView>() {};

    private final String editSummary = "\nEDIT: ";
    private final String removeSummary = "\nREMOVED ID: ";
    private final String allRemoveSummary = "\nALL REMOVED";

    private final static String defaultLanguage = "default"; //ENG
    private final static String PLLanguage = "PL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openSummary(View v) {
        Intent intentSum = new Intent(this, Summary.class);
        startActivity(intentSum);
    }

    private static String language = defaultLanguage;

    public static String getLanguage() {
        return language;
    }


    public void clickPL(View v) {
        language = PLLanguage;
        updateLocale();
    }

    public void clickENG(View v) {
        language = defaultLanguage;
        updateLocale();
    }

    private Button add;
    private Button remove;
    private Button summary;

    public void updateLocale() {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        add = (Button) findViewById(R.id.buttonDodaj);
        add.setText(R.string.add);
        remove = (Button) findViewById(R.id.buttonUsun);
        remove.setText(R.string.remove);
        summary = (Button) findViewById(R.id.buttonPods);
        summary.setText(R.string.summary);
        read();
    }

    @Override
    public void onResume() {
        super.onResume();
        listTV.clear();
        read();
    }

    private final int txtSize = 24;

    public void read() {
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main);
        cls();

        Cursor k = zb.writeAllData();
        while (k.moveToNext()) {
            int nr = k.getInt(0);
            String imie = k.getString(1);
            int wiek = k.getInt(2);
            String sciezka = k.getString(3);
            String data = k.getString(4);
            TableRow tableRrow1 = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setTextSize(txtSize);
            textView.setText("\n" + imie + ", " + wiek + ", " + data);
            textView.setId(nr);
            textView.setTag(sciezka);
            listTV.add(textView);


            ImageView imageView = new ImageView(this);
            imageView.setId(nr);

            setPic(sciezka, imageView);

            Button bEdit = new Button(this);
            bEdit.setText(R.string.edit);
            bEdit.setId(nr);


            Button bRemove = new Button(this);
            bRemove.setText(R.string.remove);
            bRemove.setId(nr);

            tableRrow1.addView(textView);
            tableRrow1.addView(imageView);
            tableRrow1.addView(bEdit);
            tableRrow1.addView(bRemove);

            layout.addView(tableRrow1);
            layout.addView(new TextView(this));

            bEdit.setOnClickListener(listenerEdit);
            bRemove.setOnClickListener(listenerRemove);

        }

    }

    private final int scale = Math.min(40, 30);

    public void setPic(String path, ImageView mImageView) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);

        // Determine how much to scale down the image
        int scaleFactor = scale;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    View.OnClickListener listenerRemove = new View.OnClickListener() {
        public void onClick(View v) {
            remove(v);
        }
    };

    View.OnClickListener listenerEdit = new View.OnClickListener() {
        public void onClick(View v) {
            openEdit(v);
        }
    };

    public void openEdit(View view) {

        Intent intentEdit = new Intent(this, Add.class);
        Bundle extras = new Bundle();

        String txt = "";
        String id = "";
        String sciezka = "";

        edit = true;

        for (TextView textView : listTV) {
            if (textView.getId() == view.getId()) {
                txt = textView.getText().toString();
                id += textView.getId();

                if (textView.getTag() != null) {
                    sciezka = textView.getTag().toString();
                }

                break;
            }
        }

        String txtSplit[] = txt.split(", ");
        String imie = txtSplit[0].substring(1);
        String wiek = txtSplit[1];
        String data = txtSplit[2];

        extras.putString("imie", imie);
        extras.putString("wiek", wiek);
        extras.putString("id", id);
        extras.putString("sciezka", sciezka);
        extras.putString("data", data);
        MainActivity.setSummaryReport(MainActivity.getSummaryReport() + editSummary + imie + " " + wiek);

        intentEdit.putExtras(extras);
        startActivity(intentEdit);
    }


    public void remove(View view) {
        zb.removeData(view.getId());
        MainActivity.setSummaryReport(MainActivity.getSummaryReport() + removeSummary + view.getId());
        read();
    }


    public void openAdd(View view) {
        Intent intent = new Intent(this, Add.class);
        startActivityForResult(intent, DODAJ_ACTIVITY_REQUEST_CODE);
    }

    public void clsDB(View view) {
        cls();
        zb.removeAllData();
        MainActivity.setSummaryReport(MainActivity.getSummaryReport() + allRemoveSummary);
    }


    public void cls() {

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main);
        Button button1 = (Button) findViewById(R.id.buttonDodaj);
        Button button2 = (Button) findViewById(R.id.buttonUsun);
        layout.removeAllViews();
        layout.addView(button2);
        layout.addView(button1);

    }
}

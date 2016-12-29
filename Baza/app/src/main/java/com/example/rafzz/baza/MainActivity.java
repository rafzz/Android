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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int DODAJ_ACTIVITY_REQUEST_CODE = 1;
    protected static final String EXTRA_MESSAGE = "";
    //protected static final String nr = "";

    protected static  boolean ifedit = false;

    private LinqBaza zb = new LinqBaza(this);

    static String summaryReport="";


    private ArrayList<TextView> listTV = new ArrayList<TextView>(){};
    private ArrayList<Button> listB = new ArrayList<Button>(){};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //zb.addData("sdfs",3,"sfds");


    }

    public void openSummary(View v){
        Intent intentSum = new Intent(this, Summary.class);
        //intentSum.putExtra("sum",summaryReport);
        startActivity(intentSum);
    }

    static String language="default";

    public void clickPL(View v){
        //add = (Button) findViewById(R.id.buttonDodaj);
        language="pl";
        updateLocale();
        //add.setText(R.string.add);

    }
    public void clickENG(View v){
        //add = (Button) findViewById(R.id.buttonDodaj);
        language="default";
        updateLocale();
        //add.setText(R.string.add);

    }
    private Button add;
    private Button remove;
    private Button summary;
    public void updateLocale(){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale=locale;
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
    public void onResume(){
        super.onResume();
        listTV.clear();
        read();
    }

    public void read(){

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main);
        cls();

        Cursor k = zb.writeAllData();
        while(k.moveToNext()){
            int nr=k.getInt(0);
            String imie=k.getString(1);
            int wiek=k.getInt(2);
            String sciezka=k.getString(3);
            String data = k.getString(4);

            TableRow tr1 = new TableRow(this);


            TextView tt = new TextView(this);

            tt.setTextSize(24);
            tt.setText("\n"+imie+", "+wiek+", "+data);
            tt.setId(nr);
            String s = sciezka;
            tt.setTag(s);
            listTV.add(tt);
            //layout.addView(tt);


            ImageView ii = new ImageView(this);
            //layout.addView(ii);
            ii.setId(nr);

            
            setPic(sciezka,ii);
            //listIV.add(ii);


            Button bEdit = new Button(this);
            bEdit.setText(R.string.edit);
            bEdit.setId(nr);

            //layout.addView(bEdit);

            Button bRemove = new Button(this);
            bRemove.setText(R.string.remove);
            bRemove.setId(nr);
            //layout.addView(bRemove);



            tr1.addView(tt);
            tr1.addView(ii);
            tr1.addView(bEdit);
            tr1.addView(bRemove);

            layout.addView(tr1);
            layout.addView(new TextView(this));

            bEdit.setOnClickListener(listenerEdit);
            bRemove.setOnClickListener(listenerRemove);

        }

    }

    public void setPic(String path, ImageView mImageView) {

        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(40,30);

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

    public void openEdit(View view){

        Intent intentEdit = new Intent(this, Add.class);
        Bundle extras = new Bundle();

        String txt="";
        String id="";
        String sciezka="";

        String txtid;
        ifedit=true;
        for(TextView t : listTV){
            if(t.getId()==view.getId()){
                txt=t.getText().toString();
                id+=t.getId();

                if(t.getTag()!=null){
                    sciezka=t.getTag().toString();
                }

                break;
            }
        }

        String txtSplit[] = txt.split(", ");
        String imie = txtSplit[0].substring(1);
        String wiek = txtSplit[1];
        String data = txtSplit[2];

        extras.putString("imie",imie);
        extras.putString("wiek",wiek);
        extras.putString("id",id);
        extras.putString("sciezka",sciezka);
        extras.putString("data",data);
        MainActivity.summaryReport+="\nEDIT: "+imie+" "+wiek;

        //txtid = txt+", "+id+", "+sciezka;


        //intentEdit.putExtra(EXTRA_MESSAGE ,txtid);
        intentEdit.putExtras(extras);
        startActivity(intentEdit);
    }

    public void remove(View v){
        zb.removeData(v.getId());
        MainActivity.summaryReport+="\nREMOVED ID: "+v.getId();
        read();
    }


    public void openAdd(View view){
        Intent intent = new Intent(this, Add.class);
        startActivityForResult(intent, DODAJ_ACTIVITY_REQUEST_CODE);
    }

    public void clsDB(View view){
        cls();
        zb.removeAllData();
        MainActivity.summaryReport+="\nALL REMOVED";
    }


    public void cls(){

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main);
        Button b1 = (Button) findViewById(R.id.buttonDodaj);
        Button b2 = (Button) findViewById(R.id.buttonUsun);
        layout.removeAllViews();
        layout.addView(b2);
        layout.addView(b1);

    }
}

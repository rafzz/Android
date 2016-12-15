package com.example.rafzz.baza;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    private static final int DODAJ_ACTIVITY_REQUEST_CODE = 1;
    protected static final String EXTRA_MESSAGE = "";
    //protected static final String nr = "";
    private final String edit = "Edycja";
    private final String del = "Usun";
    protected static  boolean ifedit = false;

    private LinqBaza zb = new LinqBaza(this);

    private ArrayList<TextView> listTV = new ArrayList<TextView>(){};
    private ArrayList<ImageView> listIV = new ArrayList<ImageView>(){};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //zb.addData("sdfs",3,"sfds");

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

            TableRow tr1 = new TableRow(this);


            TextView tt = new TextView(this);

            tt.setTextSize(24);
            tt.setText("\n"+imie+", "+wiek);
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
            bEdit.setText(edit);
            bEdit.setId(nr);

            //layout.addView(bEdit);

            Button bRemove = new Button(this);
            bRemove.setText(del);
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

        extras.putString("imie",imie);
        extras.putString("wiek",wiek);
        extras.putString("id",id);
        extras.putString("sciezka",sciezka);
        //txtid = txt+", "+id+", "+sciezka;


        //intentEdit.putExtra(EXTRA_MESSAGE ,txtid);
        intentEdit.putExtras(extras);
        startActivity(intentEdit);
    }

    public void remove(View v){
        zb.removeData(v.getId());
        read();
    }


    public void openAdd(View view){
        Intent intent = new Intent(this, Add.class);
        startActivityForResult(intent, DODAJ_ACTIVITY_REQUEST_CODE);
    }

    public void clsDB(View view){
        cls();
        zb.removeAllData();
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

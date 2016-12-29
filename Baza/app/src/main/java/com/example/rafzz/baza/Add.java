package com.example.rafzz.baza;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.rafzz.baza.MainActivity.ifedit;
import static com.example.rafzz.baza.MainActivity.summaryReport;


public class Add extends AppCompatActivity {

    public static final String RESPONSE = "";
    private String globnr;
    private boolean iffoto=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        updateLocale();


    }

    private Button save;
    private Button photo;
    private TextView name;
    private TextView age;
    private TextView day;
    private TextView month;
    private TextView year;
    public void updateLocale(){
        Locale locale = new Locale(MainActivity.language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        save = (Button) findViewById(R.id.buttonZapisz);
        save.setText(R.string.save);
        photo = (Button) findViewById(R.id.foto);
        photo.setText(R.string.photo);
        name = (TextView) findViewById(R.id.LabelImie);
        name.setText(R.string.name);
        age = (TextView) findViewById(R.id.labelWiek);
        age.setText(R.string.age);
        day = (TextView) findViewById(R.id.labelDay);
        day.setText(R.string.day);
        month = (TextView) findViewById(R.id.labelMonth);
        month.setText(R.string.month);
        year = (TextView) findViewById(R.id.labelYear);
        year.setText(R.string.year);
    }

    @Override
    public void onStart(){
        super.onStart();
        //mCurrentPhotoPath="";
    }

    @Override
    public void  onDestroy(){
        super.onDestroy();
        ifedit=false;
        //EditText imie = (EditText) findViewById(R.id.editTextImie);
        //imie.setText("niszcze");
        //mCurrentPhotoPath=null;
    }

    public void save(View view){
        EditText imie = (EditText) findViewById(R.id.editTextImie);
        EditText wiek = (EditText) findViewById(R.id.editTextWiek);
        ImageView zdjecie = (ImageView) findViewById(R.id.imageView);
        EditText dd = (EditText) findViewById(R.id.editTextDay);
        EditText mm = (EditText) findViewById(R.id.editTextMonth);
        EditText rrrr = (EditText) findViewById(R.id.editTextYear);

        //String response = imie.getText()+", "+wiek.getText();

        LinqBaza zb = new LinqBaza(this);

        if(ifedit){
            ifedit=false;

            zb.updateData(Integer.parseInt(globnr), imie.getText().toString(), Integer.parseInt(wiek.getText().toString()),mCurrentPhotoPath,dd.getText().toString()+"/"+mm.getText().toString()+"/"+rrrr.getText().toString());
            //Intent resultIntent = new Intent();
            //resultIntent.putExtra(RESPONSE, response);
            //setResult(RESULT_OK, resultIntent);



        }else{
            zb.addData(imie.getText().toString(),Integer.parseInt(wiek.getText().toString()),mCurrentPhotoPath,dd.getText().toString()+"/"+mm.getText().toString()+"/"+rrrr.getText().toString());
            //Intent resultIntent = new Intent();
            //resultIntent.putExtra(RESPONSE, response);
            //setResult(RESULT_OK, resultIntent);
            MainActivity.summaryReport+="\nADD: "+imie.getText().toString()+" "+wiek.getText().toString();
        }


        this.finish();
        mCurrentPhotoPath=null;

    }


    @Override
    public void onResume(){
        super.onResume();

        if(mCurrentPhotoPath!=null) {
            setPic();
            if(iffoto && ifedit){
                iffoto=false;
                return;
            }
        }

        EditText imie = (EditText) findViewById(R.id.editTextImie);
        EditText wiek = (EditText) findViewById(R.id.editTextWiek);
        ImageView zdjecie = (ImageView) findViewById(R.id.imageView);
        EditText dd = (EditText) findViewById(R.id.editTextDay);
        EditText mm = (EditText) findViewById(R.id.editTextMonth);
        EditText rrrr = (EditText) findViewById(R.id.editTextYear);

        Intent intentEdit = getIntent();

        if(ifedit){
            //String message = intentEdit.getStringExtra(MainActivity.EXTRA_MESSAGE);
            Bundle extras =  intentEdit.getExtras();

            //String messagesplit[] = message.split(", ");

            //imie.setText(messagesplit[0]);
            imie.setText((String) extras.get("imie"));
            //wiek.setText(messagesplit[1]);
            wiek.setText((String) extras.get("wiek"));
            String data = (String) extras.get("data");
            String dataSplit[] = data.split("/");
            dd.setText(dataSplit[0]);
            mm.setText(dataSplit[1]);
            rrrr.setText(dataSplit[2]);

            try{
                //mCurrentPhotoPath=messagesplit[3];
                mCurrentPhotoPath=(String) extras.get("sciezka");
                setPic();
            }catch(ArrayIndexOutOfBoundsException a){

            }


            //globnr = messagesplit[2];
            globnr=(String) extras.get("id");
        }
    }

    private static String mCurrentPhotoPath;

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    public void dispatchTakePictureIntent(View view) {
        iffoto=true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //ImageView iv = (ImageView) findViewById(R.id.imageView2);
                //Bitmap imageBitmap = (Bitmap) photoFile;
                //iv.setImageBitmap();
                //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                //iv.setImageBitmap(bitmap);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


            }
        }


    }


    public void setPic() {
        ImageView mImageView = (ImageView) findViewById(R.id.imageView);
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

}

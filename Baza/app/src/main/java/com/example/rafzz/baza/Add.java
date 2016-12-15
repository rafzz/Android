package com.example.rafzz.baza;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.rafzz.baza.MainActivity.ifedit;



public class Add extends AppCompatActivity {

    public static final String RESPONSE = "";
    private String globnr;
    private boolean iffoto=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

    }

    @Override
    public void  onDestroy(){
        super.onDestroy();
        ifedit=false;
    }

    public void save(View view){
        EditText imie = (EditText) findViewById(R.id.editTextImie);
        EditText wiek = (EditText) findViewById(R.id.editTextWiek);
        ImageView zdjecie = (ImageView) findViewById(R.id.imageView);

        String response = imie.getText()+", "+wiek.getText();

        LinqBaza zb = new LinqBaza(this);

        if(ifedit){
            ifedit=false;

            zb.updateData(Integer.parseInt(globnr), imie.getText().toString(), Integer.parseInt(wiek.getText().toString()),mCurrentPhotoPath);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(RESPONSE, response);
            setResult(RESULT_OK, resultIntent);


        }else{
            zb.addData(imie.getText().toString(),Integer.parseInt(wiek.getText().toString()),mCurrentPhotoPath);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(RESPONSE, response);
            setResult(RESULT_OK, resultIntent);
        }


        this.finish();

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

        Intent intentEdit = getIntent();

        if(ifedit){
            //String message = intentEdit.getStringExtra(MainActivity.EXTRA_MESSAGE);
            Bundle extras =  intentEdit.getExtras();

            //String messagesplit[] = message.split(", ");

            //imie.setText(messagesplit[0]);
            imie.setText((String) extras.get("imie"));
            //wiek.setText(messagesplit[1]);
            wiek.setText((String) extras.get("wiek"));
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

    private String mCurrentPhotoPath;

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

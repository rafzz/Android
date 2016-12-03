package basiclocationsample.sample.location.gms.android.google.com.flashlight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private Camera c;
    private Camera.Parameters parameters;

    public void light(View v){
        if(!v.isSelected()) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            c.setParameters(parameters);
            c.startPreview();
            v.setSelected(true);
        }else if(v.isSelected()){
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            c.setParameters(parameters);
            c.stopPreview();
            v.setSelected(false);
        }
    }


    protected void onResume(){
        super.onResume();


        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            c = Camera.open();   //flashlight
            parameters = c.getParameters();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        c.release();//flashlight
    }

}

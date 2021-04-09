package de.vogella.android.receiver.phone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int PERMISSION_REQUEST_CODE = 11;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        if(!checkPermission()){
            requestPermission();
        }
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(
                MainActivity.this, android.Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this, Manifest.permission.READ_PHONE_STATE)
        &&
                ActivityCompat.shouldShowRequestPermissionRationale(
                        MainActivity.this, Manifest.permission.READ_CALL_LOG)){
            Toast.makeText(MainActivity.this, "Phone state permission",
                    Toast.LENGTH_LONG).show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Log.e("value", "Permission Granted, Now you can use local drive.");
            else
                Log.e("value", "Permission Denied, You cannot use local drive");
        }
    }
}
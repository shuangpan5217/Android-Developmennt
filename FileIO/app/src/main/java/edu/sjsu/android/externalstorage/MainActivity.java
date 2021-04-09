package edu.sjsu.android.externalstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private EditText fname, fcontent, fnameread;
    private TextView filecon;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fname = (EditText) findViewById(R.id.fname);
        fcontent = (EditText)findViewById(R.id.ftext);
        fnameread = (EditText)findViewById(R.id.fnameread);

        Button write = (Button) findViewById(R.id.btnwrite);
        Button read = (Button) findViewById(R.id.btnread);

        filecon = (TextView)findViewById(R.id.filecon);

        File root = getExternalFilesDir(null);
        if(root == null){
            Log.d("Fail", "Shared storage is not currently available.");
            return;
        }
        path = root.getPath();
        System.out.println("Path:" + path);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = fname.getText().toString();
                String filecontent = fcontent.getText().toString();

                if(checkPermission()){
                    FileOperations fop = new FileOperations(path);
                    if(fop.write(filename, filecontent)){
                        Toast.makeText(getApplicationContext(), filename + " created",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "I/O error",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    requestPermission();
                }
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readfilename = fnameread.getText().toString();
                FileOperations fop = new FileOperations(path);
                String text = fop.read(readfilename);

                if(text == null){
                    Toast.makeText(getApplicationContext(), "File not Found",
                            Toast.LENGTH_SHORT).show();
                }
                filecon.setText(text);
            }
        });
    }

    private boolean  checkPermission() {
        int result = ContextCompat.checkSelfPermission(
                MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this, "Write External Storage permission",
                    Toast.LENGTH_LONG).show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
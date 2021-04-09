package edu.sjsu.android.receiver;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "edu.sjsu.android.receiver";

    public DownloadService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null)
            return;
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);

        if(urlPath == null || fileName == null)
            return;
        File output = new File(Environment.getExternalStorageDirectory(), fileName);
        if(output.exists()){
            if(!output.exists()){
                Log.e("Error delete", "Fail deleting the file");
            }
        }
        InputStream stream = null;
        FileOutputStream fos = null;

        try{
            URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next = -1;
            while((next = reader.read()) != -1){
                fos.write(next);
            }
            // successfully finished
            result = Activity.RESULT_OK;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(stream != null){
                try{
                    stream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(fos != null){
                try{
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        publishResults(output.getAbsolutePath(), result);
    }

    public void  publishResults(String outputPath, int result){
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}

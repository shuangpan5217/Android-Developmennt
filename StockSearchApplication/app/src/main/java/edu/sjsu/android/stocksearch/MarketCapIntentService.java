package edu.sjsu.android.stocksearch;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MarketCapIntentService extends IntentService {
    public static final String prefix = "https://api.tiingo.com/tiingo/fundamentals/";
    public static final String middle = "/daily?startDate=";
    public static final String suffix = "&token=" + MainActivity.TIINGO_API_KEY;

    public MarketCapIntentService() {
        super("Market Cap");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String stockSymbol = intent.getStringExtra("stockSymbol");
        String startDate = intent.getStringExtra("startDate");
        String path = prefix + stockSymbol + middle + startDate + suffix;
//        System.out.println(path);
        HttpsURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;

        // stack overflow
        // https://stackoverflow.com/questions/19050294/
        // what-is-the-most-efficient-way-on-android-to-call-http-web-api-calls-that-return
        try{
            url = new URL(path);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

            String temp;
            StringBuilder response = new StringBuilder();
            while ((temp = bReader.readLine()) != null) {
                response.append(temp);
            }

            JSONTokener jsonTokener = new JSONTokener(response.toString());

            JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
            Intent finish = new Intent("marketCapService");
            if(jsonArray.length() == 0){
                finish.putExtra("marketCap", "-");
            }
            else{
                object = (JSONObject)jsonArray.get(jsonArray.length() - 1);
                if(object.getString("marketCap").equals("null")){
                    finish.putExtra("marketCap", "-");
                }
                else{
                    finish.putExtra("marketCap", object.getString("marketCap"));
                }
            }

            sendBroadcast(finish);
        }
        catch(FileNotFoundException e){
            Intent finish = new Intent("marketCapService");
            finish.putExtra("marketCap", "-");
            sendBroadcast(finish);
        }
        catch(Exception e){
            e.printStackTrace();
        }finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String checkNull(String x){
        return x.equals("null") ? "-" : x;
    }
}

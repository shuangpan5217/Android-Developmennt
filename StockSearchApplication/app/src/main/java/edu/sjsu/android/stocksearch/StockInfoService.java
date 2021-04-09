package edu.sjsu.android.stocksearch;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class StockInfoService extends IntentService {
    public static final String prefix = "https://api.tiingo.com/tiingo/utilities/search?query=";
    public static final String suffix = "&token=" + MainActivity.TIINGO_API_KEY;

//    private AutoCompleteTextView autoCompleteTextView;
    public StockInfoService(){
        super("Stock Info");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String stockSymbol = intent.getStringExtra("stockSymbol");
        String path = prefix + stockSymbol + suffix;

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
            ArrayList<String> symbols = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
            for(int i = 0; i < jsonArray.length(); i++){
                object = (JSONObject) jsonArray.get(i);
//                System.out.println(object.toString());
                symbols.add(object.getString("ticker"));
                names.add(object.getString("name"));
            }
            Intent finishHeader = new Intent("headerService");
            finishHeader.putStringArrayListExtra("symbol", symbols);
            finishHeader.putStringArrayListExtra("name", names);

            sendBroadcast(finishHeader);
        }catch(Exception e){
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

//    public void setAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView){
//        this.autoCompleteTextView = autoCompleteTextView;
//    }
}



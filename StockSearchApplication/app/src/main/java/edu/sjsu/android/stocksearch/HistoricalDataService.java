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

public class HistoricalDataService extends IntentService {

    public static final String prefix = "https://api.tiingo.com/tiingo/daily/";
    public static final String middle = "/prices?startDate=";
    public static final String secondMiddle = "&endDate=";
    public static final String suffix = "&resampleFreq=daily&token=" + MainActivity.TIINGO_API_KEY;

    public HistoricalDataService() {
        super("Current Stock Price Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String stockSymbol = intent.getStringExtra("stockSymbol");
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        String path = prefix + stockSymbol + middle + startDate + secondMiddle + endDate + suffix;

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
            ArrayList<String> dates = new ArrayList<>();
            ArrayList<String> closes = new ArrayList<>();
            ArrayList<String> highs = new ArrayList<>();
            ArrayList<String> lows = new ArrayList<>();
            ArrayList<String> opens = new ArrayList<>();
            ArrayList<String> volumes = new ArrayList<>();

            ArrayList<String> adjCloses = new ArrayList<>();
            ArrayList<String> adjHighs = new ArrayList<>();
            ArrayList<String> adjLows = new ArrayList<>();
            ArrayList<String> adjOpens = new ArrayList<>();
            ArrayList<String> adjVolumes = new ArrayList<>();
            ArrayList<String> divCashs = new ArrayList<>();
            ArrayList<String> splitFactors = new ArrayList<>();

            JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
            for(int i = 0; i < jsonArray.length(); i++){
                object = (JSONObject) jsonArray.get(i);

                dates.add(object.getString("date"));
                closes.add(object.getString("close"));
                highs.add(object.getString("high"));
                lows.add(object.getString("low"));
                opens.add(object.getString("open"));
                volumes.add(object.getString("volume"));
                adjCloses.add(object.getString("adjClose"));
                adjHighs.add(object.getString("adjHigh"));
                adjLows.add(object.getString("adjLow"));
                adjOpens.add(object.getString("adjOpen"));
                adjVolumes.add(object.getString("adjVolume"));
                divCashs.add(object.getString("divCash"));
                splitFactors.add(object.getString("splitFactor"));
            }
            Intent finish = new Intent("historicalDataService");

            finish.putStringArrayListExtra("dates", dates);
            finish.putStringArrayListExtra("closes", closes);
            finish.putStringArrayListExtra("highs", highs);
            finish.putStringArrayListExtra("lows", lows);
            finish.putStringArrayListExtra("opens", opens);
            finish.putStringArrayListExtra("volumes", volumes);

            finish.putStringArrayListExtra("adjCloses", adjCloses);
            finish.putStringArrayListExtra("adjHighs", adjHighs);
            finish.putStringArrayListExtra("adjLows", adjLows);
            finish.putStringArrayListExtra("adjOpens", adjOpens);
            finish.putStringArrayListExtra("adjVolumes", adjVolumes);
            finish.putStringArrayListExtra("divCashs",divCashs);
            finish.putStringArrayListExtra("splitFactors", splitFactors);

            sendBroadcast(finish);
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
}

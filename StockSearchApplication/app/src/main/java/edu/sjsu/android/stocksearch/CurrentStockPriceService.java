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

public class CurrentStockPriceService extends IntentService {
    public static final String prefix = "https://api.tiingo.com/iex/?tickers=";
    public static final String suffix = "&token=" + MainActivity.TIINGO_API_KEY;

    public CurrentStockPriceService() {
        super("Current Stock Price Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String stockSymbol = intent.getStringExtra("stockSymbol");
        String path = prefix + stockSymbol + suffix;
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
            ArrayList<String> timestamps = new ArrayList<>();
            ArrayList<String> bidPrices = new ArrayList<>();
            ArrayList<String> lows = new ArrayList<>();
            ArrayList<String> bidSizes = new ArrayList<>();
            ArrayList<String> prevCloses = new ArrayList<>();
            ArrayList<String> quoteTimestamps = new ArrayList<>();
            ArrayList<String> lasts = new ArrayList<>();
            ArrayList<String> askSizes = new ArrayList<>();
            ArrayList<String> volumes = new ArrayList<>();
            ArrayList<String> lastSizes = new ArrayList<>();
            ArrayList<String> highs = new ArrayList<>();
            ArrayList<String> tngoLasts = new ArrayList<>();
            ArrayList<String> askPrices = new ArrayList<>();
            ArrayList<String> opens = new ArrayList<>();
            ArrayList<String> lastSaleTimestamps = new ArrayList<>();
            ArrayList<String> mids = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
            for(int i = 0; i < jsonArray.length(); i++){
                object = (JSONObject) jsonArray.get(i);

                timestamps.add(object.getString("timestamp"));
                bidPrices.add(checkNull(object.getString("bidPrice")));
                lows.add(object.getString("low"));
                bidSizes.add(checkNull(object.getString("bidSize")));
                prevCloses.add(object.getString("prevClose"));
                quoteTimestamps.add(object.getString("quoteTimestamp"));
                lasts.add(object.getString("last"));
                askSizes.add(checkNull(object.getString("askSize")));
                volumes.add(object.getString("volume"));
                lastSizes.add(checkNull(object.getString("lastSize")));
                highs.add(object.getString("high"));
                tngoLasts.add(object.getString("tngoLast"));
                askPrices.add(checkNull(object.getString("askPrice")));
                opens.add(object.getString("open"));
                lastSaleTimestamps.add(object.getString("lastSaleTimestamp"));
                mids.add(checkNull(object.getString("mid")));
            }
            Intent finish = null;
            if(intent.getIntExtra("status", -1) == 0)
                finish = new Intent("currentStockService");
            else
                finish = new Intent("mainStockService");
            finish.putStringArrayListExtra("timestamps", timestamps);
            finish.putStringArrayListExtra("bidPrices", bidPrices);
            finish.putStringArrayListExtra("lows", lows);
            finish.putStringArrayListExtra("bidSizes", bidSizes);
            finish.putStringArrayListExtra("prevCloses", prevCloses);
            finish.putStringArrayListExtra("quoteTimestamps", quoteTimestamps);
            finish.putStringArrayListExtra("lasts", lasts);
            finish.putStringArrayListExtra("askSizes", askSizes);
            finish.putStringArrayListExtra("volumes", volumes);
            finish.putStringArrayListExtra("lastSizes", lastSizes);
            finish.putStringArrayListExtra("highs", highs);
            finish.putStringArrayListExtra("tngoLasts", tngoLasts);
            finish.putStringArrayListExtra("askPrices", askPrices);
            finish.putStringArrayListExtra("opens", opens);
            finish.putStringArrayListExtra("lastSaleTimestamps", lastSaleTimestamps);
            finish.putStringArrayListExtra("mids", mids);
            finish.putExtra("ticker", stockSymbol);

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

    private String checkNull(String x){
        return x.equals("null") ? "-" : x;
    }
}

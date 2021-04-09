package edu.sjsu.android.stocksearch;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CompanyDescriptionInfoService extends IntentService {
    public static final String prefix = "https://api.tiingo.com/tiingo/daily/";
    public static final String suffix = "?token=" + MainActivity.TIINGO_API_KEY;

    //    private AutoCompleteTextView autoCompleteTextView;
    public CompanyDescriptionInfoService(){
        super("Company Description");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        assert intent != null;
        String stockSymbol = intent.getStringExtra("stockSymbol");
        String path = prefix + stockSymbol + suffix;

        HttpsURLConnection urlConnection = null;
        URL url = null;
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
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            Intent finishCompanyDescription = new Intent("companyDescriptionService");
            if(jsonObject.length() == 1){
                finishCompanyDescription.putExtra("detail", "Not Found");
            }
            else{
                finishCompanyDescription.putExtra("exchangeCode", jsonObject.getString("exchangeCode"));
                finishCompanyDescription.putExtra("description", jsonObject.getString("description"));
                finishCompanyDescription.putExtra("ticker", jsonObject.getString("ticker"));
                finishCompanyDescription.putExtra("startDate", jsonObject.getString("startDate"));
                finishCompanyDescription.putExtra("name", jsonObject.getString("name"));
                finishCompanyDescription.putExtra("endDate", jsonObject.getString("endDate"));
            }
            sendBroadcast(finishCompanyDescription);
//            System.out.println("???");
        }
        catch (FileNotFoundException e){
            Intent finishCompanyDescription = new Intent("companyDescriptionService");
            finishCompanyDescription.putExtra("detail", "Not Found");
            sendBroadcast(finishCompanyDescription);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
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



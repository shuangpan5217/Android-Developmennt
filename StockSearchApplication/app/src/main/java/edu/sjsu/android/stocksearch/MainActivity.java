package edu.sjsu.android.stocksearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    public static final String TIINGO_API_KEY = "54c73145349e3a6188a64f8081f62550fd2f3a72";

    private AutoCompleteTextView autoCompleteTextView;
    private ProgressBar progressBar;
    private TextView fetchingTextView;

    private String stockSymbol;
    private boolean fromClickedItem = false;

    private SharedPreferences tickerPref;
    private SharedPreferences namePref;
    private SharedPreferences incrementPref;
    private SharedPreferences pricePref;
    private SharedPreferences marketCapPref;

    private List<FavoriteData> list;
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;

    private String tickerJSONText;
    private String nameJSONText;
    private String incrementJSONText;
    private String priceJSONText;
    private String marketCapJSONText;

    private List<String> tickerText;
    private List<String> nameText;
    private List<Double> incrementText;
    private List<Double> priceText;
    private List<String> marketCapText;

    private double prevClosePrice;
    private double last;

    public boolean flag = true;
    private ReentrantLock lock = new ReentrantLock();

    private Thread t1 = null;
    private Thread t2 = null;

    private Gson gson;

    private BroadcastReceiver headerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            removeProgressBar();
            ArrayList<String> symbols = intent.getStringArrayListExtra("symbol");
            ArrayList<String> names = intent.getStringArrayListExtra("name");
            List<StockHeader> stockHeaderList = new ArrayList<>();
            if (symbols != null && names != null) {
                for (int i = 0; i < symbols.size(); i++) {
                    stockHeaderList.add(new StockHeader(names.get(i), symbols.get(i)));
                }
            }
            AutoCompleteAdapter autoCompleteAdapter =
                    new AutoCompleteAdapter(MainActivity.this, R.layout.auto_complete, stockHeaderList);
            autoCompleteTextView.setAdapter(autoCompleteAdapter);
            autoCompleteTextView.showDropDown();

        }
    };

    private BroadcastReceiver companyDescriptionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            removeProgressBar();
//          System.out.println("got here");
            if(intent.getStringExtra("detail") != null){
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Invalid Symbol")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
            else{
                Intent stockDetailIntent = new Intent(MainActivity.this, StockDetail.class);
                stockDetailIntent.putExtra("ticker", intent.getStringExtra("ticker"));
                stockDetailIntent.putExtra("name", intent.getStringExtra("name"));
                stockDetailIntent.putExtra("exchangeCode", intent.getStringExtra("exchangeCode"));
                stockDetailIntent.putExtra("startDate", intent.getStringExtra("startDate"));
                stockDetailIntent.putExtra("endDate", intent.getStringExtra("endDate"));
                startActivity(stockDetailIntent);
            }
        }
    };

    private BroadcastReceiver cspReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        removeAllPref();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPref();
        gson = new Gson();

        setCspReceiver();
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        progressBar = findViewById(R.id.progressBar);
        fetchingTextView = findViewById(R.id.textView);

        Button getQuoteButton = findViewById(R.id.get_quote_button);
        getQuoteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String sSymbol = autoCompleteTextView.getText().toString();
                if(sSymbol.length() == 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Please enter a Stock Name/Symbol")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
                else{
                    setProgressBar();
                    Intent companyDescriptionIntent =
                            new Intent(MainActivity.this, CompanyDescriptionInfoService.class);
                    companyDescriptionIntent.putExtra("stockSymbol", sSymbol);
                    startService(companyDescriptionIntent);
                }
            }
        });

        Button clearButton = (Button) findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                autoCompleteTextView.setText("");
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(fromClickedItem){
                    fromClickedItem = false;
                    return;
                }
                if(charSequence.length() >= 3){
                    setProgressBar();
                    stockSymbol = charSequence.toString();
                    Intent getStockSymbolIntent = new Intent(MainActivity.this, StockInfoService.class);
                    getStockSymbolIntent.putExtra("stockSymbol", stockSymbol);
                    startService(getStockSymbolIntent);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fromClickedItem = true;
                AutoCompleteAdapter.ViewHolder viewHolder = (AutoCompleteAdapter.ViewHolder) view.getTag();
                autoCompleteTextView.setText(viewHolder.rowSymbol.getText().toString());
            }
        });

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(list, MainActivity.this);
        recyclerView.setAdapter(favoriteAdapter);

        ImageButton selfRefresh = findViewById(R.id.imageButton2);
        selfRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJSONText();
//                System.out.println(tickerJSONText + " " + tickerJSONText.length());
                if(tickerJSONText != null && tickerJSONText.length() != 2){
                    getList();
                    setProgressBar();
                    for(int i = 0; i < tickerText.size(); i++){
                        Intent cspIntent = new Intent(MainActivity.this, CurrentStockPriceService.class);
                        cspIntent.putExtra("stockSymbol", tickerText.get(i));
                        cspIntent.putExtra("status", 1);
                        startService(cspIntent);
                    }
                }
            }
        });

        final SwitchCompat mySwitch = (SwitchCompat) findViewById(R.id.switch1);
        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mySwitch.isChecked()){
                    t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean interrupted = false;
                            getJSONText();
                            if(tickerJSONText != null && tickerJSONText.length() != 2){
                                getList();
                                lock.lock();
                                if(!flag)
                                    flag = true;
//                                System.out.println("flag" + flag);
                                while(flag){
                                    lock.unlock();
                                    for(int i = 0; i < tickerText.size(); i++){
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
//                                                Log.d("UI thread", "I am the UI thread");
                                                setProgressBar();
                                            }
                                        });
                                        Intent cspIntent = new Intent(MainActivity.this, CurrentStockPriceService.class);
                                        cspIntent.putExtra("stockSymbol", tickerText.get(i));
                                        cspIntent.putExtra("status", 1);
                                        startService(cspIntent);
                                    }
                                    try {
                                        Thread.sleep(10000);
                                    } catch (InterruptedException e) {
                                        interrupted = true;
                                    }
                                    if(Thread.currentThread().isInterrupted()){
                                        return;
                                    }
                                    if(!interrupted){
                                        lock.lock();
                                    }
                                    else
                                        return;
                                }
                            }
                        }
                    });
                    t1.start();
                }
                else{
                    t2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            lock.lock();
                            flag = false;
                            lock.unlock();
                            t1.interrupt();
                        }
                    });
                    t2.start();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        updateFavorite();
        IntentFilter headerFilter = new IntentFilter("headerService");
        registerReceiver(headerReceiver, headerFilter);

        IntentFilter companyDescriptionFilter = new IntentFilter("companyDescriptionService");
        registerReceiver(companyDescriptionReceiver, companyDescriptionFilter);

        IntentFilter currentStockPriceIntentFilter = new IntentFilter("mainStockService");
        registerReceiver(cspReceiver, currentStockPriceIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(headerReceiver);
        unregisterReceiver(companyDescriptionReceiver);
        unregisterReceiver(cspReceiver);
    }

    public void setProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        fetchingTextView.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void removeProgressBar(){
        progressBar.setVisibility(View.GONE);
        fetchingTextView.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void removeAllPref(){
        getPref();
        tickerPref.edit().clear().apply();
        namePref.edit().clear().apply();
        incrementPref.edit().clear().apply();
        pricePref.edit().clear().apply();
        marketCapPref.edit().clear().apply();
    }

    private void setCspReceiver(){
        cspReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
//                System.out.println("what happen?");
                ArrayList<String> prevCloses = intent.getStringArrayListExtra("prevCloses");
                ArrayList<String> lasts = intent.getStringArrayListExtra("lasts");

                assert prevCloses != null;
                assert lasts != null;
                for (int i = 0; i < lasts.size(); i++) {
                    try{
                        prevClosePrice = Double.parseDouble(prevCloses.get(i));
                        last = Double.parseDouble(lasts.get(i));
                    }
                    catch(Exception e){
                        prevClosePrice = 1;
                        last = 1;
                    }
                }
                removeProgressBar();

                //update SharedPreferences
                updateSharedPreferences(intent);
            }
        };
    }

    private void updateSharedPreferences(Intent intent){
        getPref();
        getJSONText();
        getList();
        int i = 0;
        for(; i < tickerText.size(); i++){
            if(tickerText.get(i).equalsIgnoreCase(intent.getStringExtra("ticker")))
                break;
        }

//        System.out.println("what happen");
        priceText.set(i, last);
        double x = (last - prevClosePrice) / prevClosePrice * 100;
        double increment = ((double) (Math.round(x * 100))) / 100;
        incrementText.set(i, increment);

        incrementPref.edit().putString("increment", gson.toJson(incrementText)).apply();
        pricePref.edit().putString("price", gson.toJson(priceText)).apply();
        marketCapPref.edit().putString("marketCap", gson.toJson(marketCapText)).apply();

        list = favoriteAdapter.getList();
        favoriteAdapter.updateItem(i, last, increment);

//      updateFavorite();
    }

    private void updateFavorite(){
        getPref();
        getJSONText();

        if(tickerJSONText != null){
            getList();

            if(tickerText.size() != nameText.size()
                    || tickerText.size() != incrementText.size()
                    || tickerText.size() != priceText.size()
                    || tickerText.size() != marketCapText.size()){
                removeAllPref();
            }
            else{
                for(int i = 0; i < tickerText.size(); i++){
                    favoriteAdapter.add(new FavoriteData(
                            tickerText.get(i),
                            nameText.get(i),
                            incrementText.get(i),
                            priceText.get(i),
                            marketCapText.get(i)
                    ));
                }
            }
        }
    }

    private void getPref(){
        tickerPref = getSharedPreferences("tickers", MODE_PRIVATE);
        namePref = getSharedPreferences("names", MODE_PRIVATE);
        incrementPref = getSharedPreferences("increments", MODE_PRIVATE);
        pricePref = getSharedPreferences("prices", MODE_PRIVATE);
        marketCapPref = getSharedPreferences("marketCaps", MODE_PRIVATE);
    }

    private void getJSONText(){
        tickerJSONText = tickerPref.getString("ticker", null);
        nameJSONText = namePref.getString("name", null);
        incrementJSONText = incrementPref.getString("increment", null);
        priceJSONText = pricePref.getString("price", null);
        marketCapJSONText = marketCapPref.getString("marketCap", null);
    }

    private void getList(){
        tickerText = new ArrayList<>(Arrays.asList(gson.fromJson(tickerJSONText, String[].class)));
        nameText = new ArrayList<>(Arrays.asList(gson.fromJson(nameJSONText, String[].class)));
        incrementText = new ArrayList<>(Arrays.asList(gson.fromJson(incrementJSONText, Double[].class)));
        priceText = new ArrayList<>(Arrays.asList(gson.fromJson(priceJSONText, Double[].class)));
        marketCapText = new ArrayList<>(Arrays.asList(gson.fromJson(marketCapJSONText, String[].class)));
    }
}


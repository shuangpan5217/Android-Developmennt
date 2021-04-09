package edu.sjsu.android.stocksearch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockDetail extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewHistory;
    private ProgressBar progressBar;
    private TextView fetchingTextView;
    private Button favorite;

    private BroadcastReceiver cspReceiver;
    private BroadcastReceiver hdReceiver;
    private BroadcastReceiver mcReceiver;

    private LocalDate previous;
    private LocalDate current;

    private List<HistoricalData> historyList;
    private HistoricalDataAdapter hdAdapter;

    private SharedPreferences tickerPref;
    private SharedPreferences namePref;
    private SharedPreferences incrementPref;
    private SharedPreferences pricePref;
    private SharedPreferences marketCapPref;

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
    private String marketCap;

    private Gson gson;

//    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_details);

        getPref();

        setCspReceiver();
        setHdReceiver();
        setMcReceiver();

        progressBar = findViewById(R.id.progressBarTwo);
        fetchingTextView = findViewById(R.id.fetchingtwo);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(StockDetail.this, RecyclerView.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerCurrent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManagerTwo =
                new LinearLayoutManager(StockDetail.this, RecyclerView.VERTICAL, false);
        recyclerViewHistory = (RecyclerView) findViewById(R.id.recyclerHistory);
        recyclerViewHistory.setHasFixedSize(true);
        recyclerViewHistory.setLayoutManager(layoutManagerTwo);

        historyList = new ArrayList<>();
        hdAdapter = new HistoricalDataAdapter(historyList);
        recyclerViewHistory.setAdapter(hdAdapter);

        TextView exchangeCodeTextView = findViewById(R.id.exchangeCode);
        TextView tickerTextView = findViewById(R.id.ticker);
        final TextView nameTextView = findViewById(R.id.name);
        TextView startDateTextView = findViewById(R.id.startDate);
        TextView endDateTextView = findViewById(R.id.endDate);

        final Intent companyIntent = getIntent();
        String exchangeCodeStr = "(" + companyIntent.getStringExtra("exchangeCode") + ")";
        exchangeCodeTextView.setText(exchangeCodeStr);
        tickerTextView.setText(companyIntent.getStringExtra("ticker"));
        nameTextView.setText(companyIntent.getStringExtra("name"));
        startDateTextView.setText(companyIntent.getStringExtra("startDate"));
        endDateTextView.setText(companyIntent.getStringExtra("endDate"));

        Intent marketCapIntent = new Intent(this, MarketCapIntentService.class);
        marketCapIntent.putExtra("stockSymbol", companyIntent.getStringExtra("ticker"));
        marketCapIntent.putExtra("startDate", LocalDate.now().minusDays(7).toString());
        startService(marketCapIntent);
        setProgressBar();

        Intent cspIntent = new Intent(this, CurrentStockPriceService.class);

        cspIntent.putExtra("stockSymbol", companyIntent.getStringExtra("ticker"));
        cspIntent.putExtra("status", 0);

        startService(cspIntent);
        setProgressBar();

        final Intent hdIntent = new Intent(this, HistoricalDataService.class);

        hdIntent.putExtra("stockSymbol", companyIntent.getStringExtra("ticker"));
        current = LocalDate.now();

        modifyCurrentDate();
        previous = current.minusDays(10);

        hdIntent.putExtra("endDate", current.toString());
        hdIntent.putExtra("startDate", previous.toString());

        startService(hdIntent);
        setProgressBar();

        Button loadMore = findViewById(R.id.loadMore);
        loadMore.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println(current.toString() + previous.toString());
                current = previous;
                modifyCurrentDate();
                previous = current.minusDays(10);
                hdIntent.putExtra("endDate", current.toString());
                hdIntent.putExtra("startDate", previous.toString());
                startService(hdIntent);
                setProgressBar();
            }
        });

        gson = new Gson();
        favorite = findViewById(R.id.favorite);

        getJSONText();

//        System.out.println(tickerJSONText);
        if(tickerJSONText == null){
            favorite.setBackgroundResource(android.R.drawable.btn_default);
            favorite.setText(new String("FAVORITE"));
        }
        else{
            getList();
            int i = 0;
            for(; i < tickerText.size(); i++){
                if(tickerText.get(i).equalsIgnoreCase(companyIntent.getStringExtra("ticker"))){
                    favorite.setBackgroundResource(R.color.colorRemove);
                    favorite.setText(new String("REMOVE"));
                    break;
                }
            }
            if(i == tickerText.size()){
                favorite.setBackgroundResource(android.R.drawable.btn_default);
                favorite.setText(new String("FAVORITE"));
            }
        }

        favorite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(favorite.getText().toString().equalsIgnoreCase("favorite")){
                    favorite.setBackgroundResource(R.color.colorRemove);
                    favorite.setText(new String("REMOVE"));

                    if(tickerJSONText != null){
                        getJSONText();
                        getList();
                    }
                    else{
                        tickerText = new ArrayList<>();
                        nameText = new ArrayList<>();
                        incrementText = new ArrayList<>();
                        priceText = new ArrayList<>();
                        marketCapText = new ArrayList<>();
                    }
                    tickerText.add(companyIntent.getStringExtra("ticker"));
                    nameText.add(companyIntent.getStringExtra("name"));
                    double x = (last - prevClosePrice) / prevClosePrice * 100;
                    incrementText.add(((double) (Math.round(x * 100))) / 100);
                    priceText.add(last);
                    if(marketCap == null)
                        marketCap = "-";
                    marketCapText.add(marketCap);
                }
                else{
                    favorite.setBackgroundResource(android.R.drawable.btn_default);
                    favorite.setText(new String("FAVORITE"));

                    getJSONText();
                    getList();

                    int i = 0;
                    for(; i < tickerText.size(); i++){
                        if(tickerText.get(i).equalsIgnoreCase(companyIntent.getStringExtra("ticker")))
                            break;
                    }

                    tickerText.remove(i);
                    nameText.remove(i);
                    incrementText.remove(i);
                    priceText.remove(i);
                    marketCapText.remove(i);
                }

                tickerPref.edit().putString("ticker", gson.toJson(tickerText)).apply();
                namePref.edit().putString("name", gson.toJson(nameText)).apply();
                incrementPref.edit().putString("increment", gson.toJson(incrementText)).apply();
                pricePref.edit().putString("price", gson.toJson(priceText)).apply();
                marketCapPref.edit().putString("marketCap", gson.toJson(marketCapText)).apply();
//                System.out.println(tickerText + " " + nameText + " " + incrementText + " " + priceText + " " + marketCapText);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter currentStockPriceIntentFilter = new IntentFilter("currentStockService");
        registerReceiver(cspReceiver, currentStockPriceIntentFilter);

        IntentFilter historicalDataFilter = new IntentFilter("historicalDataService");
        registerReceiver(hdReceiver, historicalDataFilter);

        IntentFilter marketCapFilter = new IntentFilter("marketCapService");
        registerReceiver(mcReceiver, marketCapFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cspReceiver);
        unregisterReceiver(hdReceiver);
        unregisterReceiver(mcReceiver);
    }

    private void setProgressBar(){
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

    private void setCspReceiver(){
        cspReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
//                System.out.println("what happen?");
                ArrayList<String> timestamps = intent.getStringArrayListExtra("timestamps");
                ArrayList<String> bidPrices = intent.getStringArrayListExtra("bidPrices");
                ArrayList<String> lows = intent.getStringArrayListExtra("lows");
                ArrayList<String> bidSizes = intent.getStringArrayListExtra("bidSizes");
                ArrayList<String> prevCloses = intent.getStringArrayListExtra("prevCloses");
                ArrayList<String> quoteTimestamps = intent.getStringArrayListExtra("quoteTimestamps");
                ArrayList<String> lasts = intent.getStringArrayListExtra("lasts");
                ArrayList<String> askSizes = intent.getStringArrayListExtra("askSizes");
                ArrayList<String> volumes = intent.getStringArrayListExtra("volumes");
                ArrayList<String> lastSizes = intent.getStringArrayListExtra("lastSizes");
                ArrayList<String> highs = intent.getStringArrayListExtra("highs");
                ArrayList<String> tngoLasts = intent.getStringArrayListExtra("tngoLasts");
                ArrayList<String> askPrices = intent.getStringArrayListExtra("askPrices");
                ArrayList<String> opens = intent.getStringArrayListExtra("opens");
                ArrayList<String> lastSaleTimestamps = intent.getStringArrayListExtra("lastSaleTimestamps");
                ArrayList<String> mids = intent.getStringArrayListExtra("mids");

                final List<CurrentStockPrice> inputs = new ArrayList<>();
                assert timestamps != null;
                assert bidPrices != null;
                assert lows != null;
                assert bidSizes != null;
                assert prevCloses != null;
                assert quoteTimestamps != null;
                assert lastSizes != null;
                assert opens != null;
                assert askPrices != null;
                assert askSizes != null;
                assert lasts != null;
                assert volumes != null;
                assert highs != null;
                assert lastSaleTimestamps != null;
                assert mids != null;
                assert tngoLasts != null;
                for (int i = 0; i < timestamps.size(); i++) {
                    inputs.add(new CurrentStockPrice(
                            timestamps.get(i),
                            bidPrices.get(i),
                            lows.get(i),
                            bidSizes.get(i),
                            prevCloses.get(i),
                            quoteTimestamps.get(i),
                            lasts.get(i),
                            askSizes.get(i),
                            volumes.get(i),
                            lastSizes.get(i),
                            highs.get(i),
                            tngoLasts.get(i),
                            askPrices.get(i),
                            opens.get(i),
                            lastSaleTimestamps.get(i),
                            mids.get(i)
                    ));
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
                // define an adapter
                CurrentStockPriceAdapter cspAdapter = new CurrentStockPriceAdapter(inputs);
                recyclerView.setAdapter(cspAdapter);
            }
        };
    }

    private void setHdReceiver(){
        hdReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<String> dates = intent.getStringArrayListExtra("dates");
                ArrayList<String> closes = intent.getStringArrayListExtra("closes");
                ArrayList<String> highs = intent.getStringArrayListExtra("highs");
                ArrayList<String> lows = intent.getStringArrayListExtra("lows");
                ArrayList<String> opens = intent.getStringArrayListExtra("opens");
                ArrayList<String> volumes = intent.getStringArrayListExtra("volumes");
                ArrayList<String> adjCloses = intent.getStringArrayListExtra("adjCloses");
                ArrayList<String> adjHighs = intent.getStringArrayListExtra("adjHighs");
                ArrayList<String> adjLows = intent.getStringArrayListExtra("adjLows");
                ArrayList<String> adjOpens = intent.getStringArrayListExtra("adjOpens");
                ArrayList<String> adjVolumes = intent.getStringArrayListExtra("adjVolumes");
                ArrayList<String> divCashs = intent.getStringArrayListExtra("divCashs");
                ArrayList<String> splitFactors = intent.getStringArrayListExtra("splitFactors");

                assert dates != null;
                assert closes != null;
                assert highs != null;
                assert lows != null;
                assert opens != null;
                assert volumes != null;
                assert adjCloses != null;
                assert adjHighs != null;
                assert adjLows != null;
                assert adjOpens != null;
                assert adjVolumes != null;
                assert divCashs != null;
                assert splitFactors != null;

                for (int i = dates.size() - 1; i >= 0; i--) {
                    hdAdapter.add(new HistoricalData(
                            dates.get(i),
                            closes.get(i),
                            highs.get(i),
                            lows.get(i),
                            opens.get(i),
                            volumes.get(i),
                            adjCloses.get(i),
                            adjHighs.get(i),
                            adjLows.get(i),
                            adjOpens.get(i),
                            adjVolumes.get(i),
                            divCashs.get(i),
                            splitFactors.get(i)
                    ));
                }
                removeProgressBar();
            }
        };
    }

    private void setMcReceiver(){
        mcReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                marketCap = intent.getStringExtra("marketCap");
//                System.out.println("enter here");
                removeProgressBar();
            }
        };
    }

    private void modifyCurrentDate(){
        if(current.getDayOfWeek().getValue() == 6){
            current = current.minusDays(1);
        }
        else if(current.getDayOfWeek().getValue() == 7){
            current = current.minusDays(2);
        }
        else{
            do{
                current = current.minusDays(1);
            }while(current.getDayOfWeek().getValue() == 6 || current.getDayOfWeek().getValue() == 7);
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

package edu.sjsu.android.stocksearch;

public class HistoricalData {
    protected String date;
    protected String close;
    protected String high;
    protected String low;
    protected String open;
    protected String volume;
    protected String adjClose;
    protected String adjHigh;
    protected String adjLow;
    protected String adjOpen;
    protected String adjVolume;
    protected String divCash;
    protected String splitFactor;

    public HistoricalData(String date, String close, String high, String low, String open,
                          String volume, String adjClose, String adjHigh, String adjLow,
                          String adjOpen, String adjVolume, String divCash, String splitFactor){
        this.date = date;
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
        this.volume = volume;
        this.adjClose = adjClose;
        this.adjHigh = adjHigh;
        this.adjLow = adjLow;
        this.adjOpen = adjOpen;
        this.adjVolume = adjVolume;
        this.divCash = divCash;
        this.splitFactor = splitFactor;
    }
}

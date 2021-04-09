package edu.sjsu.android.stocksearch;

public class CurrentStockPrice {
    protected String timestamp;
    protected String bidPrice;
    protected String low;
    protected String bidSize;
    protected String prevClose;
    protected String quoteTimestamp;
    protected String last;
    protected String askSize;
    protected String volume;
    protected String lastSize;
    protected String high;
    protected String tngoLast;
    protected String askPrice;
    protected String open;
    protected String lastSaleTimestamp;
    protected String mid;

    public CurrentStockPrice(String timestamp, String bidPrice, String low, String bidSize,
                             String prevClose, String quoteTimestamp, String last, String askSize,
                             String volume, String lastSize, String high, String tngoLast,
                             String askPrice, String open, String lastSaleTimestamp, String mid){
        this.timestamp = timestamp;
        this.bidPrice = bidPrice;
        this.low = low;
        this.bidSize = bidSize;
        this.prevClose = prevClose;
        this.quoteTimestamp = quoteTimestamp;
        this.last = last;
        this.askSize = askSize;
        this.volume = volume;
        this.lastSize = lastSize;
        this.high = high;
        this.tngoLast = tngoLast;
        this.askPrice = askPrice;
        this.open = open;
        this.lastSaleTimestamp = lastSaleTimestamp;
        this.mid = mid;
    }
}

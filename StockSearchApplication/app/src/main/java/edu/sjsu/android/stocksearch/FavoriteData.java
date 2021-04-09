package edu.sjsu.android.stocksearch;

public class FavoriteData {
    protected String ticker;
    protected String name;
    protected double increment;
    protected double price;
    protected String marketCap;
    
    public FavoriteData(String ticker, String name, double increment, double price, String marketCap){
        this.ticker = ticker;
        this.name = name;
        this.increment = increment;
        this.price = price;
        this.marketCap = marketCap;
    }
}

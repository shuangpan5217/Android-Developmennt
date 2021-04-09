package edu.sjsu.android.stocksearch;

public class StockHeader {
    private String name;
    private String symbol;

    public StockHeader(String name, String symbol){
        this.name = name;
        this.symbol = symbol;
    }

    public String getName(){
        return name;
    }

    public String getSymbol(){
        return symbol;
    }
}

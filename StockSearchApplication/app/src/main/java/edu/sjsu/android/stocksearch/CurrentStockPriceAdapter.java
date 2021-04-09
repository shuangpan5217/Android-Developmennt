package edu.sjsu.android.stocksearch;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CurrentStockPriceAdapter extends RecyclerView.Adapter<CurrentStockPriceAdapter.ViewHolder> {
    private List<CurrentStockPrice> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View layout;
        public TextView timestamp;
        public TextView bidPrice;
        public TextView low;
        public TextView bidSize;
        public TextView prevClose;
        public TextView quoteTimestamp;
        public TextView last;
        public TextView askSize;
        public TextView volume;
        public TextView lastSize;
        public TextView high;
        public TextView tngoLast;
        public TextView askPrice;
        public TextView open;
        public TextView lastsale;
        public TextView mid;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            timestamp = (TextView) v.findViewById(R.id.timestamp);
            bidPrice = (TextView) v.findViewById(R.id.bidPrice);
            low = (TextView) v.findViewById(R.id.low);
            bidSize = (TextView) v.findViewById(R.id.bidSize);
            prevClose = (TextView) v.findViewById(R.id.prevClose);
            quoteTimestamp = (TextView) v.findViewById(R.id.quoteTimeStamp);
            last = (TextView) v.findViewById(R.id.last);
            askSize = (TextView) v.findViewById(R.id.askSize);
            volume = (TextView) v.findViewById(R.id.volume);
            lastSize = (TextView) v.findViewById(R.id.lastSize);
            high = (TextView) v.findViewById(R.id.high);
            tngoLast = (TextView) v.findViewById(R.id.tngoLast);
            askPrice = (TextView) v.findViewById(R.id.askPrice);
            open = (TextView) v.findViewById(R.id.open);
            lastsale = (TextView) v.findViewById(R.id.lastsale);
            mid = (TextView) v.findViewById(R.id.mid);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CurrentStockPriceAdapter(List<CurrentStockPrice> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CurrentStockPriceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.current_stock_price, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        CurrentStockPrice currentStockPrice = values.get(position);

        holder.timestamp.setText(currentStockPrice.timestamp);
        holder.bidPrice.setText(currentStockPrice.bidPrice);

        String low = currentStockPrice.low;
        holder.low.setText(low);

        holder.bidSize.setText(currentStockPrice.bidSize);
        String prevClose = currentStockPrice.prevClose;

        holder.prevClose.setText(prevClose);
        holder.bidPrice.setText(currentStockPrice.bidPrice);
        holder.quoteTimestamp.setText(currentStockPrice.quoteTimestamp);

        String last = currentStockPrice.last;
        holder.last.setText(last);

        holder.askSize.setText(currentStockPrice.askSize);

        String volume = currentStockPrice.volume;
        holder.volume.setText(volume);

        holder.lastSize .setText(currentStockPrice.lastSize);

        String high = currentStockPrice.high;
        holder.high.setText(high);
        String tngoLast = currentStockPrice.tngoLast;
        holder.tngoLast.setText(tngoLast);

        holder.askPrice.setText(currentStockPrice.askPrice);

        String open = currentStockPrice.open;
        holder.open.setText(open);

        holder.lastsale.setText(currentStockPrice.lastSaleTimestamp);
        holder.mid.setText(currentStockPrice.mid);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}


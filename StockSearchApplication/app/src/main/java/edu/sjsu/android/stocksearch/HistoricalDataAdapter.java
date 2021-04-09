package edu.sjsu.android.stocksearch;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoricalDataAdapter extends RecyclerView.Adapter<HistoricalDataAdapter.ViewHolder> {
    private List<HistoricalData> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;

        public TextView date;
        public TextView close;
        public TextView high;
        public TextView low;
        public TextView open;
        public TextView volume;

        public TextView adjClose;
        public TextView adjHigh;
        public TextView adjLow;
        public TextView adjOpen;
        public TextView adjVolume;
        public TextView divCash;
        public TextView splitFactor;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            date = (TextView) v.findViewById(R.id.date);
            close = (TextView) v.findViewById(R.id.close);
            high = (TextView) v.findViewById(R.id.high);
            low = (TextView) v.findViewById(R.id.low);
            open = (TextView) v.findViewById(R.id.open);
            volume = (TextView) v.findViewById(R.id.volume);

            adjClose = (TextView) v.findViewById(R.id.adjClose);
            adjHigh = (TextView) v.findViewById(R.id.adjHigh);
            adjLow = (TextView) v.findViewById(R.id.adjLow);
            adjOpen = (TextView) v.findViewById(R.id.adjOpen);
            adjVolume = (TextView) v.findViewById(R.id.adjVolume);

            divCash = (TextView) v.findViewById(R.id.divCash);
            splitFactor = (TextView) v.findViewById(R.id.splitFactor);
        }
    }

    public void add(HistoricalData historicalData){
        values.add(historicalData);
        notifyDataSetChanged();
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoricalDataAdapter(List<HistoricalData> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public HistoricalDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.historical_data, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        HistoricalData historicalData = values.get(position);

        holder.date.setText(historicalData.date);
        holder.close.setText(historicalData.close);
        holder.high.setText(historicalData.high);
        holder.low.setText(historicalData.low);
        holder.open.setText(historicalData.open);
        holder.volume.setText(historicalData.volume);
        holder.adjClose.setText(historicalData.adjClose);
        holder.adjHigh.setText(historicalData.adjHigh);

        holder.adjLow.setText(historicalData.adjLow);
        holder.adjOpen.setText(historicalData.adjOpen);

        holder.adjVolume.setText(historicalData.adjVolume);
        holder.divCash.setText(historicalData.divCash);
        holder.splitFactor.setText(historicalData.splitFactor);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}


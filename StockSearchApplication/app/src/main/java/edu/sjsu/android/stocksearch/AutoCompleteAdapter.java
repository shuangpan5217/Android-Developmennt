package edu.sjsu.android.stocksearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<StockHeader> {
    public AutoCompleteAdapter(@NonNull Context context, int resource, List<StockHeader> stockHeaderList) {
        super(context, resource, stockHeaderList);
    }

    public static class ViewHolder {
        // each data item is just a string in this case
        public TextView rowSymbol;
        public TextView rowName;
    }

    @androidx.annotation.NonNull
    @Override
    public View getView(int position, View convertView, @androidx.annotation.NonNull ViewGroup root) {
        View v = convertView;
        ViewHolder holder; // to reference the child views for later actions

        if (v == null) {
            LayoutInflater vi =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.auto_complete, root, false);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.rowSymbol = (TextView) v.findViewById(R.id.rowSymbol);
            holder.rowName = (TextView) v.findViewById(R.id.rowName);
            // associate the holder with the view for later lookup
            v.setTag(holder);
        }
        else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) v.getTag();
        }
        // no local variables with findViewById here

        // use holder.nameText where you were
        // using the local variable nameText before
        StockHeader stockHeader = getItem(position);
        if(stockHeader != null){
            if(stockHeader.getSymbol() == null)
                holder.rowSymbol.setText("-");
            else
                holder.rowSymbol.setText(stockHeader.getSymbol());
            if(stockHeader.getName() == null)
                holder.rowName.setText("-");
            else
                holder.rowName.setText(stockHeader.getName());
        }
        return v;
    }

//    @Override
//    public int getCount(){
//        return stockHeaderList.size();
//    }
}

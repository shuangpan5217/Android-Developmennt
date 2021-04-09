package edu.sjsu.android.stocksearch;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>
{
    private List<FavoriteData> values;
    private MainActivity context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView ticker;
        public TextView name;
        public TextView increment;
        public TextView price;
        public TextView marketCap;

        public ViewHolder(@NonNull View v) {
            super(v);
            layout = v;
            ticker = v.findViewById(R.id.ticker);
            name = v.findViewById(R.id.name);
            increment = v.findViewById(R.id.increment);
            price = v.findViewById(R.id.price);

            v.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    context.setProgressBar();
                    Intent companyDescriptionIntent =
                            new Intent(context, CompanyDescriptionInfoService.class);
                    companyDescriptionIntent.putExtra("stockSymbol", ticker.getText().toString());
                    context.startService(companyDescriptionIntent);
                }
            });
            marketCap = v.findViewById(R.id.marketCap);
        }
    }

    public void add(FavoriteData favoriteData){
        values.add(favoriteData);
        notifyDataSetChanged();
    }

    public void updateItem(int position, double last, double increment){
        values.get(position).price = last;
        values.get(position).increment = increment;
        notifyDataSetChanged();
    }

    public FavoriteAdapter(List<FavoriteData> values, MainActivity context){
        this.values = values;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.favorite, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new FavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        FavoriteData favoriteData = values.get(position);

        holder.ticker.setText(favoriteData.ticker);
        holder.name.setText(favoriteData.name);
        if(favoriteData.increment >= 0){
            holder.increment.setBackgroundColor(Color.GREEN);
            holder.increment.setText(
                    new String("+" + Double.toString(favoriteData.increment) + "%"));
        }
        else{
            holder.increment.setBackgroundColor(Color.RED);
            holder.increment.setText(
                    new String("-" + Double.toString(favoriteData.increment) + "%"));
        }
        holder.price.setText(new String("$" + Double.toString(((double) Math.round(favoriteData.price * 100)) / 100)));
        if(!favoriteData.marketCap.equals("-")){
            double x = Double.parseDouble(favoriteData.marketCap) / 1000000000;
            String mc = "Market Cap: $" + ((double) Math.round(x * 1000)) / 1000 + "Billion";
            holder.marketCap.setText(mc);
        }
        else{
            holder.marketCap.setText(
                    new String("Market Cap: $" + "-"));
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public List<FavoriteData> getList(){
        return values;
    }
}

package edu.sjsu.android.animallisting;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    private List<Animal> values;
    private MainActivity mainActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView animalName;
        public View layout;
        public ImageView animalView;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            animalName = (TextView) v.findViewById(R.id.firstLine);
            animalView = (ImageView) v.findViewById(R.id.animalImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnimalAdapter(List<Animal> myDataset, MainActivity mainActivity) {
        values = myDataset;
        this.mainActivity = mainActivity;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AnimalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final Animal animal = values.get(position);
        // view clicked listener
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == values.size() - 1){
                    //alert dialog
                    final AlertDialog alertDialog = new AlertDialog.Builder(mainActivity)
                            .setTitle("Warning")
                            .setMessage("The animal is very scary. Do you still want to proceed?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showDetail(animal);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
                else{
                    showDetail(animal);
                }
            }
        });

        try {
            InputStream inputStream = holder.animalView.getContext().getAssets().open(animal.getPath());
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.animalView.setImageDrawable(drawable);
            holder.animalName.setText(animal.getName());
            holder.animalName.setTextColor(Color.BLACK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    //show details by calling Animal Detailsactivity
    private void showDetail(Animal animal){
        Intent detailsIntent = new Intent(mainActivity, AnimalDetails.class);
        detailsIntent.putExtra("name", animal.getName());
        detailsIntent.putExtra("path", animal.getPath());
        detailsIntent.putExtra("details", animal.getDetails());
        mainActivity.startActivity(detailsIntent);
    }
}

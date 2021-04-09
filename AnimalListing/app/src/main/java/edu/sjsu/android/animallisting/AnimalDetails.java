package edu.sjsu.android.animallisting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class AnimalDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);

        Intent detailIntent = getIntent();
        String name = detailIntent.getStringExtra("name");
        String path = detailIntent.getStringExtra("path");
        String details = detailIntent.getStringExtra("details");

        ImageView animalImage = (ImageView) findViewById(R.id.animalView);
        TextView animalName = (TextView) findViewById(R.id.name);
        TextView animalDetails = (TextView) findViewById(R.id.details);

        //set animal name
        animalName.setText(name);
        animalName.setTextColor(Color.BLACK);

        //set details
        animalDetails.setText(details);
        animalDetails.setTextColor(Color.GRAY);

        //set animal image
        try {
            assert path != null;
            InputStream inputStream = animalImage.getContext().getAssets().open(path);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            animalImage.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.information){
            Intent dialIntent = new Intent(this, ZooInfo.class);
            startActivity(dialIntent);
            return true;
        }else if(id == R.id.uninstall){
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
            uninstallIntent.setData(Uri.parse("package:" + "edu.sjsu.android.animallisting"));
            startActivity(uninstallIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

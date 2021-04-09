package edu.sjsu.android.animallisting;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ZooInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoo_information);

        TextView zooName = (TextView) findViewById(R.id.zoo_name);
        zooName.setTextColor(Color.GRAY);
        String name = "Shuang and Wei's Zoo";
        zooName.setText(name);

        final Button dial = (Button) findViewById(R.id.dial);
        final String phoneNumber = "tel:0123456789";
        dial.setTextColor(Color.BLACK);

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                startActivity(dialIntent);
            }
        });
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

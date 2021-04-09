package edu.sjsu.android.activityloaderactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityLoaderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String link = "http://www.amazon.com";
        final String phoneNumber = "tel:+194912344444";

        Button browser = (Button) findViewById(R.id.browser);
        Button dial = (Button) findViewById(R.id.call);

        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                browserIntent.putExtra("link", link);
                Intent browserChooser = Intent.createChooser(browserIntent, "Load " + link + " with:");

                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserChooser);
                }
            }
        });

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                startActivity(dialIntent);
            }
        });
    }
}
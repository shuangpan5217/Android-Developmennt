package edu.sjsu.android.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MyBrowser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browser);

        Intent myBrowserIntent = getIntent();
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(myBrowserIntent.getDataString());
        
    }
}

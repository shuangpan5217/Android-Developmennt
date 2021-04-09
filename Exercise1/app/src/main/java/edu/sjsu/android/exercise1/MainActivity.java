package edu.sjsu.android.exercise1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle myInput = this.getIntent().getExtras();

        TextView t = (TextView) this.findViewById(R.id.textView2);
        t.setBackgroundColor(Color.RED);
        t.setTextSize(30);
        t.setText("Hello " + myInput.getString("uname"));
    }
}
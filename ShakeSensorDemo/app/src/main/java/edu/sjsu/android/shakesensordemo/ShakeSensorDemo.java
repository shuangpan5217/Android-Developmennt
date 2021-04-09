package edu.sjsu.android.shakesensordemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ShakeSensorDemo extends AppCompatActivity implements SensorEventListener {
    private boolean color = false;
    private View view;
    private long lastUpdate;
    TextView textX, textY, textZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        textX = (TextView) findViewById(R.id.x_axis);
        textY = (TextView) findViewById(R.id.y_axis);
        textZ = (TextView) findViewById(R.id.z_axis);

        view = findViewById(R.id.header);
        view.setBackgroundColor(Color.BLUE);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            displayAccelerometer(sensorEvent);
            checkShake(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void displayAccelerometer(SensorEvent sensorEvent){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        textX.setText(new String("X axis" + "\t\t" + x));
        textY.setText(new String("Y axis" + "\t\t" + y));
        textZ.setText(new String("Z axis" + "\t\t" + z));
    }

    private void checkShake(SensorEvent sensorEvent){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        float accelerationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if(accelerationSquareRoot >= 2){
            if(actualTime - lastUpdate < 200){
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(this, "Don't shake me!", Toast.LENGTH_SHORT).show();
            if(color)
                view.setBackgroundColor(Color.BLUE);
            else
                view.setBackgroundColor(Color.RED);
            color = !color;
        }
    }
}
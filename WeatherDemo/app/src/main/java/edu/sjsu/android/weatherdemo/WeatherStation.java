package edu.sjsu.android.weatherdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WeatherStation extends AppCompatActivity {
    private SensorManager sensorManager;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView lightTextView;

    private float currentTemperature = Float.NaN;
    private float currentPressure = Float.NaN;
    private float currentLight = Float.NaN;

    private final SensorEventListener tempSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            currentTemperature = sensorEvent.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    private final SensorEventListener pressureSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            currentPressure = sensorEvent.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    private final SensorEventListener lightSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            currentLight = sensorEvent.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = (TextView) findViewById(R.id.temperature);
        pressureTextView = (TextView) findViewById(R.id.pressure);
        lightTextView = (TextView) findViewById(R.id.light);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Timer updateTimer = new Timer("weatherUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGUI();
            }
        }, 0, 1000);


    }

    @Override
    protected void onResume(){
        super.onResume();

        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(temperatureSensor != null){
            sensorManager.registerListener(tempSensorEventListener,
                    temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            temperatureTextView.setText(new String("Thermometer Unavailable"));
        }

        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressureSensor != null){
            sensorManager.registerListener(pressureSensorEventListener,
                    pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            pressureTextView.setText(new String("Barometer Unavailable"));
        }

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            lightTextView.setText(new String("Light Sensor Unavailable"));
        }
    }

    @Override
    protected void onPause(){
        sensorManager.unregisterListener(tempSensorEventListener);
        sensorManager.unregisterListener(pressureSensorEventListener);
        sensorManager.unregisterListener(lightSensorEventListener);
        super.onPause();
    }

    private void updateGUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!Float.isNaN(currentTemperature)){
                    temperatureTextView.setText(new String(currentTemperature + "C"));
                }
                if(!Float.isNaN(currentPressure)){
                    pressureTextView.setText(new String(currentPressure + "(mBars)"));
                }
                if(!Float.isNaN(currentLight)){
                    String light = "Sunny";
                    if(currentLight <= SensorManager.LIGHT_CLOUDY)
                        light = "Night";
                    else if(currentLight <= SensorManager.LIGHT_OVERCAST)
                        light = "Cloudy";
                    else if(currentLight <= SensorManager.LIGHT_SUNLIGHT)
                        light = "Overcast";
                    lightTextView.setText(light);
                    lightTextView.invalidate();
                }
            }
        });
    }
}
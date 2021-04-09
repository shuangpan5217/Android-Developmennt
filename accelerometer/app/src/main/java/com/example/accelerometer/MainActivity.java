package com.example.accelerometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


public class MainActivity extends Activity {
    private static final String main_act = "com.example.accelerometer:MainActivity";
    private WakeLock mWakeLock;

    SimulationView simulationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, main_act);
        simulationView = new SimulationView(this);
        setContentView(simulationView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mWakeLock.acquire(10*60*1000L /*10 minutes*/);
        simulationView.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        simulationView.stopSimulation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the menu and adding items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

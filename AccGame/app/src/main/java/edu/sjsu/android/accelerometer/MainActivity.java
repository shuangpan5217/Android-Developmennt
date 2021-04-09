package edu.sjsu.android.accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "edu.sjsu.android.accelerometer:MainActivity";
    private PowerManager.WakeLock mWakeLock;
    //The view
    private SimulationView mSimulationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager mPowerManage = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManage.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, TAG);
        mSimulationView = new SimulationView(this);

        setContentView(mSimulationView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // acquire wakelock
        mWakeLock.acquire(10*60*1000L /*10 minutes*/);
        // start simulation to register the listener
        mSimulationView.startSimulation();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Release wakelock
        mWakeLock.release();
        // stop simulation to unregister the listener
        mSimulationView.stopSimulation();
    }
}
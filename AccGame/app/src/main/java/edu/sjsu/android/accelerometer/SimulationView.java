package edu.sjsu.android.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class SimulationView extends View implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Display display;

    private Bitmap mField;
    private Bitmap mBasket;
    private Bitmap mBitmap;
    private static final int BALL_SIZE = 64;
    private static final int BASKET_SIZE = 80;

    private float mXOrigin;
    private float mYOrigin;
    private float sensorX;
    private float sensorY;
    private long eventTimeStamp;
    private Particle mBall = new Particle();
    private float mHorizontalBound;
    private float mVerticalBound;

    public SimulationView(Context context) {
        super(context);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = mWindowManager.getDefaultDisplay();

        // Initialize images from drawable
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);

        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXOrigin = (float) (w * 0.5);
        mYOrigin = (float) (h * 0.5);

        mHorizontalBound = (float) ((w - BALL_SIZE) * 0.5);
        mVerticalBound = (float) ((h - BALL_SIZE) * 0.5);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        eventTimeStamp = sensorEvent.timestamp;
        int degree = display.getRotation();
        if(degree == Surface.ROTATION_0){
            sensorX = sensorEvent.values[0];
            sensorY = sensorEvent.values[1];
        }
        else if(degree == Surface.ROTATION_90){
            sensorX = -sensorEvent.values[1];
            sensorY = sensorEvent.values[0];
        }
        else if(degree == Surface.ROTATION_180){
            sensorX = -sensorEvent.values[0];
            sensorY = -sensorEvent.values[1];
        }
        else{
            sensorX = sensorEvent.values[1];
            sensorY = -sensorEvent.values[0];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mField, 0, 0, null);
        canvas.drawBitmap(mBasket,
                (float) (mXOrigin - BASKET_SIZE / 2),
                (float) (mYOrigin - BASKET_SIZE / 2), null);

        mBall.updatePosition(sensorX, sensorY, eventTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);

        canvas.drawBitmap(mBitmap,
                (float) (mXOrigin - BALL_SIZE / 2) + mBall.mPosX,
                (float) (mYOrigin - BALL_SIZE / 2) - mBall.mPosY, null);
        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    public void startSimulation(){
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,
                accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSimulation(){
        sensorManager.unregisterListener(this);
    }
}

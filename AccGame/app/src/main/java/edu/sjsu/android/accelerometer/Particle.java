package edu.sjsu.android.accelerometer;

public class Particle {
    private static final float COR = 0.7f;
    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;

    public void updatePosition(float sx, float sy, long timestamp) {
        float dt = (System.nanoTime() - timestamp) / 1000000000.0f;
        mVelX += -sx * dt;
        mVelY += -sy * dt;
        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
    }
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if (Float.compare(mPosX, mHorizontalBound) > 0) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
        }
        else if (Float.compare(mPosX, -mHorizontalBound) < 0) {
            mPosX = -mHorizontalBound;
            mVelX = -mVelX * COR;
        }
        if (Float.compare(mPosY, mVerticalBound) > 0) {
            mPosY = mVerticalBound;
            mVelY = -mVelY * COR;
        }
        else if (Float.compare(mPosY, -mVerticalBound) < 0) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
        }
    }
}

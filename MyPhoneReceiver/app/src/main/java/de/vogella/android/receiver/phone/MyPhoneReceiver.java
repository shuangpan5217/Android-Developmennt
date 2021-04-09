package de.vogella.android.receiver.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if(state != null){
                Log.w("MY_DEBUG_TAG", state);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String phoneNumber = extras .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if(phoneNumber != null){
                        Log.w("Phone Number", phoneNumber);
                    }
                    else{
                        Log.w("Phone Number", "Null phone number");
                    }
                }
            }
        }
    }
}

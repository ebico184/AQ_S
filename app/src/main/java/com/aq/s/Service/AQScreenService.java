package com.aq.s.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.Nullable;

import com.aq.s.IAQScreenCallbackInterface;
import com.aq.s.IAQScreenServiceInterface;
import com.aq.s.Utils.ScreenListener;

public class AQScreenService extends Service  {

    private static String TAG = "AQScreenService";
    private ScreenListener screenListener;
    private Context mContext;
    private int screenStatus;

    private IAQScreenServiceInterface screenServiceInterface;

    public AQScreenService() {
        Log.d(TAG, "AQScreenService: ");
        screenServiceInterface = new IAQScreenServiceInterface.Stub() {
            private IAQScreenCallbackInterface callbackInterface;
            @Override
            public void onSreenStatusChanged(int screenStatus) throws RemoteException {
                Log.d(TAG, "onSreenStatusChanged: ");
                callbackInterface.onSreenStatusChanged(screenStatus);
            }

            @Override
            public void registScreenCallback(IAQScreenCallbackInterface callback) throws RemoteException {
                Log.d(TAG, "registScreenCallback: ");
                this.callbackInterface = callback;
            }
        };
        mContext = getBaseContext();
        screenListener = new ScreenListener(mContext);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) screenServiceInterface;
    }




}


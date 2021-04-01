package com.aq.s.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import com.aq.s.Constant.AQConstant;
import com.aq.s.IAQScreenCallbackInterface;
import com.aq.s.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private Context mContext = this;
    private Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String screenStatusString = "";
            switch (msg.what){
                case AQConstant.SCREEN_ON:
                    Log.d(TAG, "handleMessage: SCREEN_ON");
                    screenStatusString = "SCREEN_ON";
                    break;

                case AQConstant.SCREEN_OFF:
                    screenStatusString = "SCREEN_OFF";
                    Log.d(TAG, "handleMessage: SCREEN_OFF");
                    break;

                case AQConstant.SCREEN_UNLOCK:
                    screenStatusString = "SCREEN_UNLOCK";
                    Log.d(TAG, "handleMessage: SCREEN_UNLOCK");
                    break;

                default:
                    break;
            }
            Toast.makeText(mContext, screenStatusString, Toast.LENGTH_LONG).show();
        }
    };

    private IAQScreenCallbackInterface screenCallbackInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenCallbackInterface = new IAQScreenCallbackInterface.Stub() {
            @Override
            public void onSreenStatusChanged(int scrrenStatus) throws RemoteException {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                Message m = new Message();
                m.what = scrrenStatus;
                m.obj = simpleDateFormat.format(date);
                mHandler.sendMessage(m);
            }

        };
        startAQScreenService();
    }

    private void startAQScreenService(){
        Log.d(TAG, "startAQScreenService: ");
        Intent intentService = new Intent();
        intentService.setClassName(this,"com.aq.isxposedinstall.Service.AQScreenService");
        ServiceConnection connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: ");
                screenCallbackInterface = IAQScreenCallbackInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ");
                screenCallbackInterface = null;
            }
        };

        bindService(intentService, connection, Context.BIND_AUTO_CREATE);
    }


}
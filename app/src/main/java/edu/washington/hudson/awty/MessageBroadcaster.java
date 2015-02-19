package edu.washington.hudson.awty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class MessageBroadcaster extends BroadcastReceiver {
    public MessageBroadcaster() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // send Toast
        Log.i("MessageBroadcaster","Message received. Beginning broadcast of " + intent.getStringExtra("Message"));
        Toast.makeText(context, "(" + intent.getStringExtra("Phone").substring(0, 3) + ") " + intent.getStringExtra("Phone").substring(3, 6) + "-" + intent.getStringExtra("Phone").substring(6, 10) + ": " + intent.getStringExtra("Message"), Toast.LENGTH_SHORT).show(); // "(425) 555-1212: Are we there yet?"

        // To enable SMS uncomment these two lines:
        // SmsManager smsManager = SmsManager.getDefault();
        // smsManager.sendTextMessage(intent.getStringExtra("Phone"), null, intent.getStringExtra("Message"), null, null);

    }
}

/*

package edu.washington.hudson.awty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MessageBroadcaster extends Service {
    public MessageBroadcaster() {
    }

    public void onCreate() {
        super.onCreate();
        Log.i("MessageBroadcaster","Service started");//intent.getIntExtra("interval",-1));
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i("MessageBroadcaster","Stopping service");
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        Log.i("MessageBroadcaster","Service started with interval of " + intent.getIntExtra("Interval",0) + " minutes.");
        return Service.START_STICKY;
    }
}

 */

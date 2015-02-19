package edu.washington.hudson.awty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Awty extends ActionBarActivity {

    private boolean started;
    private String buttonText;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awty);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_awty, menu);
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

    public boolean phoneCheck(String phoneInput) {
        Pattern legit = Pattern.compile("^[+]?[0-9]{10,13}$");
        Matcher phoneCheck = legit.matcher(phoneInput);
        return phoneCheck.matches();
    }

    public void onClick(View v) {

        // message
        EditText messageInput = (EditText) findViewById(R.id.message_input);
        String message = messageInput.getText().toString();
        Log.i("Atwy","Message is " + message);

        // phone number
        EditText phoneInput = (EditText)findViewById(R.id.phone_input);
        String phone = phoneInput.getText().toString();

        // interval
        EditText intervalInput = (EditText)findViewById(R.id.interval_input);
        int interval = 0;
        if (!intervalInput.getText().toString().equals("")) {
            interval = Integer.parseInt(intervalInput.getText().toString());
        }

        // check if legit
        Button temp =  (Button) findViewById(R.id.start_stop);
        if(!phoneCheck(phone) || message.equals("") || interval <= 0) {
            Log.i("Awty","Message, phone number, or interval is invalid!");
        } else if (!started) {
            AlarmManager alarmManager = (AlarmManager) Awty.this.getSystemService(ALARM_SERVICE);
            Intent startTrigger = new Intent(Awty.this,MessageBroadcaster.class);
            startTrigger.putExtra("Message",message);
            startTrigger.putExtra("Phone",phone);
            pendingIntent = PendingIntent.getBroadcast(Awty.this,0,startTrigger,0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60 * 1000 * interval,pendingIntent);

            buttonText = "Stop";
            temp.setText(buttonText);
            Log.i("Awty","Message, phone number, and interval are valid!");
            started = !started;
        } else {
            AlarmManager alarmManager=(AlarmManager) Awty.this.getSystemService(ALARM_SERVICE);
            Intent stopTrigger = new Intent(Awty.this,MessageBroadcaster.class);
            pendingIntent = PendingIntent.getBroadcast(Awty.this, 0, stopTrigger, 0);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
            buttonText = "Start";
            temp.setText(buttonText);
            Log.i("Awty","Stopping service");
            started = !started;

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("Awty","Saving state");
        super.onSaveInstanceState(savedInstanceState);
        EditText temp = (EditText) findViewById(R.id.message_input);
        savedInstanceState.putString("Message", temp.getText().toString());
        temp = (EditText) findViewById(R.id.phone_input);
        savedInstanceState.putString("Phone", temp.getText().toString());
        temp = (EditText) findViewById(R.id.interval_input);
        if (!temp.getText().toString().equals("")) {
            savedInstanceState.putInt("Interval", Integer.parseInt(temp.getText().toString()));
        }
        savedInstanceState.putBoolean("Started",started);
        Button tempButton = (Button) findViewById(R.id.start_stop);
        savedInstanceState.putString("Button", "" + tempButton.getText());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("Awty","Restoring state");
        super.onRestoreInstanceState(savedInstanceState);
        EditText temp = (EditText) findViewById(R.id.message_input);
        temp.setText(savedInstanceState.getString("Message"));
        temp = (EditText) findViewById(R.id.phone_input);
        temp.setText(savedInstanceState.getString("Phone"));
        temp = (EditText) findViewById(R.id.interval_input);
        if (savedInstanceState.getInt("Interval") > 0) {
            temp.setText("" + savedInstanceState.getInt("Interval"));
        }
        started = savedInstanceState.getBoolean("Started");
        Button tempButton = (Button) findViewById(R.id.start_stop);
        tempButton.setText(savedInstanceState.getString("Button", buttonText));
    }
}

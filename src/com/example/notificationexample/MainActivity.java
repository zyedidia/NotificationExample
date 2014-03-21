package com.example.notificationexample;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int numSeconds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button goButton = (Button) findViewById(R.id.goButton);
		goButton.setText("Go!");
	}

	// Get the number of seconds the user wants to wait for from the text box
	public int getSeconds() {
		EditText secondsText = (EditText) findViewById(R.id.secondsText);

		numSeconds = 0;

		// Make sure the user entered an integer
		try {
			numSeconds = Integer.parseInt(secondsText.getText().toString());
		} catch (Exception e) {
			Toast.makeText(this, "Must be an integer!", Toast.LENGTH_SHORT).show();
			secondsText.setText("");

			numSeconds = 5;
		}

		return numSeconds;
	}

	// Called when the user clicks the go button
	public void goButtonClicked(View v) {
		Button goButton = (Button) findViewById(R.id.goButton);

		if (goButton.getText().toString() == "Go!") {
		
			numSeconds = getSeconds();
			
			// Begin the countdown
			countDownSeconds(numSeconds);
		}
	}

	public void countDownSeconds(final int numSeconds) {
		final Button goButton = (Button) findViewById(R.id.goButton);

		// A countDown timer will countdown for the amount of milliseconds specified
		// The method onTick will be called every 1000 milliseconds in this case
		new CountDownTimer(numSeconds * 1000, 1000) {

			public void onTick(long millisUntilFinished) {
				// Enter how many seconds are remaining on the Go button
				goButton.setText("Seconds remaining: " + millisUntilFinished / 1000);
			}

			public void onFinish() {
				goButton.setText("Go!");
				// Create the notification
				createNotification("You have been notified after " + numSeconds + " seconds");
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void createNotification(String message) {		
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Notifier")
		.setContentText(message);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.

		notificationManager.notify(1, builder.build());

		CheckBox vibrate = (CheckBox) findViewById(R.id.vibrateBox);		
		
		if (vibrate.isChecked()) {
			// Vibrate the device
			// Vibrating is not necessary
			Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 250 milliseconds
			v.vibrate(250);
		}
	}
}

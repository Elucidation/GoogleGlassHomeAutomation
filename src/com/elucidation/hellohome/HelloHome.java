/**
 * HelloHome.java
 * @author Sameer Ansari
 */
package com.elucidation.hellohome;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.TimelineManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log; 

public class HelloHome extends Service {
	private static final String LOG_TAG = "SAM_DEBUG";

	/*
	 * TimelineManager allows applications to interact with the timeline.
	 * 
	 * Additional information: https://developers.google.com/glass/develop/gdk/reference/com/google/android/glass/timeline/TimelineManager
	 */
	private TimelineManager timeline;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		// Start timeline manager from context
		timeline = TimelineManager.from(this);
	}
	
	@Override
	public void onDestroy() {
		Log.v(LOG_TAG, "Closing down Home!");
		super.onDestroy();
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/*
	 * onStartCommand is used to start a service from the voice trigger in res/xml/voice_trigger_start.xml
	 * (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(LOG_TAG, "We're starting home!!");
		
		long cardId;
		Card card = new Card(this);
		card.setText("Hello Home Again");
		
		synchronized (this) {
			try {
				Log.v(LOG_TAG, "Waiting");
				wait(10000);
				Log.v(LOG_TAG, "Adding Card.");
				// Add card to timeline
				cardId = timeline.insert(card);
				wait(10000);
				Log.v(LOG_TAG, "Removing Card.");
				timeline.delete(cardId);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.v(LOG_TAG, "Finishing.");
		
		this.stopSelf();
		
		return startId;
	}

}

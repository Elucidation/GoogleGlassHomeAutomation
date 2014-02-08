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
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/*
	 * onStartCommand is used to start a service from the voice trigger in res/xml/voice_trigger_start.xml
	 * (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("SAM_DEBUG", "We're starting home!!");
		Card card = new Card(this);
		card.setText("Hello Home");
		
		// Add card to timeline
		timeline.insert(card);
		
		return startId;
	}

}

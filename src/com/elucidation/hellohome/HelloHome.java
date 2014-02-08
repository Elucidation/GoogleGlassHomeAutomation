/**
 * HelloHome.java
 * @author Sameer Ansari
 */
package com.elucidation.hellohome;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class HelloHome extends Service {

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
		return startId;
	}

}

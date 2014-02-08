/**
 * HelloHome.java
 * @author Sameer Ansari
 */
package com.elucidation.hellohome;

import java.util.ArrayList;

import com.google.android.glass.app.Card;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.util.Log; 
import android.widget.TextView;

public class HelloHome extends Activity {

	private static final String LOG_TAG = "SAM_DEBUG";
	
	private TextView mTextView; // Member variable for text view in layout
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(LOG_TAG, "Starting up Home!");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		Log.v(LOG_TAG, "Closing down Home!");
		super.onDestroy();
	}
	
	
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Log.v(LOG_TAG, "We're starting home!!");
		ArrayList<String> voiceResults = getIntent().getExtras()
		        .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
		
		// Hack to '[example, string, message]' to 'example string message'
		String voiceMessage = voiceResults.toString().replace(", ", " ").replaceAll("[\\[\\]]", "");
		
		
		Log.v(LOG_TAG, String.format("Received Voice Command: %s", voiceMessage));
		
		// Create a card showing what you said
		Card card = new Card(this);
		card.setText(voiceMessage);
		card.setFootnote("Said you.");
		setContentView(card.toView());
		
		Log.v(LOG_TAG, "Finishing.");
	}
}

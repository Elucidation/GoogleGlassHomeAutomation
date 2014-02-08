/**
 * HelloHome.java
 * @author Sameer Ansari
 */
package com.elucidation.hellohome;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.glass.app.Card;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.util.Log; 

public class HelloHome extends Activity {
	
	// Tag used in log messages
	private static final String LOG_TAG = "SAM_DEBUG";
	
	// URL for querying info (on my local network for example)
	private static final String URL = "http://192.168.62.177/";

	// Request result
	private String info_result;
	
	private Card info_card;
	
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
		
		// If the message is "get info"
		if (voiceMessage.contains("temperature")) {
			Log.v(LOG_TAG, String.format("Getting info!"));
			
	        
	        // Create a card showing what web service returned
			info_card = new Card(this);
			info_card.setText("");
			info_card.setFootnote("Querying web service...");
			setContentView(info_card.toView());
			
			// Call web service (which updates info_card
			new queryWebService().execute("info");
		}
		else if (voiceMessage.contains("turn") && voiceMessage.contains("light") && voiceMessage.contains("on")) {
			Log.v(LOG_TAG, String.format("Setting LED On!"));
			
	        // Create a card showing what web service returned
			info_card = new Card(this);
			info_card.setText("");
			info_card.setFootnote("Asking web service to turn on LED...");
			setContentView(info_card.toView());
			
			// Call web service (which updates info_card
			new queryWebService().execute("led", "on");
		}
		else if (voiceMessage.contains("turn") && voiceMessage.contains("light") && voiceMessage.contains("off")) {
			Log.v(LOG_TAG, String.format("Setting LED Off!"));
			
	        // Create a card showing what web service returned
			info_card = new Card(this);
			info_card.setText("");
			info_card.setFootnote("Asking web service to turn off LED...");
			setContentView(info_card.toView());
			
			// Call web service (which updates info_card
			new queryWebService().execute("led", "off");
		}
		
		Log.v(LOG_TAG, "Finishing.");
	}
	
	private class queryWebService extends AsyncTask<String, Void, String> {
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			// Update card info with what the web service said!
			info_card.setText(info_result);
			info_card.setFootnote("Said the web service.");
			setContentView(info_card.toView());
		}

		@Override
		protected void onPreExecute() {}

		@Override
		protected void onProgressUpdate(Void... values) {}

		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet(URL);
			request.addHeader("applicationName", "Glass Hello Home");
			
			ResponseHandler<String> handler = new BasicResponseHandler();
			
			if (params.length > 0 && params[0].equalsIgnoreCase("info")) {
				request.addHeader("command", "info");
				try {
					info_result = httpclient.execute(request, handler);
				} catch (ClientProtocolException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		        httpclient.getConnectionManager().shutdown();
		        if (info_result == null) {
		        	info_result = "Couldn't connect :(";
		        }
		        Log.i(LOG_TAG, info_result);
			}
			else if (params.length > 0 && params[0].equalsIgnoreCase("led")) {
				if (params.length == 2) {
					if (params[1].equalsIgnoreCase("on"))
						request.addHeader("command", "led on");
					else
						request.addHeader("command", "led off");
					
				} else {
					Log.w(LOG_TAG, "Error with passed params, ignoring.");
				}
				try {
					info_result = httpclient.execute(request, handler);
				} catch (ClientProtocolException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		        httpclient.getConnectionManager().shutdown();
		        if (info_result == null) {
		        	info_result = "Couldn't connect :(";
		        }
		        Log.i(LOG_TAG, info_result);
			}
			
			return "Execution Finished";
		}
		
	}
}

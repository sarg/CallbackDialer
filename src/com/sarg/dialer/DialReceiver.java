package com.sarg.dialer;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;

public class DialReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		
		if (placeCall(context, number)) {
			setResultData(null);
		}
	}
	
	private boolean placeCall(Context context, String number) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		if (! prefs.getBoolean("enabled", false)) return false;
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() == null) return false;

		HttpClient client = new DefaultHttpClient();
		
		String url = prefs.getString("url", null);
		if (url == null) return false;
		
		String from = prefs.getString("self_number", null);
		if (from == null) return false;
		
		HttpPost req = new HttpPost(url + "?from=" + Uri.encode(from) + "&to=" + Uri.encode(number));
		
		try {
			client.execute(req);
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
}

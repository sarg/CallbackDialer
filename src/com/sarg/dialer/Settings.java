package com.sarg.dialer;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private Preference self_number;
	private Preference url;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	
		PreferenceScreen pscreen = getPreferenceScreen();
		self_number = pscreen.findPreference("self_number");
		url = pscreen.findPreference("url");
		
		updateSummaries();

		setResult(RESULT_CANCELED);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	private void updateSummaries() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		self_number.setSummary(getResources().getString(R.string.pref_summary_self_number) + "\n"
				+ prefs.getString("self_number", null));

		url.setSummary(getResources().getString(R.string.prefs_url_summary) + "\n" + prefs.getString("url", null));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.settings_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				int mAppWidgetId = extras.getInt(
						AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);

				// Make sure we pass back the original appWidgetId
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
				setResult(RESULT_OK, resultValue);
			}

			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		updateSummaries();
	}
}
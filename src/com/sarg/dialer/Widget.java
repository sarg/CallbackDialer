package com.sarg.dialer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateWidget(context, appWidgetManager, appWidgetId);
		}
	}
	
    @Override
	public void onDisabled(Context context) {
		Log.d("SARG", "disable");
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("enabled", false).commit();

    	super.onDisabled(context);
	}

    @Override
	public void onEnabled(Context context) {
		Log.d("SARG", "disable");
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("enabled", false).commit();
		
    	super.onEnabled(context);
	}

	static void updateWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        views.setTextViewText(R.id.Button01, prefs.getBoolean("enabled", false) ? "ON" : "OFF");
        
		Intent intent = new Intent(context, Widget.class);
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		intent.setData(Uri.parse("custom:" + appWidgetId));
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		views.setOnClickPendingIntent(R.id.Button01, pendingIntent);

		// Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		
		Log.d("SARG", intent.toString());
		
		if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			
			Editor editor = prefs.edit();
			editor.putBoolean("enabled", ! prefs.getBoolean("enabled", true));
			editor.commit();
			
			Uri data = intent.getData();
			int appWidgetId = Integer.parseInt(data.getSchemeSpecificPart());
			updateWidget(context, AppWidgetManager.getInstance(context), appWidgetId);
		}
	}
}
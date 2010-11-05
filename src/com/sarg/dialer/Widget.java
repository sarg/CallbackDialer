package com.sarg.dialer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		for (int i=0; i<appWidgetIds.length; i++) {
			updateWidget(context, appWidgetManager, appWidgetIds[i]);
		}
	}
	
	@Override
	public void onDisabled(Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("enabled", false);
		
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("enabled", false);
		
		super.onEnabled(context);
	}

	static void updateWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        views.setTextViewText(R.id.widget_button, prefs.getBoolean("enabled", false) ? "ON" : "OFF");
        
		Intent intent = new Intent(context, Widget.class);
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

		// Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			
			Editor editor = prefs.edit(); 
			editor.putBoolean("enabled", ! prefs.getBoolean("enabled", true));
			editor.commit();

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName thisAppWidget = new ComponentName(context.getPackageName(), Widget.class.getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

			onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}
}
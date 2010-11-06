package com.sarg.dialer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
		PreferenceManager.getDefaultSharedPreferences(context).edit()
			.putBoolean("enabled", false).commit();
	}

	@Override
	public void onEnabled(Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
			.putBoolean("enabled", false).commit();
	}

	static void updateWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        views.setImageViewResource(R.id.widget_button, 
        		prefs.getBoolean("enabled", false) 
        			? R.drawable.icon
        			: R.drawable.icon_off
        );
        
		Intent intent = new Intent(context, Widget.class);
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			
			prefs.edit()
				.putBoolean("enabled", ! prefs.getBoolean("enabled", true))
				.commit();

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			ComponentName thisAppWidget = new ComponentName(context.getPackageName(), Widget.class.getName());
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

			onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}
}
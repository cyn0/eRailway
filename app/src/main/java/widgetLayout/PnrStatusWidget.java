package widgetLayout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import in.co.erailway.erailway.Constants;
import in.co.erailway.erailway.MainActivity;
import in.co.erailway.erailway.R;

/**
 * Implementation of App Widget functionality.
 */
public class PnrStatusWidget extends AppWidgetProvider {

	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
								int appWidgetId) {

		CharSequence widgetText = "PNR Status";
		// Construct the RemoteViews object
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pnr_status_widget);
		views.setTextViewText(R.id.appwidget_text, widgetText);
		Intent configIntent = new Intent(context, MainActivity.class);
		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
		configIntent.putExtra(Constants.APP_URL, Constants.URI_PNR_STATUS);
		views.setOnClickPendingIntent(R.id.pnr_widget_layout, configPendingIntent);
		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		for (int appWidgetId : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onEnabled(Context context) {
		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}
}


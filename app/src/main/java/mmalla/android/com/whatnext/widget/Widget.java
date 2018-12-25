package mmalla.android.com.whatnext.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import mmalla.android.com.whatnext.R;
import mmalla.android.com.whatnext.SplashActivity;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetConfigureActivity WidgetConfigureActivity}
 */
public class Widget extends AppWidgetProvider {

    private static final String TAG = Widget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Timber.d(TAG, "Starting updateAppWidget() in Widget.......");

        CharSequence widgetText = WidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Set up the collection depending on which
        // version is running on the device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }

        Intent appIntent = new Intent(context, SplashActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Timber.d(TAG, "Completed updateAppWidget() in Widget.......");
    }

    /**
     * @param context
     * @param views
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, RemoteViews views) {
        Timber.d(TAG, "Starting setRemoteAdapterV11() in Widget.......");
        views.setRemoteAdapter(0, R.id.widget_grid_view,
                new Intent(context, WidgetService.class));
    }

    /**
     * @param context
     * @param views
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, RemoteViews views) {
        Timber.d(TAG, "Starting setRemoteAdapter() in Widget.......");
        views.setRemoteAdapter(R.id.widget_grid_view,
                new Intent(context, WidgetService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Timber.d(TAG, "Starting onUpdate() in Widget.......");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Timber.d(TAG, "Completed updateAppWidget() in Widget.......");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Timber.d(TAG, "Starting onDeleted() in Widget.......");
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
        Timber.d(TAG, "Completed onDeleted() in Widget.......");
    }

    @Override
    public void onEnabled(Context context) {
        Timber.d(TAG, "Starting onEnabled() in Widget.......");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Timber.d(TAG, "Starting onDisabled() in Widget.......");
        // Enter relevant functionality for when the last widget is disabled
    }
}


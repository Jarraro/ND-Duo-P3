package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.ScoresViewsFactory;

/**
 * Created by Jarrar on 19/10/2015.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new ScoresViewsFactory(this.getApplicationContext(), intent));
    }
}
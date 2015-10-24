package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.service.myFetchService;

/**
 * Created by Jarrar on 19/10/2015.
 */
public class ScoresViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<WidgetListItem> listItemList = new ArrayList();
    private Context context = null;
    private int appWidgetId;
    private String[] mDate = new String[1];
    private Cursor cursor;
    public static final String TIME_COL = "time";
    public static final String HOME_COL = "home";
    public static final String AWAY_COL = "away";
    public static final String HOME_GOALS_COL = "home_goals";
    public static final String AWAY_GOALS_COL = "away_goals";

    public ScoresViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        update_scores();
        Date widgetDate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        mDate[0] = mformat.format(widgetDate);
        cursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate()
                , null, null, mDate, null);
        populateListItems();
    }

    private void populateListItems() {
        if (cursor == null) {
            return;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        while (cursor.moveToNext()) {
            WidgetListItem listItem = new WidgetListItem();
            listItem.home = cursor.getString(cursor.getColumnIndex(HOME_COL));
            listItem.away = cursor.getString(cursor.getColumnIndex(AWAY_COL));
            listItem.time = cursor.getString(cursor.getColumnIndex(TIME_COL));
            listItem.score = cursor.getString(cursor.getColumnIndex(HOME_GOALS_COL))
                    + " - " + cursor.getString(cursor.getColumnIndex(AWAY_GOALS_COL));
            listItemList.add(listItem);
        }
        cursor.close();
    }

    private void update_scores() {
        Intent service_start = new Intent(context, myFetchService.class);
        context.startService(service_start);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_list_item);
        WidgetListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.textViewHome, listItem.home);
        remoteView.setTextViewText(R.id.textViewAway, listItem.away);
        remoteView.setTextViewText(R.id.textViewTime, listItem.time);
        // check to see if match didn't start yet
        if (!listItem.score.equals("-1 - -1"))
            remoteView.setTextViewText(R.id.textViewScore, listItem.score);
        else
            remoteView.setTextViewText(R.id.textViewScore, "-");

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
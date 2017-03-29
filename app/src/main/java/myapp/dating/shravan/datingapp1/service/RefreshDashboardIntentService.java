package myapp.dating.shravan.datingapp1.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by admin on 16/08/16.
 */
public class RefreshDashboardIntentService extends IntentService
{
    String TAG = "RefreshDashboardIntentService";

    public RefreshDashboardIntentService() {
        super("RefreshDashboardIntentService");
        Log.e(TAG, "Inside Service Constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
      Log.e(TAG, "Inside onHandleIntent");
    }
}

package myapp.dating.shravan.datingapp1.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import myapp.dating.shravan.datingapp1.network.CheckInternetConnection;

/**
 * Created by admin on 16/08/16.
 */
public class MyFirebaseInstantIdService extends FirebaseInstanceIdService
{

    private static final String TAG = "MyFirebaseInstanceID";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        Log.e(TAG, "Outside Check Internet Connection");
        if(checkInternetConnection.checkingInternetConnection(getApplicationContext())){
            Log.e(TAG, "Inside Check Internet Connection");
            //sendRegistrationToServer(refreshedToken);
        }

    }

}

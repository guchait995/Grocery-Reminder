package dao.manishasah.com.manishaboutique;

import android.app.Service;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sourav Guchait on 4/24/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private String token=null;
    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAG3", "Refreshed token: " + refreshedToken);
        this.token=refreshedToken;

          sendRegistrationToServer(refreshedToken);
        storeToken(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
    }

    private void storeToken(String refreshedToken) {
        SharedPreferenceManager sharedPreferenceManager= SharedPreferenceManager.getInstance(getApplicationContext());
        sharedPreferenceManager.storeToken(refreshedToken);
        Log.i("TAG", "storeToken: ");
        Log.d("TAG", "getToken: "+sharedPreferenceManager.getToken());
    }
    public String getToken(){
     return token;
    }
}

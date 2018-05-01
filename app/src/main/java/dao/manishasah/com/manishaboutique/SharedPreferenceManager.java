package dao.manishasah.com.manishaboutique;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Sourav Guchait on 4/24/2018.
 */

public class SharedPreferenceManager {
    private static SharedPreferenceManager sharedPreferenceManager;
    private static Context context;
    private static final String SHARED_PREFERENCE_NAME="fcmsharedpreferencedemo";
    private static final String KEY_ACCESS_TOKEN="token";
    private String TAG="SharedPref";
    public SharedPreferenceManager(Context context) {
        this.context = context;
        Log.i(TAG, "SharedPreferenceManager: ");
    }
    public static synchronized SharedPreferenceManager getInstance(Context context){
        if(sharedPreferenceManager==null){

            sharedPreferenceManager=new SharedPreferenceManager(context);
            Log.i("TAG", "getInstance: ");
        }
        return sharedPreferenceManager;


    }

    public boolean storeToken(String token){
        Log.i(TAG, "storeToken: "+token);
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;
    }

    public String getToken(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        Log.i(TAG, "getToken: "+sharedPreferences.getString(KEY_ACCESS_TOKEN,null));
        return  sharedPreferences.getString(KEY_ACCESS_TOKEN,null);

    }
}

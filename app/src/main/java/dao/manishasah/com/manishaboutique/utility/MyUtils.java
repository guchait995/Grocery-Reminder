package dao.manishasah.com.manishaboutique.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Sourav Guchait on 4/21/2018.
 */

public class MyUtils {
    public static String getimageName(String name){
        String imagename=null;
        if(name.contains(" ")){

           String[] s= name.split(" ");
            for (String s1:s) {
                imagename=s+"_"+imagename;
            }
        }else{
            imagename=name;
        }
        return imagename.toLowerCase();
    }

    public static boolean checkInternet(Context context){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        if(!connected){
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }else{
         //   Toast.makeText(context,"Please wait ",Toast.LENGTH_LONG).show();
        }
        return connected;
    }
}

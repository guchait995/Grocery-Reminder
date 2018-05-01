package dao.manishasah.com.manishaboutique;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class GetProductData extends Service {
    public GetProductData() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Thread t =new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}

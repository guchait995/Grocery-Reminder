package dao.manishasah.com.manishaboutique;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class LauncherActivity extends Activity {

    private ImageView imageView;
    private int opacity=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
      //  getActionBar().hide();
        imageView=findViewById(R.id.imageViewLauncher);
/*

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //run();
             //imageView.setIma
               run();
            }
        }, 2);*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(LauncherActivity.this,HomeActivity.class);
                LauncherActivity.this.startActivity(mainIntent);
                LauncherActivity.this.finish();
            }
        }, 4000);
        /*while (opacity<255){
            opacity++; // from 0 to 255
            imageView.setBackgroundColor(opacity * 0x1000000);
            try {
                Thread.sleep(0);
                Log.i("TAG", "onCreate:loop ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/



    }
}

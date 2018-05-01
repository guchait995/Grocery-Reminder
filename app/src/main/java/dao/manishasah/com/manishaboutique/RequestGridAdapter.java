package dao.manishasah.com.manishaboutique;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import dao.manishasah.com.manishaboutique.Model.Request;

/**
 * Created by Sourav Guchait on 4/19/2018.
 */

public class RequestGridAdapter extends ArrayAdapter {
    private ArrayList<Request> requests;
    private Context context;
    private TextView textView;
    private String TAG="TAG";

    public RequestGridAdapter(@NonNull Context context, ArrayList<Request> requests) {
        super(context, R.layout.each_grid,requests);
        this.requests=requests;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root=null;
        if (root == null) {
            // if it's not recycled, initialize some attributes

            LayoutInflater vi = LayoutInflater.from(context);
            root = vi.inflate(R.layout.each_grid,null);

        }
        textView = root.findViewById(R.id.textViewGrid);

        textView.setText(requests.get(position).getProduct().getProductName());
        String name = requests.get(position).getProduct().getProductImage();
        Log.i("TAG", "getView: "+name);


        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("TAG", "run: getView1");
                new LoadImageFromFirebase(textView,context,requests.get(position).getProduct()).execute();

            }
        });
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/GrocryImages/");
        dir.mkdir();
        File image=new File(dir+"/"+requests.get(position).getProduct().getProductImage()+".jpg");
        Uri uri=Uri.fromFile(image.getAbsoluteFile());
        Log.i(TAG, "getView6: "+uri.toString());
        if(image.exists()) {
            textView.setBackground(Drawable.createFromPath(image.getAbsolutePath()));
        }else{
            Log.i(TAG, "getView7: ");
            thread.start();
        }



        textView.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);

        return root;
    }
}

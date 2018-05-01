package dao.manishasah.com.manishaboutique;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import dao.manishasah.com.manishaboutique.Dao.ProductDao;
import dao.manishasah.com.manishaboutique.Dao.RequestDao;
import dao.manishasah.com.manishaboutique.Dao.UserTokensDao;
import dao.manishasah.com.manishaboutique.Model.Product;
import dao.manishasah.com.manishaboutique.Model.Request;
import dao.manishasah.com.manishaboutique.Model.User;
import dao.manishasah.com.manishaboutique.Model.UserTokens;
import dao.manishasah.com.manishaboutique.utility.ImageSearch;
import dao.manishasah.com.manishaboutique.utility.MyUtils;

/**
 * Created by Sourav Guchait on 4/15/2018.
 */

public class GridAdapter extends ArrayAdapter {
    private  String UserId;
    private Context context;
    private ArrayList<Product> products;
    private TextView textView;
    private String TAG="TAG";
    private Product product;
    private Request request;
    private RequestDao requestDao;
    public GridAdapter(@NonNull Context context,ArrayList<Product> products,String UserId) {
        super(context, R.layout.each_grid,products);
        this.products=products;
        this.context=context;
        this.UserId=UserId;
    }
    public View getView(final int position,
                        View convertView, ViewGroup parent) {
        View root=null;
        if (root == null) {
            // if it's not recycled, initialize some attributes

            LayoutInflater vi = LayoutInflater.from(getContext());
            root = vi.inflate(R.layout.each_grid,null);

        }
        textView = root.findViewById(R.id.textViewGrid);

        textView.setText(products.get(position).getProductName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                request=new Request();
                request.setProduct(products.get(position));
                request.setDateTime(Calendar.getInstance().getTime().toString().substring(0,10));
                request.setUserId(UserId);
                final User user=new User();
                user.setUserId(UserId);
                requestDao=new RequestDao(user);

                Snackbar.make(view, "Confirm Your Request for "+products.get(position).getProductName(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("CONFIRM", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestDao.addRequest(request);
                                UserTokensDao userTokensDao=new UserTokensDao();
                               userTokensDao.getUserTokens(user,context,request);
                            }
                        }).setActionTextColor(context.getResources().getColor(R.color.action_yellow)).show();


            }
        });

        String name = products.get(position).getProductImage();
        Log.i(TAG, "getView: "+name);


        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: getView1");
                 new LoadImageFromFirebase(textView,context,products.get(position)).execute();

            }
        });


            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/GrocryImages/");
            dir.mkdir();
            File image=new File(dir+"/"+products.get(position).getProductImage()+".jpg");
            Uri uri=Uri.fromFile(image.getAbsoluteFile());
            Log.i(TAG, "getView6: "+uri.toString());
            if(image.exists()) {
               //
               // Drawable drawable=new BitmapDrawable(Bitmap.createScaledBitmap(BitmapDrawable.createFromPath(image.getAbsolutePath()), 120, 120, false));
                textView.setBackground(resize(Drawable.createFromPath(image.getAbsolutePath())));
            }else{
                Log.i(TAG, "getView7: ");
                thread.start();
            }


        textView.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);

       return root;
    }
    private Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 180, 180, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
}

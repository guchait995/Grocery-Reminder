package dao.manishasah.com.manishaboutique;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import dao.manishasah.com.manishaboutique.Model.Product;
import dao.manishasah.com.manishaboutique.utility.MyUtils;

/**
 * Created by Sourav Guchait on 4/21/2018.
 */

public class LoadImageFromFirebase extends AsyncTask<Void,Void,Void> {
    private TextView textView;
    private Context context;
    private Drawable drawable;
    private Product product;
    private String TAG="TAG";
    private String path1;
    private Bitmap bitmap;
    private final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("images");

    LoadImageFromFirebase(TextView textView, Context context, Product product){
        this.textView=textView;
        this.context=context;
        this.product=product;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        long TWO_MEGABYTE = 2048 * 2048;
        Log.i(TAG, "doInBackground: "+product.getProductImage());
        storageReference.child(product.getProductImage()+".jpg").getBytes(TWO_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Log.i(TAG, "onSuccess: "+product.getProductImage());
                //drawable= new BitmapDrawable(context.getResources(),bitmap);
                  //           Log.i(TAG, "onSuccess: doinBack");
                if(bitmap!=null) {
                    // textView.setBackground(drawable);
                    File path = Environment.getExternalStorageDirectory();
                    File dir = new File(path + "/GrocryImages/");
                    dir.mkdir();
                    File file = new File(dir, "/"+product.getProductImage() + ".jpg");
                    path1=file.getAbsolutePath();
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                textView.setBackground(Drawable.createFromPath(path1));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: doinBack"+e.getMessage());
            }
        });



        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}

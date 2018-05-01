package dao.manishasah.com.manishaboutique;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dao.manishasah.com.manishaboutique.Model.Product;

import static android.content.ContentValues.TAG;

/**
 * Created by Sourav Guchait on 4/20/2018.
 */

public class ImageLoadTask extends AsyncTask<Void,Void,Bitmap> {
    private String url;
    private Context context;
    private StorageReference storageReference;
    private StorageReference storageReferenceImages;
    private FirebaseStorage firebaseStorage;
    private Product product;
    private String TAG="TAG2";
    public ImageLoadTask(String url, Product product, Context context) {
        this.url = url;
        this.context=context;
        this.product=product;
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        storageReferenceImages= storageReference.child("images");
        Log.i(TAG, "ImageLoadTask: triggered"+product.getProductName());
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
        //    connection.setDoInput(true);
         //   connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(connection.getInputStream());
            Log.i(TAG, "doInBackground: Bitmap set");
            return myBitmap;
        } catch (Exception e) {
            Log.i(TAG, "doInBackground: "+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
      // Drawable d = new BitmapDrawable(context.getResources(),result);
        //textView.setBackground(d);
        Log.i(TAG, "onPostExecute: ");

        addImagetoFireBaseStorage(product,bitmap);

    }
    public void addImagetoFireBaseStorage(Product product,Bitmap bitmap) {
        StorageReference storageReferenceApple = storageReferenceImages.child(product.getProductName().toLowerCase() + ".jpg");
        //Drawable  drawable=context.getResources().getDrawable(R.drawable.apple);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReferenceApple.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(context, "Upload Failed" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.i(TAG, "onSuccess: Uploaded " + downloadUrl);
                    Toast.makeText(context, "Image Uploaded to FireBase = " + downloadUrl, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


}

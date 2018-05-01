package dao.manishasah.com.manishaboutique;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.sql.Time;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import dao.manishasah.com.manishaboutique.Dao.ProductDao;
import dao.manishasah.com.manishaboutique.Model.Product;
import dao.manishasah.com.manishaboutique.utility.ImageSearch;
import dao.manishasah.com.manishaboutique.utility.MyUtils;
import droidninja.filepicker.FilePickerBuilder;

public class AddProductActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPrice;
    private Spinner spinnerType;
    private Button btnAddProduct;
    private CollectionReference  collectionReference=FirebaseFirestore.getInstance().collection("sampleData");
    private ArrayList<String> filepaths;
    private String UserId;
    private StorageReference storageReference;
    private Product product;
    private StorageReference storageReferenceImages;
    private FirebaseStorage firebaseStorage;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etName=findViewById(R.id.editTextProductName);
        etPrice=findViewById(R.id.editTextProductPrice);
        spinnerType=findViewById(R.id.spinner);
        btnAddProduct=findViewById(R.id.buttonAddProduct);
        progressDialog=new ProgressDialog(this);
         firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
       storageReferenceImages= storageReference.child("images");

        UserId="Sourav";
        filepaths=new ArrayList<>();
            btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name=etName.getText().toString();
                String type=spinnerType.getSelectedItem().toString();
                product=new Product();
                product.setProductId(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                product.setProductName(name);
                product.setProductImage(MyUtils.getimageName(name));
                product.setProductType(type);
                product.setProductPrice("100");

                addProduct(product);
                progressDialog.setMessage("Please Wait ... ");
                progressDialog.show();

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ImageSearch(product,getApplicationContext()).execute();



                    }
                });

                 thread.start();
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));


            }
        });




    }



    private void addProduct(Product product) {

       // ProgressDialog progressDialog=new ProgressDialog(this);
        ProductDao productDao=new ProductDao(UserId);
        productDao.addProduct(product,progressDialog);



    }

}

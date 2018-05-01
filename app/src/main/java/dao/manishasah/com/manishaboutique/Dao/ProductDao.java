package dao.manishasah.com.manishaboutique.Dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Permission;
import java.util.ArrayList;

import dao.manishasah.com.manishaboutique.GridAdapter;
import dao.manishasah.com.manishaboutique.Model.Product;
import dao.manishasah.com.manishaboutique.Model.Request;

/**
 * Created by Sourav Guchait on 4/16/2018.
 */

public class ProductDao {
    private String UserId;
    private CollectionReference collectionReference;
    private ArrayList<Product> products;
    private ArrayList<String> productNames;
    public Product product;
    private ProgressDialog progressDialog;
    private final String TAG="TAG";
    public ProductDao(String UserId) {
        this.collectionReference = FirebaseFirestore.getInstance().collection("sampleData");
        this.productNames=new ArrayList<>();
        this.products=new ArrayList<>();
        this.UserId=UserId;

    }

    public void addProduct(final Product product, final ProgressDialog progressDialog)
    {
        DocumentReference documentReference=collectionReference.document(product.getProductId());
        
        documentReference.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //this.progressDialog=progressDialog;
                    Log.i(TAG, "onComplete: new Item Added");
                }
            }
        });

    }

    public ArrayList<Product> getProducts(){
        return products;
    }

    public ArrayList<Product> getAllproducts( final GridView gridView, final Context context){
         collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult())
                    {
                       Product product=queryDocumentSnapshot.toObject(Product.class);
                        Log.i(TAG, "onComplete: "+product.getProductName());
                        products.add(product);
                        Log.i(TAG, "onComplete: "+products.size());
                    }

                    GridAdapter gridAdapter=new GridAdapter(context,products,UserId);
                    gridView.setAdapter(gridAdapter);
                }
            }

        });
        Log.i(TAG, "getAllproducts: "+products.size());
        return products;
    }

    public ArrayList<String> getProductNames(){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                        Product product1=queryDocumentSnapshot.toObject(Product.class);
                        productNames.add(product1.getProductName());
                    }
                }
            }
        });
        return productNames;

    }

    public Product getProductByName(final String name){
        this.product=new Product();
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                        Product product1=queryDocumentSnapshot.toObject(Product.class);
                        if(product1.getProductName().equalsIgnoreCase(name)){
                            /*product.setProductName(product1.getProductName());
                            product.setProductId(product1.getProductId());
                            product.setProductPrice(product1.getProductPrice());
                            product.setProductType(product1.getProductType());
                            product.setProductImage(product1.getProductImage());
                            Log.i(TAG, "onComplete: "+product.getProductImage());
                            */
                            product=product1 ;
                            break;
                        }
                    }
                }
            }
        });
        //Log.i(TAG, "getProductByName: "+product.getProductImage());
        return product;
    }


}

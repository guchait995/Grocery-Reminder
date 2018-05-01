package dao.manishasah.com.manishaboutique;

import android.*;
import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

import dao.manishasah.com.manishaboutique.Dao.DatabaseHelper;
import dao.manishasah.com.manishaboutique.Dao.ProductDao;
import dao.manishasah.com.manishaboutique.Dao.UserDao;
import dao.manishasah.com.manishaboutique.Model.Product;
import dao.manishasah.com.manishaboutique.Model.User;
import dao.manishasah.com.manishaboutique.utility.ImageSearch;

public class HomeActivity extends AppCompatActivity {
    private String TAG="HomeActivity";
    private GridView listView;
    private ListAdapter listAdapter;
    private CollectionReference cr;
    private ArrayList<String> strings;
    private GridAdapter gridAdapter;
    private SearchView searchView;
    private String m_Text = "";
    private ArrayList<Product> products;
    private ProgressBar progressBar;
    private String UserId;
    private User user;
   // private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setLogo(R.mipmap.ic_launcher_round);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar=new ProgressBar(this);

        cr= FirebaseFirestore.getInstance().collection("sampleData");
        listView = (GridView) findViewById(R.id.ProductList);
       // imageView=findViewById(R.id.imageView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
              */  AddaProduct();
            }
        });
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(HomeActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        products=new ArrayList<>();
        checkInternet(this);
        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        User user= databaseHelper.fetchUserDetails();
        this.user=user;
        Log.i(TAG, "onCreate: "+user.getUserId());
        user.setTheme(2);
        switch(user.getTheme()){
            case 1:{

               getTheme().applyStyle(R.style.AppTheme,true);break;
            }
            case 2:{
                getTheme().applyStyle(R.style.OverlayPrimaryColorGreen,true);break;
            }
            case 3:{
                getTheme().applyStyle(R.style.OverlayPrimaryColorBlack,true);break;
            }default:{
                getTheme().applyStyle(R.style.AppTheme,true);
            }

        }
        if(user.getLoginStatus()==0 && user.getClientMode()!=1){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();

        }else if(user.getClientMode()==1 && user.getLoginStatus()==1){
            Intent intent=new Intent(getApplicationContext(),ClientActivity.class);
            //intent.putExtra("");
            startActivity(new Intent(getApplicationContext(),ClientActivity.class));
            finish();
        }else{
          //  Toast.makeText(this, "Clien mode "+user.getClientMode()+",loginstatus "+user.getLoginStatus(), Toast.LENGTH_SHORT).show();

            UserId=user.getUserId();
        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        ProductDao dao=new ProductDao(UserId);
        products=dao.getAllproducts(listView,getApplicationContext());
      //  Toast.makeText(this, "product list size"+products.size(), Toast.LENGTH_SHORT).show();

    }


    private void AddaProduct() {
        startActivity(new Intent(this,AddProductActivity.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.MyAccount) {
            startActivity(new Intent(getApplicationContext(),MyAccountActivity.class));
            return true;
        }
        if(id==R.id.ClientModePassword){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Setup Client Password ");

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    UserDao userDao=new UserDao();
                    user.setClientPassword(m_Text);
                    new DatabaseHelper(getApplicationContext()).setLocalPassword(user);
                    userDao.setClientPassword(user,HomeActivity.this);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
        if (id == R.id.app_bar_search) {
            Log.i(TAG, "onOptionsItemSelected: ");
           searchView.setQueryHint("Search Item");

            // perform set on query text listener event
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                // do something on text submit
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                // do something when text changes
                    ArrayList<Product> products1=new ArrayList<>();
                    for (Product product:products ) {
                        Log.i(TAG, "onQueryTextChange: "+products.size()+","+newText+","+product.getProductName());
                        if(product.getProductName().toLowerCase().startsWith(newText)||product.getProductName().toUpperCase().startsWith(newText)){
                            Log.i(TAG, "onQueryTextChangeEqual: "+products.size()+","+newText+","+product.getProductName());
                            products1.add(product);
                        }
                    }
                    gridAdapter=new GridAdapter(getApplicationContext(),products1,UserId);
                    listView.setAdapter(gridAdapter);
                   //products1.clear();
                    return false;
                }
            });
            return true;
        }
        if (id == R.id.ClientMode) {
            finish();
           Intent intent= new Intent(getApplicationContext(),ClientActivity.class);
            intent.putExtra("client",1);
            intent.putExtra("user", (Serializable) user);

            startActivity(new Intent(getApplicationContext(),ClientActivity.class));
           // DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
           // databaseHelper.setClient(user);
            return true;
        }
        if(id==R.id.logout){
            //
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to logout ?");
            builder.setIcon(R.drawable.ic_info_black_24dp);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
                    databaseHelper.logoutUser(user);
                    new UserDao().ServerlogoutUser(user);
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();




            //

/*

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            dialog.cancel();
                            break;
                    }
                }
            };
*/
       /*     AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Are you sure you want to logout ?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
*/
        }

        return super.onOptionsItemSelected(item);
    }



    public void checkInternet(Context context){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
           Toast.makeText(context,"Please wait ",Toast.LENGTH_LONG).show();
       }
    }

}

/*
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
            while(true){
                try {
                    ArrayList<String> strings2= fetchData();
                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            }
        });
        thread.start();
    }


 */
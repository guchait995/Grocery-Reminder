package dao.manishasah.com.manishaboutique;

import android.*;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import dao.manishasah.com.manishaboutique.Dao.DatabaseHelper;
import dao.manishasah.com.manishaboutique.Dao.RequestDao;
import dao.manishasah.com.manishaboutique.Dao.UserDao;
import dao.manishasah.com.manishaboutique.Model.Request;
import dao.manishasah.com.manishaboutique.Model.User;

import static droidninja.filepicker.FilePickerConst.REQUEST_CODE;

public class ClientActivity extends AppCompatActivity {

    private GridView gridViewRequest;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Request> requests;
    private User user;
    private String TAG="tag";
    //private PullToRefreshGridView pullToRefreshGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_action_name);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        ActivityCompat.requestPermissions(ClientActivity.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        ActivityCompat.requestPermissions(ClientActivity.this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        user=new User();
        user= (User) getIntent().getSerializableExtra("user");
        if(user==null){
            user=new DatabaseHelper(getApplicationContext()).fetchUserDetails();
        }
       // user.setUserId(user.getUserId());
        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
/*
        new AsyncTask<Void, Void, Void>() {
            private User userfirebase;
            @Override
            protected Void doInBackground(Void... voids) {

                UserDao userDao=new UserDao();
                Log.i(TAG, "doInBackground: "+user.getUserId());
                userDao.getUserfromFireBase(user,getApplicationContext());
                Log.i(TAG, "doInBackground: User set :  "+user.getUserId()+","+user.getLoginStatus()+","+user.getClientPassword()+","+user.getClientMode());
               // Toast.makeText(ClientActivity.this, , Toast.LENGTH_SHORT).show();
                //user=databaseHelper.setClient();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
                user=databaseHelper.fetchUserDetails();
                Log.i(TAG, "onPostExecute: "+user);
            }


        }.execute();
*/


        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        User user=databaseHelper.fetchUserDetails();


        //Log.i(TAG, "onCreate: ClientPASSWORD1"+user.getClientPassword());
        if(user.getClientPassword().equals("clientDefault"))
        {
           // Log.i(TAG, "onCreate: ClientPASSWORD"+this.user.getClientPassword());
        }
        //Toast.makeText(this, "Client Mode"+user.getClientMode(), Toast.LENGTH_SHORT).show();
        int z=getIntent().getIntExtra("clientmode",0);

        if(user.getClientMode()==1){
             Toast.makeText(this, "Restricted Client Mode", Toast.LENGTH_SHORT).show();
         }else{
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         }



        setContentView(R.layout.activity_client);
        gridViewRequest=findViewById(R.id.gridViewRequest);

        swipeRefreshLayout=findViewById(R.id.layout);
        requests=new ArrayList<>();
        RequestDao  requestDao=new RequestDao(user);
        Toast.makeText(this, ""+user.getClientMode(), Toast.LENGTH_SHORT).show();

        requestDao.getAllrequests(getApplicationContext(),gridViewRequest);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ClientActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
                User user= databaseHelper.fetchUserDetails();
                RequestDao  requestDao=new RequestDao(user);
                requestDao.getAllrequests(getApplicationContext(),gridViewRequest);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();

        if(user.getClientMode()!=1)
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if(user.getClientMode()!=1)
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.logoutClient){


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
                    Log.i(TAG, "onClickserverlogout: "+user.getClientPassword());

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



        }
        return super.onOptionsItemSelected(item);
    }
}

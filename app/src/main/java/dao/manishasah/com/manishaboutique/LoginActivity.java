package dao.manishasah.com.manishaboutique;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.manishasah.com.manishaboutique.Dao.DatabaseHelper;
import dao.manishasah.com.manishaboutique.Dao.UserDao;
import dao.manishasah.com.manishaboutique.Dao.UserTokensDao;
import dao.manishasah.com.manishaboutique.Model.User;
import dao.manishasah.com.manishaboutique.Model.UserTokens;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private EditText mEmailView;
    private EditText mPasswordView;
    private Button buttonRegister;
    private Button buttonLogIn;
    private Button buttonClientLogIn;
    private CheckBox checkBoxKeepLogin;
    private ProgressDialog progressDialog;
    //private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 123;
    private String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        buttonClientLogIn=findViewById(R.id.buttonClientLogIn);
        buttonLogIn=findViewById(R.id.buttonLogiIn);
        checkBoxKeepLogin=findViewById(R.id.checkBoxKeepLogin);
        buttonRegister=findViewById(R.id.email_sign_in_button);
        mPasswordView=findViewById(R.id.password);
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        Button btnRegister = (Button) findViewById(R.id.email_sign_in_button);
        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
          }
        });
        buttonLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  UserDao userDao=new UserDao();
                User user=new User();
                user.setUserId(mEmailView.getText().toString());
                user.setPassword(mPasswordView.getText().toString());
                if(checkBoxKeepLogin.isChecked()){
                user.setLoginStatus(1);}
                else {
                 user.setLoginStatus(0);
                }
              */
                //

                new AsyncTask<Void, Void, User>() {
                    int flag1=0;
                    User user2=new User();

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setTitle("Loging In ");
                        progressDialog.setMessage("Please wait ...");
                        progressDialog.show();

                    }

                    @Override
                    protected User doInBackground(Void... voids) {
                        //  User user=new User();
                        Log.i(TAG, "doInBackground: started");
                        FirebaseFirestore.getInstance().collection("User")
                                .document(mEmailView.getText().toString()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User localUser=documentSnapshot.toObject(User.class);
                                        if(localUser.getUserId().equals(mEmailView.getText().toString())){
                                            if(localUser.getPassword().equals(mPasswordView.getText().toString())){
                                                Log.i(TAG, "onSuccess: username : "+localUser.getUserId());

                                                user2=localUser;
                                                Log.i(TAG, "onSuccess: "+user2.getUserId());
                                                flag1=1;
                                                Log.i(TAG, "onSuccess: "+flag1);
                                                user2.setClientMode(0);
                                                if(user2!=null){
                                                    Log.i(TAG, "onPostExecute: "+flag1);
                                                    progressDialog.dismiss();
                                                    finish();
                                                    if(checkBoxKeepLogin.isChecked()){
                                                        user2.setLoginStatus(1);
                                                    }else{
                                                        user2.setLoginStatus(0);
                                                    }

                                                    DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
                                                    Log.i(TAG, "onSuccess: "+user2.getUserId());
                                                    databaseHelper.setAdmin(user2);
                                                    UserTokens userTokens=new UserTokens();
                                                    userTokens.setUser(user2);
                                                    userTokens.setToken(SharedPreferenceManager.getInstance(getApplicationContext()).getToken());
                                                    UserTokensDao userTokensDao=new UserTokensDao();
                                                    userTokensDao.setUserToken(userTokens);
                                                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                                    intent.putExtra("user", (Serializable) user2);
                                                    intent.putExtra("clientmode",0);

                                                    startActivity(intent);

                                                }else{

                                                    Log.i(TAG, "onPostExecute: "+flag1);
                                                    progressDialog.setTitle("Email Id and Password mismatch");
                                                    progressDialog.setCancelable(true);
                                                }
                                            }else{
                                                progressDialog.setTitle("Login Failed");
                                               // progressDialog.setDismissMessage(new Message().se);
                                                progressDialog.setMessage("Email Id and Password mismatch");
                                                progressDialog.setIcon(R.drawable.failed);
                                                progressDialog.setCancelable(true);
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();

                                    }
                                });
                        Log.i(TAG, "doInBackground: ended "+user2.getUserId());
                        return user2;
                    }



                }.execute();




        //     user.setClientPassword(mPasswordView.getText().toString());
          //      userDao.checkLogin(user,getApplicationContext(),view);

            }
        });

        buttonClientLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              //  final User user=new User();

                /*UserDao userDao=new UserDao();
                user.setUserId(mEmailView.getText().toString());
                //user.setClientMode(1);
                user.setClientPassword(mPasswordView.getText().toString());
                if(checkBoxKeepLogin.isChecked()){
                    user.setLoginStatus(1);}
                else {
                    user.setLoginStatus(0);
                }*/

                new AsyncTask<Void, Void, User>() {
                    int flag1=0;
                    User user2=new User();

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setTitle("Loging In as Client ");
                        progressDialog.setMessage("Please wait ... connecting to "+mEmailView.getText().toString());
                        progressDialog.show();

                    }

                    @Override
                    protected User doInBackground(Void... voids) {
                      //  User user=new User();
                        Log.i(TAG, "doInBackground: started");
                        FirebaseFirestore.getInstance().collection("User")
                                .document(mEmailView.getText().toString()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User localUser=documentSnapshot.toObject(User.class);
                                        if(localUser.getUserId().equals(mEmailView.getText().toString())){
                                         if(localUser.getClientPassword().equals(mPasswordView.getText().toString())){
                                             Log.i(TAG, "onSuccess: username : "+localUser.getUserId());

                                             user2=localUser;
                                             Log.i(TAG, "onSuccess: "+user2.getUserId());
                                             flag1=1;
                                             Log.i(TAG, "onSuccess: "+flag1);
                                             user2.setClientMode(1);
                                             if(user2!=null){
                                                 Log.i(TAG, "onPostExecute: "+flag1);
                                                 progressDialog.dismiss();
                                                 finish();
                                                 if(checkBoxKeepLogin.isChecked()){
                                                     user2.setLoginStatus(1);
                                                 }else{
                                                     user2.setLoginStatus(0);
                                                 }

                                                 DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
                                                 databaseHelper.setClient(user2);
                                                 UserTokens userTokens=new UserTokens();
                                                 userTokens.setUser(user2);
                                                 userTokens.setToken(SharedPreferenceManager.getInstance(getApplicationContext()).getToken());
                                                 UserTokensDao userTokensDao=new UserTokensDao();
                                                 userTokensDao.setUserToken(userTokens);
                                                 Intent intent=new Intent(getApplicationContext(),ClientActivity.class);
                                                 intent.putExtra("user", (Serializable) user2);
                                                 intent.putExtra("clientmode",1);

                                                 startActivity(intent);

                                             }else{

                                                 Log.i(TAG, "onPostExecute: "+flag1);
                                                 progressDialog.setTitle("Email Id and Password mismatch");
                                                 progressDialog.setCancelable(true);
                                             }
                                         }else{
                                             progressDialog.setTitle("Login Failed as Client");
                                             progressDialog.setMessage("Email Id and Password mismatch ...\n please confirm your password from "+mEmailView.getText().toString()+" (Admin).");
                                             progressDialog.setIcon(R.drawable.crossred);

                                             progressDialog.setCancelable(true);
                                         }
                                     }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();

                                    }
                                });
                        Log.i(TAG, "doInBackground: ended "+user2.getUserId());
                        return user2;
                    }



                }.execute();
                    }

        });

    }


    private void verifyuser(String email) {
        FirebaseUser  firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser!=null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(LoginActivity.this, "Verification send", Toast.LENGTH_SHORT).show();

                }

            });
        }
    }


    public void registerToUser(String email, String password){
        int LoginStatus=0;
        if(checkBoxKeepLogin.isChecked()){
            Log.i(TAG, "registerToUser: checked");
            LoginStatus=1;
        }
        UserDao userDao=new UserDao();
        User user=new User();
        user.setUserId(email);
        user.setPassword(password);
        user.setLoginStatus(LoginStatus);
        userDao.addNewUser(user,getApplicationContext());
        userDao.addToLocalDataBase(getApplicationContext(),user);
        Log.i(TAG, "registerUser: added");
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("forceLogin",1);
        startActivity(intent);
        finish();


    }

 /*        List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

                        startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
       */
}


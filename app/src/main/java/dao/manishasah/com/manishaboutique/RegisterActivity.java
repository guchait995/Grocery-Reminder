package dao.manishasah.com.manishaboutique;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dao.manishasah.com.manishaboutique.Dao.DatabaseHelper;
import dao.manishasah.com.manishaboutique.Model.User;
import dao.manishasah.com.manishaboutique.utility.MyUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmailRegister;
    private EditText editTextPasswordRegister;
    private EditText editTextConfirmPasswordRegister;
    private EditText editTextClientPasswordRegister;
    private Button buttonRegister;
    private ProgressDialog progressDialog;
    private AsyncTask<Void, Void, Void> mytask;
    private String TAG="TAG";
    private FirebaseAuth firebaseAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_register);
        firebaseAuth=FirebaseAuth.getInstance();
        MyUtils.checkInternet(getApplicationContext());
        user=new User();
        editTextEmailRegister=findViewById(R.id.emailRegister);
        editTextPasswordRegister=findViewById(R.id.passwordRegister);
        editTextClientPasswordRegister=findViewById(R.id.ClientPasswordRegister);
        editTextConfirmPasswordRegister=findViewById(R.id.confirmPasswordRegister);
        buttonRegister=findViewById(R.id.buttonRegisterUser);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Registering User");
        progressDialog.setMessage("Please Wait ... ");
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            if(MyUtils.checkInternet(getApplicationContext())){
                //  User user=new User();
                user.setUserId(editTextEmailRegister.getText().toString());
                user.setClientPassword(editTextClientPasswordRegister.getText().toString());
                user.setPassword(editTextPasswordRegister.getText().toString());
                user.setLoginStatus(1);
                user.setClientMode(0);


                if(user.getPassword().equals(editTextConfirmPasswordRegister.getText().toString()))
                {
                    mytask= new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            // Toast.makeText(RegisterActivity.this, "Sync2 Started", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "doInBackground: sync2 called");



                            DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
                            databaseHelper.addtoLastUser(user);
                            FirebaseFirestore.getInstance().collection("User").document(user.getUserId())
                                    .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i(TAG, "onSuccess: value set");
                                }
                            });


                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);

                            startActivity(intent);
                        }
                    };
                    new AsyncTask<Void,Void,Integer>(){

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressDialog.show();

                        }

                        @Override
                        protected Integer doInBackground(Void... voids) {
                            Log.i(TAG, "doInBackground: Sync Started");

                            //  Toast.makeText(RegisterActivity.this, "Sync Started", Toast.LENGTH_SHORT).show();
                        /*User user=new User();
                        user.setUserId(editTextEmailRegister.getText().toString());
                        user.setClientPassword(editTextClientPasswordRegister.getText().toString());
                        user.setPassword(editTextPasswordRegister.getText().toString());
                        */
                            FirebaseFirestore.getInstance().collection("User").
                                    document(user.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user1=documentSnapshot.toObject(User.class);
                                    if(user1==null){
                                        Log.i(TAG, "onSuccess: Username not found");
                                        mytask.execute();
                                    }else{
                                        Log.i(TAG, "onSuccess: Username Already Registered");
                                        //            Toast.makeText(RegisterActivity.this, "Username Already Registered", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "onFailure: Faliure triggered"+e.getMessage());
                                    mytask.execute();
                                }
                            });
                            return null;
                        }


                    }.execute();


                }else{
                    Snackbar.make(view,"Password Mismatch ... ",Snackbar.LENGTH_INDEFINITE).show();
                }
            }


            }
        });


    }

    public void registerUser(String email,String password){




        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "User Successfully Registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to add data", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onFailure: "+e.getMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}

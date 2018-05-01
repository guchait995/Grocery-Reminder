package dao.manishasah.com.manishaboutique.Dao;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import dao.manishasah.com.manishaboutique.ClientActivity;
import dao.manishasah.com.manishaboutique.HomeActivity;
import dao.manishasah.com.manishaboutique.Model.User;

/**
 * Created by Sourav Guchait on 4/19/2018.
 */

public class UserDao {
private CollectionReference collectionReference;
private String TAG="USERDAO";
private int flag=0;
private User user2;

    public UserDao() {
        this.collectionReference = FirebaseFirestore.getInstance().collection("User");

    }
    public void addToLocalDataBase(Context context,User user){
        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        databaseHelper.addtoLastUser(user);
    }

    public void addNewUser(User user ,Context context){
        if(!isPresent(user)) {
            DocumentReference documentReference = collectionReference.document(user.getUserId());
            documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "onComplete: Success");
                    } else {
                        Log.i(TAG, "onComplete: Failed");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: ");
                }
            });
        }else{
            Toast.makeText(context, "Username Already Registered", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isPresent(User user){
        DocumentReference documentReference=collectionReference.document(user.getUserId());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              User user1= documentSnapshot.toObject(User.class);
              if(user1!=null)
              flag=1;
            }
        });
        if(flag==1){
            return true;
        }else
            return false;
    }

    public void getUserfromFireBase(final User user, final Context context){

        User user1=new User();
        new AsyncTask<Void, Void,User>() {
            private User user3=new User();
            @Override
            protected User doInBackground(Void... voids) {
                collectionReference.document(user.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user3=documentSnapshot.toObject(User.class);
                        flag=1;
                        Log.i(TAG, "onSuccess: ClientPASSWORD4"+user.getClientPassword());
                        Log.i(TAG, "onSuccess: "+user.getUserId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        flag=0;
                    }
                });

                return user3;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                DatabaseHelper databaseHelper=new DatabaseHelper(context);
                Log.i(TAG, "onPostExecute: "+user.getUserId());
                databaseHelper.setClient(user);
            }
        }.execute();

    }

    public void ServerlogoutUser(final User user){
        DocumentReference documentReference=collectionReference.document(user.getUserId());
        user.setLoginStatus(0);
        user.setClientMode(0);
        Log.i(TAG, "ServerlogoutUser: "+user.getClientPassword());

        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Log.i(TAG, "onComplete: SuccessLogOut"+user.getClientPassword());
                }else{
                    Log.i(TAG, "onComplete: Failed");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    public void checkLogin(final User user, final Context context, final View view){
        Log.i(TAG, "checkLogin: "+user.getUserId());
        DocumentReference documentReference=collectionReference.document(user.getUserId());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user1 = documentSnapshot.toObject(User.class);
                if(user1!=null) {
                    Log.i(TAG, "onSuccess: " + user1.getUserId() + user1.getPassword());

                    if (user1.getPassword().equals(user.getPassword())) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.logIn(user);
                        databaseHelper.setAdmin(user);
                        Log.i(TAG, "onComplete: " + user.getLoginStatus() + "," + user.getClientMode());

                        Intent i = new Intent(context, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);


                    }else{
                        Toast.makeText(context, "Username and Password not matched...", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i(TAG, "onSuccess: username not found");
                   // Snackbar.make(view,"EmailID not registered with us ",Snackbar.LENGTH_INDEFINITE);
                    Toast.makeText(context, "Email Id not registered.\n      Register Please ...", Toast.LENGTH_SHORT).show();
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "User Name Not Found", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void setClientPassword(final User user,final Context context){
        DocumentReference documentReference=collectionReference.document(user.getUserId());

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Client Password Set.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable Client Password.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void checkClientLogin(final User user, final Context context, final View view){
        Log.i(TAG, "checkLogin: "+user.getUserId());
        DocumentReference documentReference=collectionReference.document(user.getUserId());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user1 = documentSnapshot.toObject(User.class);
                if(user1!=null ) {
                    Log.i(TAG, "onSuccess: " + user1.getUserId() + user1.getClientPassword());

                    if (user1.getClientPassword().equals(user.getClientPassword())) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        user1.setClientMode(1);
                        user1.setLoginStatus(user.getLoginStatus());
                        Log.i(TAG, "onSuccessLogout: "+user1.getClientPassword());
                        databaseHelper.setClient(user1);
                        //databaseHelper.logIn(user1);
                        //databaseHelper.setAdmin(user1);
                        Log.i(TAG, "onComplete: " + user.getLoginStatus() + "," + user.getClientMode());

                        Intent i = new Intent(context, ClientActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(i);


                    }else{
                        Toast.makeText(context, "Username and Password not matched...", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i(TAG, "onSuccess: username not found");
                    // Snackbar.make(view,"EmailID not registered with us ",Snackbar.LENGTH_INDEFINITE);
                    Toast.makeText(context, "Email Id not registered.\n      Register Please ...", Toast.LENGTH_SHORT).show();
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "User Name Not Found", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

package dao.manishasah.com.manishaboutique.Dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

import dao.manishasah.com.manishaboutique.Model.Request;
import dao.manishasah.com.manishaboutique.Model.User;
import dao.manishasah.com.manishaboutique.Model.UserTokens;
import dao.manishasah.com.manishaboutique.PushNotificationHelper;

/**
 * Created by Sourav Guchait on 4/24/2018.
 */

public class UserTokensDao {
    private CollectionReference collectionReference;
    private String TAG="USER_TOKENS_DAO";
    private UserTokens userTokens;
    public UserTokensDao() {
        this.collectionReference = FirebaseFirestore.getInstance().collection("UserTokens");
    }

    public void setUserToken(UserTokens userToken){
        collectionReference.document(userToken.getUser().getUserId()).set(userToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccessTOKENSET: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: TOKEN NOT SET");
            }
        });
    }

    public UserTokens getUserTokens(User user, final Context context, final Request request){
       collectionReference.document(user.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               userTokens=documentSnapshot.toObject(UserTokens.class);
               /*FirebaseMessaging fm = FirebaseMessaging.getInstance();
               Log.i(TAG, "onSuccess: REemoteMessage");
               fm.send(new RemoteMessage.Builder(userTokens.getToken() + "@fcm.googleapis.com")
                       .setMessageId(Integer.toString(1))
                       .addData("my_message", "Hello World")
                       .addData("my_action","SAY_HELLO")
                       .build());

               */

               Thread thread=new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           PushNotificationHelper.sendPushNotification(userTokens.getToken(),request);
                       } catch (IOException e) {
                           Toast.makeText(context, "no notification generated", Toast.LENGTH_SHORT).show();
                           e.printStackTrace();
                       }


                   }
               });
               thread.start();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               userTokens=new UserTokens();
               Log.i(TAG, "onFailure_USER_TOKEN: ");
           }
       });
       return userTokens;
    }

}

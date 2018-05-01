package dao.manishasah.com.manishaboutique.Dao;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import dao.manishasah.com.manishaboutique.Model.Request;
import dao.manishasah.com.manishaboutique.Model.User;
import dao.manishasah.com.manishaboutique.RequestGridAdapter;

/**
 * Created by Sourav Guchait on 4/16/2018.
 */

public class RequestDao {

    private CollectionReference collectionReference;
    private User user;
    private ArrayList<Request> requests;
    private String TAG="RequestDao";
    private FirebaseFirestore firebaseFirestore;
    public RequestDao(User user) {
        this. firebaseFirestore=FirebaseFirestore.getInstance();
        this.collectionReference = firebaseFirestore.collection("Request");

        requests=new ArrayList<>();
        this.user=user;
    }

    public void addRequest(Request request)
    {
        DocumentReference documentReference=collectionReference.document(request.getUserId()).collection(request.getDateTime()).document(request.getProduct().getProductName());
        documentReference.set(request);

    }

    public ArrayList<Request> getAllrequests(final Context context, final GridView gridView){
       collectionReference.document(user.getUserId()).collection(Calendar.getInstance().getTime().toString().substring(0,10)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                       Request request1=queryDocumentSnapshot.toObject(Request.class);
                          requests.add(request1);
                           Log.i(TAG, "onComplete: " + request1.getDateTime());

                   }
                   RequestGridAdapter requestGridAdapter=new RequestGridAdapter(context,requests);
                   gridView.setAdapter(requestGridAdapter);
               }
           }
       });


        return requests;
    }



}

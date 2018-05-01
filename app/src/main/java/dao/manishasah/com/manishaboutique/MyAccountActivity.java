package dao.manishasah.com.manishaboutique;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dao.manishasah.com.manishaboutique.Dao.DatabaseHelper;
import dao.manishasah.com.manishaboutique.Model.User;

public class MyAccountActivity extends AppCompatActivity {

    private TextView textViewMyEmail;
    private TextView textViewMyClientPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);;
        setContentView(R.layout.activity_my_account);
        textViewMyEmail=findViewById(R.id.textViewMyEmail);
        textViewMyClientPassword=findViewById(R.id.textViewMyClientPassword);
        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        User user= databaseHelper.fetchUserDetails();
        textViewMyEmail.setText(user.getUserId());
        textViewMyClientPassword.setText("Client Password : "+user.getClientPassword());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                FirebaseFirestore.getInstance().collection("User").
                        document(textViewMyEmail.getText().toString()).
                        get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                     textViewMyClientPassword.setText("Client Password : "+documentSnapshot.toObject(User.class).getClientPassword());
                    }
                });

                return null;
            }
        }.execute();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

package dao.manishasah.com.manishaboutique.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import dao.manishasah.com.manishaboutique.Model.User;

/**
 * Created by Sourav Guchait on 4/18/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private String TAG="TAG";
    public DatabaseHelper(Context context) {
        super(context, "mydb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE user (userId text,clientMode int,theme int ,password text,clientPassword text,loginStatus int);");

    }

    public void addtoLastUser(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("userId",user.getUserId());
        values.put("clientMode",user.getClientMode());
        values.put("theme",user.getTheme());
        values.put("password",user.getPassword());
        values.put("clientPassword",user.getClientPassword());
        values.put("loginStatus",user.getLoginStatus());

       // Log.i(TAG, "addtoLastUser: "+user.getUserId()+","+user.getPassword()+","+user.getLoginStatus());
        sqLiteDatabase.insert("user",null,values);

    }

    public void logoutUser(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL("update user set loginStatus=0,clientMode=0 where userId='"+user.getUserId()+"';");
    }

    public void logIn(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL("update user set loginStatus=1 where userId='"+user.getUserId()+"';");
    }


    public void setClient(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        try{
            if(fetchUserDetails()==null){
                addtoLastUser(user);
            }else {
                if (fetchUserDetails().getUserId().equals(user.getUserId())) {
                    sqLiteDatabase.execSQL("update user set clientMode=1 ,loginStatus=" + user.getLoginStatus() + "   where userId='" + user.getUserId() + "';");
                } else {
                    addtoLastUser(user);
                }
                Log.i(TAG, "setClient: ADDED TO LAST USER" + user.getClientMode() + "," + user.getLoginStatus());
            }
        }catch(SQLException e ) {
            Log.i(TAG, "setClient: EXCETEPTION CAUGHT"+user.getClientMode()+","+user.getLoginStatus());

        }
    }


    public void setLocalPassword(User user){
        addtoLastUser(user);
    }
    public void setAdmin(User user){
        /*SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        try{
            addtoLastUser(user);
        }catch(SQLException e ){
            sqLiteDatabase.execSQL("update user set clientMode=0 where userId='"+user.getUserId()+"';");

        }
*/

        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        try{
            if(fetchUserDetails()==null){
                addtoLastUser(user);
            }else {
                if (fetchUserDetails().getUserId().equals(user.getUserId())) {
                    sqLiteDatabase.execSQL("update user set clientMode=0 ,loginStatus=" + user.getLoginStatus() + "   where userId='" + user.getUserId() + "';");
                } else {
                    addtoLastUser(user);
                }
              //  Log.i(TAG, "setClient: ADDED TO LAST USER" + user.getClientMode() + "," + user.getLoginStatus());
            }
        }catch(SQLException e ) {
           // Log.i(TAG, "setClient: EXCETEPTION CAUGHT"+user.getClientMode()+","+user.getLoginStatus());

        }
    }





    public User fetchUserDetails(){
        SQLiteDatabase db=getReadableDatabase();
        User user=new User();
       Cursor cursor= db.rawQuery("select * from user",null);
       if (cursor.moveToFirst()){
            do{
                user.setUserId(cursor.getString(0));
                user.setClientMode(cursor.getInt(1));
                user.setTheme(cursor.getInt(2));
                user.setPassword(cursor.getString(3));
                user.setClientPassword(cursor.getString(4));
                Log.i(TAG, "fetchUserDetails: "+cursor.getString(0)+","+cursor.getInt(1));
                user.setLoginStatus(cursor.getInt(5));
            }while (cursor.moveToNext());

       }
       return user;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

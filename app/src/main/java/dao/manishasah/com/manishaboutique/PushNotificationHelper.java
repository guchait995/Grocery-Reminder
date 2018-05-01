package dao.manishasah.com.manishaboutique;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Sourav Guchait on 4/24/2018.
 */

public class PushNotificationHelper {
    public final static String AUTH_KEY_FCM = "AIzaSyBrDvzT4m9RoPaDSp5h6Dm39owqphTSe68";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public final static String API_URL_FCMS ="https://fcm.googleapis.com/v1/projects/manishaboutique-a299d/messages:send HTTP/1.1";
    private  String TAG ="NOTIFICATION_CLASS";

    public PushNotificationHelper() throws FileNotFoundException {
    }

    public static String sendPushNotification(String deviceToken,dao.manishasah.com.manishaboutique.Model.Request request1)
            throws IOException {
        String result = "";
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \r\n    \"to\" : \""+deviceToken+"\",\r\n    \"notification\" : {\r\n      \"body\" : \""+request1.getProduct().getProductName().toUpperCase()+"\",\r\n      \"title\" : \"Grocery Reminder\"\r\n      }\r\n   \r\n}");
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "key=AIzaSyBrDvzT4m9RoPaDSp5h6Dm39owqphTSe68")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "4e7c2277-c7a0-20a2-7f3d-36e4ed3b5d65")
                .build();

        Response response = client.newCall(request).execute();

       /* URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        try {
            json.put("to", deviceToken.trim());
            JSONObject info = new JSONObject();
            info.put("title", "notification title"); // Notification title
            info.put("body", "message body"); // Notification
            // body
            json.put("notification", info);
            json.put("content_available",true);
            Log.i("TAG", "sendPushNotification: "+deviceToken);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("TAG", "sendPushNotificationERROR1: "+e.getMessage());
        }
        try {
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(json.toString());
            Log.i("TAG", "sendPushNotification: "+json.toString());
            conn.connect();
            Log.i("TAG", "sendPushNotification: "+conn.getContent().toString());

            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            //result = CommonConstants.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "sendPushNotificationERROR: "+e.getMessage()+e.toString());
            // result = CommonConstants.FAILURE;
        }
        System.out.println("GCM Notification is sent successfully");

       */ return result;

    }



}
package dao.manishasah.com.manishaboutique.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import dao.manishasah.com.manishaboutique.ImageLoadTask;
import dao.manishasah.com.manishaboutique.Model.Product;

/**
 * Created by Sourav Guchait on 4/16/2018.
 */

public class ImageSearch extends AsyncTask<Void,Void,Void>{
// now have some fun with the results...
    private final String API_KEY="AIzaSyD37bzHK6grmzu13gEM1B2f4aEdh07r0gk";
    private final String searchEngineID="011030291067126260859:aycrn-_quqe";
    private JSONObject jsonObject;
    private String TAG="TAG1";
    private String query;
    private Context context;
    private String immageurl;
    private Product product;
    public ImageSearch(Product product, Context context){
        this.query=product.getProductName();
        this.context=context;
        this.product=product;
        Log.i(TAG, "ImageSearch: "+product.getProductName());
    }

public ArrayList<String> searchImage(String s){

    ArrayList<String> imageUrlStrings=new ArrayList<>();
    URL url = null;

    /*OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://www.googleapis.com/customsearch/v1?cx=011030291067126260859%3Aaycrn-_quqe&q=banana&key=AIzaSyD37bzHK6grmzu13gEM1B2f4aEdh07r0gk&num=1")
            .get()
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "9d21b4fe-e487-d820-9187-1dec80cea7dd")
            .build();

    try {
        Response response = client.newCall(request).execute();
        Log.i(TAG, "searchImage: "+response.toString());
        StringBuilder stringBuilder=new StringBuilder();

       // BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(response));
        JsonParser jsonParser=new JsonParser();

        JsonElement jsonObject= jsonParser.parse(response.toString());
        JsonObject jsonObject1= jsonObject.getAsJsonObject();
        JsonArray jsonArrayItems= jsonObject1.get("items").getAsJsonArray();
      String urlf =  jsonArrayItems.get(0).getAsJsonObject().get("metatags").getAsJsonArray().get(0).getAsJsonObject().get("og:image").getAsString();
        Log.i(TAG, "searchImage8: "+urlf);

    } catch (IOException e) {
        e.printStackTrace();
    }*/

    try {
        String urls="https://www.googleapis.com/customsearch/v1?cx=011030291067126260859:aycrn-_quqe&q="+query+"&key=AIzaSyAf16RrC1sdee_M6X1r_3aTMDCOBuWRpwU&num=1";
        url = new URL(urls);
        String imageurl=null;
        URLConnection connection = url.openConnection();
       // https://www.googleapis.com/customsearch/v1?q=apple&cx=011030291067126260859:aycrn-_quqe&key=AIzaSyD37bzHK6grmzu13gEM1B2f4aEdh07r0gk' \-H 'Accept: application/json

        //connection.addRequestProperty("Referer", " Enter the URL of your site here ");
        Log.i(TAG, "searchImage: "+url.getContent());
        Log.i(TAG, "searchImage3: "+url.getContent().toString());
        String line=null;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while((line = reader.readLine()) != null) {
            Log.i(TAG, "searchImage4: "+line);
            /*if(line.contains("og:image")){
               imageurl= line.substring(19,line.length()-2);
                imageUrlStrings.add(imageurl);
                Log.i(TAG, "searchImage5: "+imageurl);
            }*/
            builder.append(line);
        }
        Log.i(TAG, "searchImage15: "+builder.toString());
        JsonParser jsonParser=new JsonParser();

        JsonElement jsonObject= jsonParser.parse(builder.toString());
        JsonObject jsonObject1= jsonObject.getAsJsonObject();
        JsonArray jsonArrayItems= jsonObject1.get("items").getAsJsonArray();
        Log.i(TAG, "searchImage84: "+jsonArrayItems.get(0).toString());
        JsonObject jsonObj1 =  jsonArrayItems.get(0).getAsJsonObject().get("pagemap").getAsJsonObject();
        Log.i(TAG, "searchImage86: "+jsonObj1.toString());
        JsonArray jsonArray=jsonObj1.get("cse_thumbnail").getAsJsonArray();
        Log.i(TAG, "searchImage87: "+jsonArray.toString());
        JsonObject jsonObject2=jsonArray.get(0).getAsJsonObject();
        String urlimage=jsonObject2.get("src").getAsString();
        Log.i(TAG, "searchImage88: "+urlimage);
        imageUrlStrings.add(urlimage);

        // String urlf=      jsonArrayItems1.get(0).getAsJsonObject().get("og:image").getAsString();
      //  Log.i(TAG, "searchImage83: "+jsonArrayItems1.get(0).getAsString());
    } catch (MalformedURLException e) {
        Log.i(TAG, "searchImage82: "+e.getMessage());
        e.printStackTrace();
    } catch (IOException e) {

        Log.i(TAG, "searchImage81: "+e.getMessage());
        e.printStackTrace();
    }
    return imageUrlStrings;
}


    @Override
    protected Void doInBackground(Void... voids) {
        Log.i(TAG, "doInBackground: ");
        if(searchImage(query).size()>0)
            immageurl= searchImage(query).get(0);
        Log.i(TAG, "doInBackground: "+immageurl);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
        Log.i(TAG, "onPostExecute: "+immageurl);
        ImageLoadTask imageLoadTask=new ImageLoadTask(immageurl,product,context);
        imageLoadTask.execute();
    }
}

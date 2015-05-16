package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WebAPI extends AsyncTask<Pair<List<NameValuePair>, HttpClient>, Void, String> {
    private Context context = null;
    private String serverURL, methodName;
    private static HttpClient httpClient = null;
    private static final String TAG = "MyAssetManger-log";
    private static final String sql = "kark.hin.no";
    private static final int sqlport = 3306;
    private static final String username = "karlsen";
    private static final String password = "530617Pass";
    private static final String db = "stud_v15_karlsen";
    private static final String URL = "http://kark.hin.no:8088/d3330log_backend/";

    public WebAPI(String serverURL, String methodName, Context context) {
        this.serverURL = serverURL;
        this.methodName = methodName;
        this.context = context;
    }

    @Override
    protected String doInBackground(Pair<List<NameValuePair>, HttpClient>... params) {
        Pair pair = params[0];
        List<NameValuePair> urlParams = (List<NameValuePair>)pair.first;
        HttpClient httpClient = (HttpClient)pair.second;
        try {
            String requestURL = serverURL + methodName;
            HttpPost httpPost = new HttpPost(requestURL);
            httpPost.setEntity(new UrlEncodedFormEntity(urlParams));
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            }
            return "Error: " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
        } catch (ClientProtocolException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //((ActivityMain)context).showLoginResult(result);
        Log.d(TAG, result);
        //Gson gson = new Gson();
        //User user = gson.fromJson(result, User.class);
        //Log.d(TAG,user.getLastname());
        Intent intent = new Intent();
        //Log.d(TAG, intent.getStringExtra("result"));
    }


    public static void doLogin(Context context) {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("uid", "530617"));
            nameValuePairs.add(new BasicNameValuePair("pwd", "kurt"));
            nameValuePairs.add(new BasicNameValuePair("connectstring", "jdbc:mysql://" + sql + ":" + sqlport + "/"));
            nameValuePairs.add(new BasicNameValuePair("dbName", db));
            nameValuePairs.add(new BasicNameValuePair("db_uid", username));
            nameValuePairs.add(new BasicNameValuePair("db_pwd", password));
            new WebAPI(URL, "logIn", context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Du er allerede logget inn!");
        }
    }



    public static void doGetEquipment(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("which_equipment", "ALL"));
            new WebAPI(URL, "getEquipment", context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Logg inn først!");
        }
    }
}
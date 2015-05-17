package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import no.hin.student.myassetmanager.Activities.ActivityMain;


public class WebAPI extends AsyncTask<Pair<List<NameValuePair>, HttpClient>, Void, String> {
    private Context context = null;
    private String serverURL, methodName;
    private Method method;

    private static HttpClient httpClient = null;


    private static final String TAG = "MyAssetManger-log";
    private static final String sql = "kark.hin.no";
    private static final int sqlport = 3306;
    private static final String username = "karlsen";
    private static final String password = "530617Pass";
    private static final String db = "stud_v15_karlsen";
    private static final String URL = "http://kark.hin.no:8088/d3330log_backend/";



    public enum Method {
        LOG_IN(0, "logIn"),
        GET_EQUIPMENT(1, "getEquipment"),
        DELETE_USER(2, "deleteUser"),
        GET_EQUIPMENTTYPE(3, "getEquipmentType"),
        GET_USERS(3, "getUsers");

        private int type;
        private String text;

        Method(int i, String text) {
            this.type = i;
            this.text = text;
        }

        public int getId() {
            return type;
        }

        public String getText() {
            return text;
        }
    };


    public WebAPI(String serverURL, Method method, Context context) {
        this.method = method;
        this.serverURL = serverURL;
        this.methodName = method.getText();
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
        Log.d(TAG, result);
        Gson gson = new Gson();
        ResponseMsg response = gson.fromJson(result, ResponseMsg.class);


        switch (method) {
            case LOG_IN:
                User user = gson.fromJson(response.getJsonResponse(), User.class);
                ((ActivityMain)context).logIn(user);
                break;
            case GET_EQUIPMENT:
                Type equipmentListType = new TypeToken<List<Equipment>>(){}.getType();
                ArrayList<MyObjects> equipments = (ArrayList<MyObjects>) gson.fromJson(response.getJsonResponse(), equipmentListType);
                ((ActivityMain)context).addToList(equipments);
                break;
            case GET_EQUIPMENTTYPE:
                Type equipmentTypeListType = new TypeToken<List<Equipment>>(){}.getType();
                ArrayList<MyObjects> equipmentType = (ArrayList<MyObjects>) gson.fromJson(response.getJsonResponse(), equipmentTypeListType);
                ((ActivityMain)context).addToList(equipmentType);
                break;
            case GET_USERS:
                Type usersListType = new TypeToken<List<User>>(){}.getType();
                ArrayList<MyObjects> users = (ArrayList<MyObjects>) gson.fromJson(response.getJsonResponse(), usersListType);
                ((ActivityMain)context).addToList(users);
                break;
        }
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
            new WebAPI(URL, Method.LOG_IN, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Du er allerede logget inn!");
        }
    }

    public static void doGetUsers(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            new WebAPI(URL, Method.GET_USERS, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Logg inn først!");
        }
    }

    public static void doGetEquipment(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("which_equipment", "ALL"));
            new WebAPI(URL, Method.GET_EQUIPMENT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Logg inn først!");
        }
    }


    public static void doGetEquipmentType(Context context, String category) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("type", category));
            Log.d(TAG, category);
            new WebAPI(URL, Method.GET_EQUIPMENTTYPE, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Logg inn først!");
        }
    }
}
package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

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
    private static String databaseUsername;
    private static String databasePassword;
    private static final String db = "stud_v15_karlsen";
    private static final String URL = "http://kark.hin.no:8088/d3330log_backend/";



    public enum Method {
        LOG_IN(1001, "logIn"),
        LOG_IN_ADMIN(1002, "logInAdmin"),
        LOG_OUT(1999, "logOut"),

        GET_EQUIPMENT(2001, "getEquipment"),
        GET_EQUIPMENTTYPE(2002, "getEquipmentType"),
        GET_USERS(2003, "getUsers"),
        GET_ALL_LOG_ENTRIES_FOR_ALL_USER(2004, "getAllLogEntriesForAllUser"),

        ADD_EQUIPMENT(1001, "addEquipment"),

        DELETE_USER(4001, "deleteUser");

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
        try {
            Pair pair = params[0];
            List<NameValuePair> urlParams = (List<NameValuePair>)pair.first;
            HttpClient httpClient = (HttpClient)pair.second;

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
        try {
            Log.d(TAG, result);
            Gson gson = new Gson();
            ResponseMsg response = gson.fromJson(result, ResponseMsg.class);
            if (response.getMessage().contains("518") || response.getMessage().contains("517")) {
                Toast.makeText(context, "Du må være admin!", Toast.LENGTH_SHORT).show();
            } else {
                switch (method) {
                    case LOG_IN:
                        if (response.getMessage().contains("512")) {
                            ((ActivityMain) context).logIn(null, false);
                            closeSession();
                        }
                        else {
                            User log_in = gson.fromJson(response.getJsonResponse(), User.class);
                            ((ActivityMain) context).logIn(log_in, true);
                        }
                        break;
                    case LOG_IN_ADMIN:
                        if (response.getMessage().contains("512")) {
                            ((ActivityMain) context).logIn(null, false);
                            closeSession();
                        }
                        else {
                            User log_in_admin = gson.fromJson(response.getJsonResponse(), User.class);
                            ((ActivityMain) context).logIn(log_in_admin, true);
                        }

                        break;
                    case LOG_OUT:
                        closeSession();
                        break;


                    case GET_EQUIPMENT:
                        Type get_equipment = new TypeToken<List<Equipment>>() {
                        }.getType();
                        ArrayList<AssetManagerObjects> equipment = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_equipment);
                        ((ActivityMain) context).addToList(equipment);
                        break;
                    case GET_EQUIPMENTTYPE:
                        Type get_equipmenttype = new TypeToken<List<Equipment>>() {
                        }.getType();
                        ArrayList<AssetManagerObjects> equipment_type = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_equipmenttype);
                        ((ActivityMain) context).addToList(equipment_type);
                        break;
                    case GET_USERS:
                        Type get_users = new TypeToken<List<User>>() {
                        }.getType();
                        ArrayList<AssetManagerObjects> users = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_users);
                        ((ActivityMain) context).addToList(users);
                        break;
                    case GET_ALL_LOG_ENTRIES_FOR_ALL_USER:
                        Type get_all_log_entries_for_all_user = new TypeToken<List<LogEntry>>() {
                        }.getType();
                        ArrayList<AssetManagerObjects> logEntries = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_all_log_entries_for_all_user);
                        ((ActivityMain) context).addToList(logEntries);


                    case ADD_EQUIPMENT:

                        break;
                    case DELETE_USER:
                        ((ActivityMain) context).deleteUser();
                        break;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void closeSession() {
        httpClient.getConnectionManager().closeExpiredConnections();
        httpClient.getConnectionManager().shutdown();
        httpClient = null;
    }

    public static void setDatabaseLoginInformation(String dbUser, String dbPassword)  {
        databaseUsername = dbUser;
        databasePassword = dbPassword;
    }


    public static void doLogin(Context context, String username, String password) {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("uid", username));
            nameValuePairs.add(new BasicNameValuePair("pwd", password));
            nameValuePairs.add(new BasicNameValuePair("connectstring", "jdbc:mysql://" + sql + ":" + sqlport + "/"));
            nameValuePairs.add(new BasicNameValuePair("dbName", db));
            nameValuePairs.add(new BasicNameValuePair("db_uid", databaseUsername));
            nameValuePairs.add(new BasicNameValuePair("db_pwd", databasePassword));
            new WebAPI(URL, Method.LOG_IN, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Du er allerede logget inn!");
        }
    }

    public static void doLoginAdmin(Context context, String username, String password) {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("uid", username));
            nameValuePairs.add(new BasicNameValuePair("pwd", password));
            nameValuePairs.add(new BasicNameValuePair("connectstring", "jdbc:mysql://" + sql + ":" + sqlport + "/"));
            nameValuePairs.add(new BasicNameValuePair("dbName", db));
            nameValuePairs.add(new BasicNameValuePair("db_uid", databaseUsername));
            nameValuePairs.add(new BasicNameValuePair("db_pwd", databasePassword));
            new WebAPI(URL, Method.LOG_IN_ADMIN, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Du er allerede logget inn!");
        }
    }

    public static void logOut(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(0);
            new WebAPI(URL, Method.LOG_OUT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Du er ikke logget på og kan derfor ikke logge ut.");
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

    public static void doDeleteUser(Context context, int userId) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("userId", Integer.toString(userId)));
            Log.d(TAG, Integer.toString(userId));
            new WebAPI(URL, Method.DELETE_USER, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Logg inn først!");
        }
    }

    public static void addEquipment(Context context, Equipment equipment) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            Log.d(TAG, "Before adding: " + equipment.toJSONString());
            nameValuePairs.add(new BasicNameValuePair("equipment", equipment.toJSONString()));
            new WebAPI(URL, Method.ADD_EQUIPMENT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Logg inn først!");
        }
    }

    public static void doGetAllLogEntriesForAllUser(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            new WebAPI(URL, Method.GET_ALL_LOG_ENTRIES_FOR_ALL_USER, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Logg inn først!");
        }
    }
}
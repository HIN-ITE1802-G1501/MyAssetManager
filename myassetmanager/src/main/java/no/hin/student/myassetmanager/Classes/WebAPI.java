/**
* This class contains all the WebAPI methods for sending request to the backend
 * server.
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.4
*/

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.R;


public class WebAPI extends AsyncTask<Pair<List<NameValuePair>, HttpClient>, Void, String> {
    private Context context = null;             // ActivityMain contect
    private String serverURL, methodName;       // Server URL and method name for handling request
    private Method method;                      // The method we are requesting

    private static HttpClient httpClient = null;    // Http client for session


    private static final String sql = "kark.hin.no";                                // The backend server address
    private static final int sqlport = 3306;                                        // Backend server tcp-port
    private static String databaseUsername;                                         // Database username for authentication
    private static String databasePassword;                                         // Database password for authentication
    private static final String db = "stud_v15_karlsen";                            // Datebase name
    private static final String URL = "http://kark.hin.no:8088/d3330log_backend/";  // Backend API
    private static final String alreadyLoggedIn = "User is already logged in.";     // Logged in message
    private static final String requireLogin =  "Requires login";                   // Require login message



    /**
     * Enum for identifying which request we are sending to the backend
     */
    public enum Method {
        LOG_IN(1001, "logIn"),
        LOG_IN_ADMIN(1002, "logInAdmin"),
        LOG_OUT(1999, "logOut"),

        GET_EQUIPMENT(2000, "getEquipment"),
        GET_EQUIPMENT_ALL(2001, "getEquipment"),
        GET_EQUIPMENT_ALL_FOR_EQUIPMENT_STATUS(2002, "getEquipment"),
        GET_EQUIPMENT_AVAILABLE_FOR_EQUIPMENT_STATUS(2003, "getEquipment"),
        GET_EQUIPMENT_IN_USE_FOR_EQUIPMENT_STATUS(2004  , "getEquipment"),
        GET_EQUIPMENTTYPE(2005, "getEquipmentType"),
        GET_USERS(2006, "getUsers"),
        GET_USERS_FOR_LOAN_FRAGMENT(2007, "getUsers"),
        GET_ALL_LOG_ENTRIES_FOR_ALL_USER(2008, "getAllLogEntriesForAllUser"),
        GET_OPEN_LOG_ENTRIES_FOR_REGISTER_RESERVATION_IN(2009, "getOpenLogEntries"),
        GET_ACTIVE_USERS(2010, "getUsers"),
        GET_NOT_ACTIVED_USERS(2011, "getNotActivatedUsers"),
        GET_LOG_ENTRIES_FOR_USER_FRAGMENT(2007, "getLogEntriesForUser"),

        ADD_EQUIPMENT(3001, "addEquipment"),
        ADD_USER_WITHOUT_LOGIN(3002, "addUserWithoutLogin"),
        ADD_USER(3003, "addUser"),

        UPDATE_USER(4001, "updateUser"),
        UPDATE_EQUIPMENT(4002, "updateEquipment"),
        CHANGE_USER_PASSWORD(4003, "changeUserPassword"),


        DELETE_USER(5001, "deleteUser"),
        DELETE_EQUIPMENT(5002, "deleteEquipment"),

        REGISTER_RESERVATION_OUT(6001, "registerReservationOut"),
        REGISTER_RESERVATION_IN(6002, "registerReservationIn"),

        GET_EQUIPMENT_WITHOUT_LOGIN(9001, "getEquipmentWithoutLogin");


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


    /**
     * Default constructor
     *
     * @param serverURL server address to backend
     * @param method request method to backend
     * @param context ActivityMain context
     */
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
        }
        catch (ClientProtocolException e) {
            return e.getMessage();
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            Log.d(App.TAG, result);
            Gson gson = new Gson();
            ResponseMsg response = gson.fromJson(result, ResponseMsg.class);
            Log.e("RESPONSE_MESSAGE", response.getMessage());

            if (response.getMessage().contains("500")) {
                App.notifyUser(R.string.WEBAPI_CODE_500);
            } else if (response.getMessage().contains("518")) {
                App.notifyUser(R.string.WEBAPI_CODE_518);
            } else if (response.getMessage().contains("512")) {
                App.notifyUser(R.string.WEBAPI_CODE_512);
                closeSession();
                Login.setUserRole(Login.UserRole.LOGGED_OUT);
                ((ActivityMain) context).replaceFragmentContainerFragmentWith(((ActivityMain) context).fragmentLogin);
            }
            else  {
                switch (method) {
                    case LOG_IN:
                        if (response.getMessage().contains("512")) {
                            Login.logIn(context, null, false, Login.UserRole.LOGGED_OUT);
                            closeSession();
                        } else {
                            EquipmentStatus.getUpdateFromDatabase(context);
                            User log_in = gson.fromJson(response.getJsonResponse(), User.class);
                            Login.logIn(context, log_in, true, Login.UserRole.REGULAR_USER);
                            Login.saveUserLoginToApp();
                        }
                        break;
                    case LOG_IN_ADMIN:
                        if (response.getMessage().contains("517")) {
                            App.notifyUser(R.string.WEBAPI_CODE_517);
                            closeSession();
                        } else {
                            EquipmentStatus.getUpdateFromDatabase(context);
                            User log_in_admin = gson.fromJson(response.getJsonResponse(), User.class);
                            Login.logIn(context, log_in_admin, true, Login.UserRole.ADMIN_USER);
                            Login.saveUserLoginToApp();
                        }
                        break;
                    case LOG_OUT:
                        closeSession();
                        Login.setUserRole(Login.UserRole.LOGGED_OUT);
                        ((ActivityMain) context).replaceFragmentContainerFragmentWith(((ActivityMain) context).fragmentLogin);
                        Login.deleteUserLoginToApp();
                        break;
                    case GET_EQUIPMENT_ALL:
                        Type get_equipment = new TypeToken<List<Equipment>>() { }.getType();
                        ArrayList<AssetManagerObjects> equipment = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_equipment);
                        ((ActivityMain) context).addToList(equipment);
                        break;
                    case GET_EQUIPMENT_ALL_FOR_EQUIPMENT_STATUS:
                        Type get_all_for_equipment_status = new TypeToken<List<Equipment>>() {}.getType();
                        ArrayList<Equipment> equipmentAllForEquipmentStatus = (ArrayList<Equipment>) gson.fromJson(response.getJsonResponse(), get_all_for_equipment_status);
                        EquipmentStatus.setAllEquipment(equipmentAllForEquipmentStatus);
                        break;
                    case GET_EQUIPMENT_AVAILABLE_FOR_EQUIPMENT_STATUS:
                        Type get_available_for_equipment_status = new TypeToken<List<Equipment>>() {}.getType();
                        ArrayList<Equipment> equipmentAvailableForEquipmentStatus = (ArrayList<Equipment>) gson.fromJson(response.getJsonResponse(), get_available_for_equipment_status);
                        EquipmentStatus.setAvailableEquipment(equipmentAvailableForEquipmentStatus);
                        break;
                    case GET_EQUIPMENT_IN_USE_FOR_EQUIPMENT_STATUS:
                        Type get_in_use_for_equipment_status = new TypeToken<List<Equipment>>() {}.getType();
                        ArrayList<Equipment> equipmentInUseForEquipmentStatus = (ArrayList<Equipment>) gson.fromJson(response.getJsonResponse(), get_in_use_for_equipment_status);
                        EquipmentStatus.setInUseEquipment(equipmentInUseForEquipmentStatus);
                        break;
                    case GET_EQUIPMENTTYPE:
                        Type get_equipmenttype = new TypeToken<List<Equipment>>() {
                        }.getType();
                        ArrayList<AssetManagerObjects> equipment_type = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_equipmenttype);
                        ((ActivityMain) context).addToList(equipment_type);
                        break;
                    case GET_USERS:
                        Type get_users = new TypeToken<List<User>>() {}.getType();
                        ArrayList<AssetManagerObjects> users = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_users);
                        ((ActivityMain) context).addToList(users);
                        break;
                    case GET_ACTIVE_USERS:
                        Type get_activeusers = new TypeToken<List<User>>() {}.getType();
                        ArrayList<AssetManagerObjects> activeusers = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_activeusers);
                        ((ActivityMain) context).addToList(activeusers);
                        break;
                    case GET_NOT_ACTIVED_USERS:
                        Type get_notactivedusers = new TypeToken<List<User>>() {}.getType();
                        ArrayList<AssetManagerObjects> notactivedusers = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_notactivedusers);
                        ((ActivityMain) context).addToList(notactivedusers);
                        break;
                    case GET_USERS_FOR_LOAN_FRAGMENT:
                        Type get_users_for_loan_fragment = new TypeToken<List<User>>() {}.getType();
                        ArrayList<User> usersForLoanFragment = (ArrayList<User>)gson.fromJson(response.getJsonResponse(), get_users_for_loan_fragment);
                        ((ActivityMain)context).populateLoanListViewWithUsers(usersForLoanFragment);
                        break;
                    case GET_ALL_LOG_ENTRIES_FOR_ALL_USER:
                        Type get_all_log_entries_for_all_user = new TypeToken<List<UserLogEntries>>() {}.getType();
                        ArrayList<AssetManagerObjects> logEntries = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_all_log_entries_for_all_user);
                        ((ActivityMain) context).addToList(logEntries);
                        break;
                    case GET_OPEN_LOG_ENTRIES_FOR_REGISTER_RESERVATION_IN:
                        Type get_open_log_entries_for_user_for_in = new TypeToken<List<LogEntry>>() {}.getType();
                        ArrayList<LogEntry> openLogEntriesForUser = (ArrayList<LogEntry>) gson.fromJson(response.getJsonResponse(), get_open_log_entries_for_user_for_in);
                        Equipment equipmentToRegisterIn = ((ActivityMain)context).getCurrentlyViewedEquipment();
                        int equipmentId = equipmentToRegisterIn.getE_id();

                        for (LogEntry entry : openLogEntriesForUser)
                            if (entry.getE_id() == equipmentId) {
                                WebAPI.doRegisterReservationIn(context, entry.getLe_id());
                                break;
                            }
                        break;
                    case GET_LOG_ENTRIES_FOR_USER_FRAGMENT:
                        Type get_log_entries_for_user_fragment = new TypeToken<List<LogEntry>>() {}.getType();
                        ArrayList<LogEntry> logEntriesForUserFragment = (ArrayList<LogEntry>) gson.fromJson(response.getJsonResponse(), get_log_entries_for_user_fragment);
                        ((ActivityMain) context).populateUserListViewWithUsers(logEntriesForUserFragment);
                        break;
                    case ADD_EQUIPMENT:
                        App.notifyUser(R.string.WEB_API_ADD_EQUIPMENT_SUCCESS);
                        EquipmentStatus.getUpdateFromDatabase(context);
                        ((ActivityMain) context).replaceFragmentContainerFragmentWith(((ActivityMain) context).fragmentList);
                        ((ActivityMain) context).addToList(Category.getCategories());
                        break;
                    case ADD_USER_WITHOUT_LOGIN:
                         WebAPI.doGetUsers(context, WebAPI.Method.GET_USERS);
                        closeSession();
                        break;
                    case ADD_USER:
                         WebAPI.doGetUsers(context, WebAPI.Method.GET_USERS);
                        break;
                    case UPDATE_USER:
                        App.notifyUser(R.string.WEB_API_UPDATE_USER_SUCCESS);
                        break;
                    case CHANGE_USER_PASSWORD:
                        App.notifyUser(R.string.WEB_API_UPDATE_USERPASSWORD_SUCCESS);
                        break;

                    case DELETE_USER:
                        WebAPI.doGetUsers(context, WebAPI.Method.GET_USERS);
                        break;
                    case UPDATE_EQUIPMENT:
                        App.notifyUser(R.string.WEB_API_UPDATE_EQUIPMENT_SUCCESS);
                        break;
                    case DELETE_EQUIPMENT:
                        ((ActivityMain) context).addToList(Category.getCategories());
                        break;
                    case REGISTER_RESERVATION_OUT:
                        App.notifyUser(R.string.WEB_API_REGISTER_RESERVATION_OUT);
                        EquipmentStatus.getUpdateFromDatabase(context);
                        break;
                    case REGISTER_RESERVATION_IN:
                        App.notifyUser(R.string.WEB_API_REGISTER_RESERVATION_IN);
                        EquipmentStatus.getUpdateFromDatabase(context);
                        break;
                    case GET_EQUIPMENT_WITHOUT_LOGIN:
                        Type get_equipment_without_login = new TypeToken<List<Equipment>>() { }.getType();
                        ArrayList<AssetManagerObjects> equipment_without_login = (ArrayList<AssetManagerObjects>) gson.fromJson(response.getJsonResponse(), get_equipment_without_login);
                        ((ActivityMain) context).addToList(equipment_without_login);
                        closeSession();
                        break;
                }
            }
        } catch (Exception e) {
            Log.d(App.TAG, e.toString());
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
            Log.d(App.TAG, alreadyLoggedIn);
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
            Log.d(App.TAG, alreadyLoggedIn);
        }
    }

    public static void logOut(Context context) {
        Log.d(App.TAG, "Logout method from WebAPI");
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(0);
            new WebAPI(URL, Method.LOG_OUT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doGetUsers(Context context, Method method) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            new WebAPI(URL, method, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }


    public static void doGetOpenLogEntriesForUser(Context context, Method method, int userId) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            new WebAPI(URL, method, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
            nameValuePairs.add(new BasicNameValuePair("userId", Integer.toString(userId)));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }


    public static void doGetEquipmentAll(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("which_equipment", "ALL"));
            new WebAPI(URL, Method.GET_EQUIPMENT_ALL, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doGetEquipmentAllForStatus(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("which_equipment", "ALL"));
            new WebAPI(URL, Method.GET_EQUIPMENT_ALL_FOR_EQUIPMENT_STATUS, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doGetEquipmentAvailable(Context context)
    {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("which_equipment", "AVAILABLE"));
            new WebAPI(URL, Method.GET_EQUIPMENT_AVAILABLE_FOR_EQUIPMENT_STATUS, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doGetEquipmentInUse(Context context)
    {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("which_equipment", "IN_USE"));
            new WebAPI(URL, Method.GET_EQUIPMENT_IN_USE_FOR_EQUIPMENT_STATUS, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doGetEquipmentType(Context context, String category) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("type", category));
            Log.d(App.TAG, category);
            new WebAPI(URL, Method.GET_EQUIPMENTTYPE, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doDeleteUser(Context context, int userId) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("userId", Integer.toString(userId)));
            Log.d(App.TAG, Integer.toString(userId));
            new WebAPI(URL, Method.DELETE_USER, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doDeleteEquipment(Context context, int equipmentId) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("equipmentId", Integer.toString(equipmentId)));
            Log.d(App.TAG, Integer.toString(equipmentId));
            new WebAPI(URL, Method.DELETE_EQUIPMENT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }


    public static void doAddEquipment(Context context, Equipment equipment) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("equipment", equipment.toJSONString()));
            new WebAPI(URL, Method.ADD_EQUIPMENT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doGetAllLogEntriesForAllUser(Context context) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            new WebAPI(URL, Method.GET_ALL_LOG_ENTRIES_FOR_ALL_USER, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doGetOpenLogEntries(Context context, Method method, int userId) {
        if (httpClient != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(userId)));
            new WebAPI(URL, method, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(App.TAG, requireLogin);
        }
    }

    public static void doAddUserWithoutLogin(Context context, User user) {
        if (httpClient == null)
            httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = null;
        nameValuePairs = new ArrayList<NameValuePair>(5);
        nameValuePairs.add(new BasicNameValuePair("user", user.toJSONString()));
        nameValuePairs.add(new BasicNameValuePair("connectstring", "jdbc:mysql://" + sql + ":" + sqlport + "/"));
        nameValuePairs.add(new BasicNameValuePair("dbName", db));
        nameValuePairs.add(new BasicNameValuePair("db_uid", databaseUsername));
        nameValuePairs.add(new BasicNameValuePair("db_pwd", databasePassword));
        new WebAPI(URL, Method.ADD_USER_WITHOUT_LOGIN, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
    }

    public static void doAddUser(Context context, User user) {
        if (httpClient == null) httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = null;
        nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("user", user.toJSONString()));
        new WebAPI(URL, Method.ADD_USER, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
    }

    public static void doUpdateUser(Context context, User user) {
        if (httpClient == null)
            httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = null;
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(user.getU_id())));
        nameValuePairs.add(new BasicNameValuePair("user", user.toJSONString()));
        new WebAPI(URL, Method.UPDATE_USER, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
    }

    public static void doChangeUserPassword(Context context, int userId, String newPassword) {
        if (httpClient == null)
            httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = null;
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        nameValuePairs.add(new BasicNameValuePair("newPassword", newPassword));
        new WebAPI(URL, Method.CHANGE_USER_PASSWORD, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
    }

    public static void doUpdateEquipment(Context context, Equipment equipment) {
        if (httpClient == null)
            httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = null;
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("equipmentId", String.valueOf(equipment.getE_id())));
        nameValuePairs.add(new BasicNameValuePair("equipment", equipment.toJSONString()));
        new WebAPI(URL, Method.UPDATE_EQUIPMENT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
    }

    public static void doRegisterReservationOut(Context context, int userId, int equipmentId, String comment) {
        if (httpClient == null)
            httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = null;
        nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        nameValuePairs.add(new BasicNameValuePair("equipmentId", String.valueOf(equipmentId)));
        nameValuePairs.add(new BasicNameValuePair("dateOut", new SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date())));
        nameValuePairs.add(new BasicNameValuePair("comment", comment));
        new WebAPI(URL, Method.REGISTER_RESERVATION_OUT, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
    }

    public static void doRegisterReservationIn(Context context, int logEntryId) {
        if (httpClient == null)
            httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = null;
        nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("le_id", String.valueOf(logEntryId)));
        nameValuePairs.add(new BasicNameValuePair("dateIn", new SimpleDateFormat("dd.MM.yyyy").format(new java.util.Date())));
        new WebAPI(URL, Method.REGISTER_RESERVATION_IN, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
    }

    public static void doGetEquipmentWithoutLogin(Context context) {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("connectstring", "jdbc:mysql://" + sql + ":" + sqlport + "/"));
            nameValuePairs.add(new BasicNameValuePair("dbName", db));
            nameValuePairs.add(new BasicNameValuePair("db_uid", databaseUsername));
            nameValuePairs.add(new BasicNameValuePair("db_pwd", databasePassword));
            new WebAPI(URL, Method.GET_EQUIPMENT_WITHOUT_LOGIN, context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
            Log.d(App.TAG, "Getting equipment");
        } else {
            Log.d(App.TAG, alreadyLoggedIn);
        }
    }
}
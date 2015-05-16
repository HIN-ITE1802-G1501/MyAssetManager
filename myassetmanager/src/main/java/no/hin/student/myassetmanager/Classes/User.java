package no.hin.student.myassetmanager.Classes;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import no.hin.student.myassetmanager.Interfaces.MyInterface;
import no.hin.student.myassetmanager.R;


/**
 * Created by wfa on 07.04.2015.
 *
 * Objekter av denne typen representerer brukere av systemet.
 * Ved registrering av ny bruker oppgis brukernavn og passord m.m.
 *
 */
public class User extends MyObjects implements MyInterface {
    private int u_id;               //PrimÃ¦rnÃ¸kkelfelt, autogenereres i databasen.
    private String userName;        //Brukernavn
    private String password;        //Passord
    private String firstname;       //Fornavn
    private String lastname;        //Etternavn
    private String phone;           //Telefonnummer (obligatorisk)
    private boolean user_activated; //Aktivert eller ikke. Settes som aktiv av admin/lÃ¦rer.


    private static HttpClient httpClient = null;

    private static final String TAG = "MyAssetManger-log";

    public User() {
        this.userName = "";
        this.password = "";
        this.firstname = "";
        this.lastname = "";
        this.phone = "";
        this.user_activated = false;
    }

    public User(int u_id, String userName, String password, String firstname, String lastname, String phone, boolean user_activated) {
        this.u_id = u_id;
        this.userName = userName;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.user_activated = user_activated;
    }

    public User(String userName, String password, String firstname, String lastname, String phone, boolean user_activated) {
        this.u_id = u_id;
        this.userName = userName;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.user_activated = user_activated;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isUser_activated() {
        return user_activated;
    }

    public void setUser_activated(boolean user_activated) {
        this.user_activated = user_activated;
    }

    public String toJSONString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public static void showUsers(MyAdapter adapterInstance) {
        adapterInstance.add(new User(1, "kekarlsen", "password", "Kurt-Erik", "Karlsen", "tlfnr123", true));
        adapterInstance.add(new User(2, "aleks", "password", "Aleksander", "Valle", "tlfnr123", true));
        adapterInstance.add(new User(3, "billy", "password", "Bill", "Gates", "tlfnr123", true));
    }

    public static void deleteUser(MyAdapter adapterInstance, User user) {
        Log.d(TAG, "Delete user from list and database");
        adapterInstance.remove(user);
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }




    @Override
    public int getId() {
        return this.u_id;
    }

    @Override
    public String getListItemTitle() {
        return this.firstname + " " + this.lastname;
    }

    @Override
    public int getListItemImage() {
        return android.R.drawable.ic_menu_report_image;
    }







    //logIn:
//Button-event, koplet til et Button-objekt definert i XML:
    public static void doLogin(Context context) {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            //Parametre:
            List<NameValuePair> nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>(6);
            nameValuePairs.add(new BasicNameValuePair("uid", "530617"));
            nameValuePairs.add(new BasicNameValuePair("pwd", "kurt"));
            //Tar også med databaseparametre her (lagres i session på serveren
            //slik at samme parametret brukes på alle forespørsler).
            //NB! Her bruker du parametre til din egen database:
            nameValuePairs.add(new BasicNameValuePair("connectstring", "jdbc:mysql://kark.hin.no:3306/"));
            nameValuePairs.add(new BasicNameValuePair("dbName", "stud_v15_karlsen"));
            nameValuePairs.add(new BasicNameValuePair("db_uid", "karlsen"));
            nameValuePairs.add(new BasicNameValuePair("db_pwd", "530617Pass"));
            //NB! Sender med referanse til seg selv til konstruktøren:
            new LogInAsyncTask(context).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            Log.d(TAG, "Du er allerede logget inn!");
        }
    }




    //getEquipment (henter alt utstyr):
    //Button-event, koplet til et Button-objekt definert i XML:
    public void doGetEquipment(View view) {
        if (httpClient != null) {
            //Parametre:
            List<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("which_equipment", "ALL"));
            //NB! Sender med referanse til seg selv til konstruktøren:
            //new GetEquipmentAsyncTask(this).execute(new Pair<List<NameValuePair>, HttpClient>(nameValuePairs, httpClient));
        } else {
            //Toast.makeText(this, "Logg inn først!!!", Toast.LENGTH_LONG).show();
        }
    }





    //Viser login-resultat:
    public void showLoginResult(String result) {
        //TextView textViewStatus = (TextView)findViewById(R.id.tvStatus);
        //textViewStatus.setText(result);
    }
}
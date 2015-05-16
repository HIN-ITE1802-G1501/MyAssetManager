package no.hin.student.myassetmanager.Classes;

import android.util.Log;
import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import no.hin.student.myassetmanager.Interfaces.MyInterface;



public class User extends MyObjects implements MyInterface {
    private int u_id;
    private String userName;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private boolean user_activated;


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


}
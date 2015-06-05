/**
 * This is the User class that represents a user object
 * @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
 * @version 1.1
 */


package no.hin.student.myassetmanager.Classes;

import android.view.View;

import com.google.gson.Gson;
import no.hin.student.myassetmanager.Interfaces.AssetManagerInterface;
import no.hin.student.myassetmanager.R;


public class User extends AssetManagerObjects implements AssetManagerInterface {

    private int u_id;                   // User ID
    private String userName;            // Username for the user
    private String password;            // Password for the user
    private String firstname;           // User's firstname
    private String lastname;            // User's lastname
    private String phone;               // User's phonenumber
    private boolean user_activated;     // Variable true if user is activated and false if not


    /**
     * Default constructor for User
     */
    public User() {
             this.userName = "";
             this.password = "";
             this.firstname = "";
             this.lastname = "";
             this.phone = "";
             this.user_activated = false;
         }

    /**
     * Constructor for User with all parameters
     *
     * @param u_id represents the User ID for the logentry
     * @param userName represents the user's username
     * @param password represents the user's password
     * @param firstname represents the user's firstname
     * @param phone represents the user's phonenumber
     * @param user_activated if the user should be activated or deactivated
     */
    public User(int u_id, String userName, String password, String firstname, String lastname, String phone, boolean user_activated) {
        this.u_id = u_id;
        this.userName = userName;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.user_activated = user_activated;
    }

    /**
     * Constructor for User without u_id
     *
     * @param userName represents the user's username
     * @param password represents the user's password
     * @param firstname represents the user's firstname
     * @param phone represents the user's phonenumber
     * @param user_activated if the user should be activated or deactivated
     */
    public User(String userName, String password, String firstname, String lastname, String phone, boolean user_activated) {
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
        return gson.toJson(this);
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
    public String getListItemSubTitle(View view) {
        return (isUser_activated() ? App.getContext().getString(R.string.CLASS_USER_STATUS_ACTIVATE) : App.getContext().getString(R.string.CLASS_USER_STATUS_DEACTIVATED));
    }

    @Override
    public int getListItemImage() {
        return (user_activated) ? R.drawable.user : R.drawable.user_notactive;
    }


}
/**
* This is the Login class contains method for Application login.
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.2
*/

package no.hin.student.myassetmanager.Classes;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Fragments.FragmentLogin;

public class Login {

    /**
     * Enum that represents the user's role, if it's an admin user or regular user.
     */
     public enum UserRole {
         ADMIN_USER(1),
         REGULAR_USER(2),
         LOGGED_OUT(9);

         private int userRoleId;

         private UserRole(int id) {
             userRoleId = id;
         }
     }

     private static User loginUser;                                 // Variable for currently logged in user

     private static String loginUsername = "";                      // Variable for currently logged in user's username
     private static String loginPassword = "";                      // Variable for currently logged in user's password
     private static Boolean loginAdmin = false;                     // Variable that represents if user tried to log in as admin

     private static UserRole loginUserRole = UserRole.LOGGED_OUT;   // Variable for currently user's role

     public static void setUsername(String username) {
         loginUsername = username;
     }

     public static String getUsername() {
         return loginUsername;
     }

     public static void setPassword(String password) {
         loginPassword = password;
     }

     public static String getPassword() {
         return loginPassword;
     }

     public static void setAdmin(Boolean admin) {
         loginAdmin = admin;
     }

     public static Boolean getAdmin() {
         return loginAdmin;
     }

     public static void setUserRole(UserRole userRole) {
         loginUserRole = userRole;
     }

     public static UserRole getUserRole() {
         return loginUserRole;
     }

     public static Boolean isLoggedOut() {
         return loginUserRole == UserRole.LOGGED_OUT ? true : false;
     }

     public static Boolean isLoggedInAsAdminUser() {
         return loginUserRole == UserRole.ADMIN_USER ? true : false;
     }

     public static Boolean isLoggedInAsRegularUser() {
         return loginUserRole == UserRole.REGULAR_USER ? true : false;
     }

     public static User getLoggedInUser() {
         return loginUser;
     }

     public static void setLoggedInUser(User user) {
         loginUser = user;
     }


    /**
     * Method for saving user's credentials to SharedPreferences
     */
     public static void saveUserLoginToApp() {
         SharedPreferences userLoginInformation = App.getContext().getSharedPreferences("MyAssetManager", App.getContext().MODE_PRIVATE);
         SharedPreferences.Editor edit = userLoginInformation.edit();
         edit.putString("username", getUsername().toString().trim());
         edit.putString("password", getPassword().toString().trim());
         edit.putBoolean("admin", getAdmin());
         edit.commit();
         Log.d(App.TAG, "Saving user " + getUsername());
     }

    /**
     * Method for deleting user's credentials in SharedPreferences
     */
    public static void deleteUserLoginToApp() {
        SharedPreferences userLoginInformation = App.getContext().getSharedPreferences("MyAssetManager", App.getContext().MODE_PRIVATE);
        SharedPreferences.Editor edit = userLoginInformation.edit();
        edit.putString("username", getUsername().toString().trim());
        edit.putString("password", "");
        edit.putBoolean("admin", getAdmin());
        edit.commit();
        Log.d(App.TAG, "Deleting password for user " + getUsername());
    }

    /**
     * Method for getting user's credentials from SharedPreferences
     */
     public static void loadUserLoginToApp() {
         SharedPreferences userLoginInformation = App.getContext().getSharedPreferences("MyAssetManager", App.getContext().MODE_PRIVATE);
         setUsername(userLoginInformation.getString("username", ""));
         setPassword(userLoginInformation.getString("password", ""));
         setAdmin(userLoginInformation.getBoolean("admin", false));
         Log.d(App.TAG, "Loading user " + getUsername());
     }


    /**
     * Method for logging in user
     *
     * @param context ActivityMain context
     * @param user represents the user that we should log in
     * @param success if login is success
     * @param userStatus user status of the login
     */
    public static void logIn(Context context, User user, boolean success, Login.UserRole userStatus) {
        ActivityMain activityMain = ((ActivityMain) context);
        if (success) {
            Log.d(App.TAG, "User logged in: " + user.getFirstname() + " " + user.getLastname());
            Login.setUserRole(userStatus);
            Login.setLoggedInUser(user);

            if (activityMain.fragmentCurrent instanceof FragmentLogin) {
                activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentList);
                activityMain.addToList(Category.getCategories());
                EquipmentStatus.getUpdateFromDatabase(activityMain);
            }
        }
    }

    public static void attemptLogin(Context context, String username, String password, boolean isAdmin) {
        if (isAdmin)
            WebAPI.doLoginAdmin(context, username, password);
        else
            WebAPI.doLogin(context, username, password);
    }
 }

package no.hin.student.myassetmanager.Classes;


import android.content.SharedPreferences;
import android.util.Log;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Fragments.FragmentLogin;

public class Login {
     public enum UserRole {
         ADMIN_USER(1),
         REGULAR_USER(2),
         LOGGED_OUT(9);

         private int userRoleId;

         private UserRole(int id) {
             userRoleId = id;
         }
     }

     private static User loginUser;

     private static String loginUsername = "";
     private static String loginPassword = "";
     private static Boolean loginAdmin = false;

     private static UserRole loginUserRole = UserRole.LOGGED_OUT;

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

     public static void saveUserLoginToApp() {
         SharedPreferences userLoginInformation = App.getContext().getSharedPreferences("MyAssetManager", App.getContext().MODE_PRIVATE);
         SharedPreferences.Editor edit = userLoginInformation.edit();
         edit.putString("username", getUsername().toString().trim());
         edit.putString("password", getPassword().toString().trim());
         edit.putBoolean("admin", getAdmin());
         edit.commit();
         Log.d(App.TAG, "Saving user " + getUsername());
     }

    public static void deleteUserLoginToApp() {
        SharedPreferences userLoginInformation = App.getContext().getSharedPreferences("MyAssetManager", App.getContext().MODE_PRIVATE);
        SharedPreferences.Editor edit = userLoginInformation.edit();
        edit.putString("username", getUsername().toString().trim());
        edit.putString("password", "");
        edit.putBoolean("admin", getAdmin());
        edit.commit();
        Log.d(App.TAG, "Deleting password for user " + getUsername());
    }

     public static void loadUserLoginToApp() {
         SharedPreferences userLoginInformation = App.getContext().getSharedPreferences("MyAssetManager", App.getContext().MODE_PRIVATE);
         setUsername(userLoginInformation.getString("username", ""));
         setPassword(userLoginInformation.getString("password", ""));
         setAdmin(userLoginInformation.getBoolean("admin", false));
         Log.d(App.TAG, "Loading user " + getUsername());
     }

    public static void logIn(User user, boolean success, Login.UserRole userStatus) {
        ActivityMain activityMain = ((ActivityMain) App.getContext());
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
        else {
            //Toast.makeText(getApplicationContext(), "Feil brukernavn og/eller passord", Toast.LENGTH_LONG).show();
        }
    }
 }

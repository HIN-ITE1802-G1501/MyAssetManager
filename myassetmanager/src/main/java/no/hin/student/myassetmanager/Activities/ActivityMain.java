/**
  * This class is the ActivityMain class that handles all app functionality within fragments
  * @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
  * @version 1.1
  */

package no.hin.student.myassetmanager.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
import no.hin.student.myassetmanager.Classes.AssetManagerObjects;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.EquipmentStatus;
import no.hin.student.myassetmanager.Classes.LogEntry;
import no.hin.student.myassetmanager.Classes.Login;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.Classes.UserLogEntries;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.Fragments.FragmentAccountSettings;
import no.hin.student.myassetmanager.Fragments.FragmentAddEquipment;
import no.hin.student.myassetmanager.Fragments.FragmentAsset;
import no.hin.student.myassetmanager.Fragments.FragmentList;
import no.hin.student.myassetmanager.Fragments.FragmentLoan;
import no.hin.student.myassetmanager.Fragments.FragmentLogin;
import no.hin.student.myassetmanager.Fragments.FragmentRegister;
import no.hin.student.myassetmanager.Fragments.FragmentUser;
import no.hin.student.myassetmanager.R;


public class ActivityMain extends Activity {

    /**
     * Enum used to represent the filter spinner on top right corner in list fragment
     */
     public enum Filter {
         FILTER_CATEGORY_ALL(R.string.FILTER_CATEGORY_ALL),
         FILTER_EQUIPMENT_AVAILABLE(R.string.FILTER_EQUIPMENT_AVAILABLE),
         FILTER_EQUIPMENT_INUSE(R.string.FILTER_EQUIPMENT_INUSE),

         FILTER_EQUIPMENT_ALL(R.string.FILTER_EQUIPMENT_ALL),
         FILTER_EQUIPMENT_AVAILABLE_BYCATEGORY(R.string.FILTER_EQUIPMENT_AVAILABLE_BYCATEGORY),
         FILTER_EQUIPMENT_INUSE_BYCATEGORY(R.string.FILTER_EQUIPMENT_INUSE_BYCATEGORY),

         FILTER_USERS_ALL(R.string.FILTER_ALL_USERS),
         FILTER_USERS_ACTIVE(R.string.FILTER_ACTIVE_USERS),
         FILTER_USERS_NOT_ACTIVE(R.string.FILTER_NOT_ACTIVE_USERS),

         FILTER_HISTORY_ALL(R.string.FILTER_HISTORY_ALL);

         private int resourceId;

         private Filter(int id) {
             resourceId = id;
         }

         public int getId() {
             return resourceId;
         }

         @Override
         public String toString() {
             return App.getContext().getString(resourceId);
         }
     }

     // MenulistID variables
     private static final int MENU_CONTEXT_LIST_SHOW = 10101;
     private static final int MENU_CONTEXT_LIST_EDIT = 10102;
     private static final int MENU_CONTEXT_LIST_DELETE = 10103;

     private static final int MENU_BUTTON_SHOW_ASSETS = 10201;
     private static final int MENU_BUTTON_SHOW_USERS = 10202;
     private static final int MENU_BUTTON_SHOW_HISTORY = 10203;
     private static final int MENU_BUTTON_SHOW_MY_PAGE = 10204;

     private static final int MENU_BUTTON_LOGIN = 10301;
     private static final int MENU_BUTTON_LOGOUT = 10302;

     private static final int MENU_BUTTON_ADD_EQUIPMENT = 10401;
     private static final int MENU_BUTTON_NEW_USER = 10402;


     // Fragments variables
     public FragmentList fragmentList;
     public FragmentUser fragmentUser;
     public FragmentAsset fragmentAsset;
     public FragmentLogin fragmentLogin;
     public FragmentRegister fragmentRegister;
     public FragmentAccountSettings fragmentAccountSettings;
     public FragmentLoan fragmentLoan;
     public FragmentAddEquipment fragmentAddEquipment;
     public Fragment fragmentCurrent = null;
     public AssetManagerAdapter adapter;

    private Equipment currentlyViewedEquipment;

    private FragmentManager fragmentManager;

     // InstanceState definitions
     static final String STATE_CURRENT_EQUIPMENT = "equipment";


     @Override
     // TODO: BUG - Fix problem when no Internet connectin or connecin to WebAPI
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         Log.d(App.TAG, "On create");

         if (savedInstanceState != null) {
             // Restore value of members from saved state
             Log.d(App.TAG, "From restoreInstanceState" + savedInstanceState.getInt(STATE_CURRENT_EQUIPMENT));
         } else {
             // Probably initialize members with default values for a new instance
             Log.d(App.TAG, "Nothing to restore from savedInstanceState");
         }

         setContentView(R.layout.activity_main);
         findViewById(R.id.btnMenu).setOnClickListener(mGlobal_OnClickListener);

         // TODO: FEATURE - Update categories list even if file exist
         File file = new File(Environment.getExternalStorageDirectory() + "/Download/products.txt");
         if (!(file.exists())) // If file doesn't exist, downloadCategoriesFile it
             Category.downloadCategoriesFile();

         // Setting the database login information
         WebAPI.setDatabaseLoginInformation(getResources().getString(R.string.db_user), getResources().getString(R.string.db_password));

         // Loads user login from SharedPreferences
         Login.loadUserLoginToApp();

         // Creating all of our fragments
         fragmentList = new FragmentList();
         fragmentUser = new FragmentUser();
         fragmentAsset = new FragmentAsset();
         fragmentLogin = new FragmentLogin();
         fragmentRegister = new FragmentRegister();
         fragmentLoan = new FragmentLoan();
         fragmentAddEquipment = new FragmentAddEquipment();
     }

     @Override
     // TODO: FEATURE - Implement Restore InstanceState for activity and fragments
     public void onRestoreInstanceState(Bundle savedInstanceState) {
         super.onRestoreInstanceState(savedInstanceState);
     }

     @Override
     // TODO: FEATURE - Implement SaveInstanceState for activity and fragments
     public void onSaveInstanceState(Bundle savedInstanceState) {
         super.onSaveInstanceState(savedInstanceState);

         savedInstanceState.putInt(STATE_CURRENT_EQUIPMENT, 2);
     }

     @Override
     public void onStart() {
         super.onStart();
         try {
             // Handling login when AcitvityMain starts/resumes
             if ( ((fragmentCurrent == null) && (Login.getUsername().equals(""))) || (Login.getPassword().equals("")) ) {
                 Log.d(App.TAG, "Not resuming, getting equipment as anonymous");
                 replaceFragmentContainerFragmentWith(fragmentList);
                 WebAPI.doGetEquipmentWithoutLogin(this);
             } else if ( (fragmentCurrent == null) && !Login.getUsername().equals("") && !Login.getPassword().equals("") ) {
                 Log.d(App.TAG, "Not resuming, trying to log in");
                 Login.attemptLogin(this, Login.getUsername(), Login.getPassword(), Login.getAdmin());
                 replaceFragmentContainerFragmentWith(fragmentList);
                 addToList(Category.getCategories());
             } else {
                 Log.d(App.TAG, "Resuming, getting equipment");
                 Login.attemptLogin(this, Login.getUsername(), Login.getPassword(), Login.getAdmin());
                 replaceFragmentContainerFragmentWith(fragmentCurrent);
             }
         } catch (Exception e) {
             Log.d(App.TAG, e.toString());
         }
     }

     @Override
     public void onStop() {
         super.onStop();

         Log.d(App.TAG, "onStop");

         // Loggin user out if logged in
         if (!Login.isLoggedOut())
             WebAPI.logOut(ActivityMain.this);
     }


     @Override
     // Create ContextMenu for application
     public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
         switch (v.getId()) {
             case R.id.lvList: // Create menu for right-click in ListView

                 super.onCreateContextMenu(menu, v, menuInfo);
                 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                 if (((AssetManagerObjects)(adapter.getItem(info.position))) instanceof Equipment) {
                     String title = ((AssetManagerObjects)(adapter.getItem(info.position))).getListItemTitle();

                     menu.setHeaderTitle(title);
                     menu.add(Menu.NONE, MENU_CONTEXT_LIST_SHOW, Menu.NONE, R.string.MENU_CONTEXT_LIST_SHOW);
                     if (Login.isLoggedInAsAdminUser()) {
                         menu.add(Menu.NONE, MENU_CONTEXT_LIST_EDIT, Menu.NONE, R.string.MENU_CONTEXT_LIST_EDIT);
                         menu.add(Menu.NONE, MENU_CONTEXT_LIST_DELETE, Menu.NONE, R.string.MENU_CONTEXT_LIST_DELETE);
                     }
                 } else if (((AssetManagerObjects)(adapter.getItem(info.position))) instanceof User) {
                     String title = ((AssetManagerObjects)(adapter.getItem(info.position))).getListItemTitle();

                     menu.setHeaderTitle(title);
                     menu.add(Menu.NONE, MENU_CONTEXT_LIST_SHOW, Menu.NONE, R.string.MENU_CONTEXT_LIST_SHOW);
                     if (Login.isLoggedInAsAdminUser()) {
                         menu.add(Menu.NONE, MENU_CONTEXT_LIST_EDIT, Menu.NONE, R.string.MENU_CONTEXT_LIST_EDIT);
                         menu.add(Menu.NONE, MENU_CONTEXT_LIST_DELETE, Menu.NONE, R.string.MENU_CONTEXT_LIST_DELETE);
                     }
                 } else if (((AssetManagerObjects)(adapter.getItem(info.position))) instanceof Category) {
                     String title = ((AssetManagerObjects)(adapter.getItem(info.position))).getListItemTitle();
                     menu.setHeaderTitle(title);
                     menu.add(Menu.NONE, MENU_CONTEXT_LIST_SHOW, Menu.NONE, R.string.MENU_CONTEXT_LIST_SHOW);
                 } else if (((AssetManagerObjects)(adapter.getItem(info.position))) instanceof UserLogEntries) {
                     String title = ((AssetManagerObjects)(adapter.getItem(info.position))).getListItemTitle();
                     menu.setHeaderTitle(title);
                     menu.add(Menu.NONE, MENU_CONTEXT_LIST_SHOW, Menu.NONE, R.string.MENU_CONTEXT_LIST_SHOW);
                 }
                 break;
         }
     }

     @Override
     // When clicking on an item in ListView
     public boolean onContextItemSelected(MenuItem item) {
         AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

         switch (item.getItemId()) {
             case MENU_CONTEXT_LIST_SHOW:
                 if (adapter.getItem(info.position).getClass().equals(Category.class) ) {
                     WebAPI.doGetEquipmentType(ActivityMain.this, ((Category) adapter.getItem(info.position)).getListItemTitle());
                     Log.d(App.TAG, "Clicking on show");
                 } else if (adapter.getItem(info.position).getClass().equals(Equipment.class) ) {
                     replaceFragmentContainerFragmentWith(fragmentAsset);
                     Equipment equipment = (Equipment)adapter.getItem(info.position);
                     Log.d(App.TAG, "Viewing " + equipment.getModel());
                     fragmentAsset.populateAssetFragmentWithAssetData(equipment);
                     currentlyViewedEquipment = equipment;
                 } else if (adapter.getItem(info.position).getClass().equals(User.class) ) {
                     replaceFragmentContainerFragmentWith(fragmentUser);
                     User user = ((User) adapter.getItem(info.position));
                     fragmentUser.populateUserFragmentWithUserData(user, ActivityMain.this);
                 } else if (adapter.getItem(info.position).getClass().equals(UserLogEntries.class) ) {
                     replaceFragmentContainerFragmentWith(fragmentUser);
                     UserLogEntries user = ((UserLogEntries) adapter.getItem(info.position));
                     fragmentUser.populateUserFragmentWithUserData(user.getUser(), ActivityMain.this);
                 }
                 return true;
             case MENU_CONTEXT_LIST_DELETE:
                 if ((adapter != null) &&(adapter.getItem(info.position).getClass().equals(User.class))) {
                     Log.d(App.TAG, "Menu context delete category");
                     WebAPI.doDeleteUser(ActivityMain.this, ((User) adapter.getItem(info.position)).getId());
                 }
                 return true;
             default:
                 return super.onContextItemSelected(item);
         }
     }








    /**
     * Method for notifying user with a Toast
     *
     * @param fragment the fragment we should replace with in the container
     */
     public void replaceFragmentContainerFragmentWith(Fragment fragment) {
         fragmentManager = getFragmentManager();
         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
         fragmentTransaction.replace(R.id.fragment_container, fragment);
         fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
         fragmentTransaction.commit();
         fragmentManager.executePendingTransactions();
         fragmentCurrent = fragment;
     }

    /**
     * Handles back navigation
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        fragmentCurrent = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragmentCurrent instanceof FragmentList) {
            if (Login.getUserRole() == Login.UserRole.LOGGED_OUT)
                WebAPI.doGetEquipmentWithoutLogin(this);
            else
                addToList(Category.getCategories());
        }
    }




     // Multiusage onClickListener delegate
     final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
         public void onClick(final View v) {
             switch(v.getId()) {
                 case R.id.btnMenu: // Populate the mainmenu button in the top right corner
                     PopupMenu popup = new PopupMenu(getApplication(), v);

                     if (Login.isLoggedInAsAdminUser()) {
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_ASSETS, Menu.NONE, R.string.MENU_BUTTON_SHOW_ASSETS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_USERS, Menu.NONE, R.string.MENU_BUTTON_SHOW_USERS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_HISTORY, Menu.NONE, R.string.MENU_BUTTON_SHOW_HISTORY).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_MY_PAGE, Menu.NONE, R.string.MENU_BUTTON_SHOW_MY_PAGE).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_ADD_EQUIPMENT, Menu.NONE, R.string.MENU_BUTTON_ADD_EQUIPMENT).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_NEW_USER, Menu.NONE, R.string.MENU_BUTTON_NEW_USER).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_LOGOUT, Menu.NONE, R.string.MENU_BUTTON_LOGOUT).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.show();
                     } else if (Login.isLoggedInAsRegularUser()) {
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_ASSETS, Menu.NONE, R.string.MENU_BUTTON_SHOW_ASSETS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_MY_PAGE, Menu.NONE, R.string.MENU_BUTTON_SHOW_MY_PAGE).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_LOGOUT, Menu.NONE, R.string.MENU_BUTTON_LOGOUT).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.show();
                     } else if (Login.isLoggedOut()) {
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_ASSETS, Menu.NONE, R.string.MENU_BUTTON_SHOW_ASSETS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.getMenu().add(Menu.NONE, MENU_BUTTON_LOGIN, Menu.NONE, R.string.MENU_BUTTON_LOGIN).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                         popup.show();
                     }
                     break;
             }
         }
     };




     // OnMenuItemClickListener for top right corner menu in AcitvityMain
     final MenuItem.OnMenuItemClickListener mGlobal_OnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem menuItem) {
             switch (menuItem.getItemId()) {
                 case MENU_BUTTON_SHOW_ASSETS:
                     Log.d(App.TAG, "Showing assets");
                     if (Login.isLoggedOut())
                         WebAPI.doGetEquipmentWithoutLogin(ActivityMain.this);
                     else addToList(Category.getCategories());
                     return true;
                 case MENU_BUTTON_SHOW_USERS:
                     Log.d(App.TAG, "Showing users");
                     WebAPI.doGetUsers(ActivityMain.this, WebAPI.Method.GET_USERS);
                     return true;
                 case MENU_BUTTON_SHOW_HISTORY:
                     WebAPI.doGetAllLogEntriesForAllUser(ActivityMain.this);
                     return true;
                 case MENU_BUTTON_SHOW_MY_PAGE:
                     fragmentAccountSettings = new FragmentAccountSettings();
                     replaceFragmentContainerFragmentWith(fragmentAccountSettings);
                     return true;
                 case MENU_BUTTON_LOGOUT:
                     WebAPI.logOut(ActivityMain.this);
                     return true;
                 case MENU_BUTTON_LOGIN:
                     replaceFragmentContainerFragmentWith(fragmentLogin);
                     return true;
                 case MENU_BUTTON_ADD_EQUIPMENT:
                     replaceFragmentContainerFragmentWith(fragmentAddEquipment);
                     fragmentAddEquipment.newEquipment();
                     return true;
                 case MENU_BUTTON_NEW_USER:
                     replaceFragmentContainerFragmentWith(fragmentRegister);
                     fragmentRegister.populateNew();
                     return true;
                 default:
                     return true;
             }
         }
     };





     // OnItemClickListener when clicking on item in ListView in fragment List
     final AdapterView.OnItemClickListener mGlobal_OnItemClickListener = new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView<?> adapterView, View row, int position, long index) {
             Log.d(App.TAG, "Clicking on equipment " + adapter.getItem(position).toString());

             boolean clickedCategory = adapter.getItem(position).getClass().equals(Category.class);
             boolean clickedEquipment = adapter.getItem(position).getClass().equals(Equipment.class);
             boolean clickedUser = adapter.getItem(position).getClass().equals(User.class);
             boolean clickedUserLogEntries = adapter.getItem(position).getClass().equals(UserLogEntries.class);

             if (clickedCategory) {
                 Log.d(App.TAG, ((Category) adapter.getItem(position)).getListItemTitle());
                 WebAPI.doGetEquipmentType(ActivityMain.this, ((Category) adapter.getItem(position)).getListItemTitle());
             }
             else if (clickedEquipment) {
                 replaceFragmentContainerFragmentWith(fragmentAsset);
                 Equipment equipment = (Equipment)adapter.getItem(position);
                 fragmentAsset.populateAssetFragmentWithAssetData(equipment);
                 currentlyViewedEquipment = equipment;
             }
             else if (clickedUser) {
                 replaceFragmentContainerFragmentWith(fragmentUser);
                 User user = ((User) adapter.getItem(position));
                 fragmentUser.populateUserFragmentWithUserData(user, ActivityMain.this);
             } if ( clickedUserLogEntries ) {
                 replaceFragmentContainerFragmentWith(fragmentUser);
                 UserLogEntries user = ((UserLogEntries) adapter.getItem(position));
                 fragmentUser.populateUserFragmentWithUserData(user.getUser(), ActivityMain.this);
             }
         }
     };


    /**
     * Method for adding objects to Listview
     *
     * @param objects to add to listview
     */
     public void addToList(final ArrayList<? extends AssetManagerObjects> objects) {
         addToList(objects, true);
     }

    /**
     * Method for adding objects to Listview with option to updatespinner
     *
     * @param objects to add to listview
     * @param updateSpinner to add to listview
     */
    // TODO: IMPROVEMENT - Move method to FragmentList class
    public void addToList(final ArrayList<? extends AssetManagerObjects> objects, Boolean updateSpinner) {
         try {
             replaceFragmentContainerFragmentWith(fragmentList);
             ListView lvList = (ListView) fragmentList.getView().findViewById(R.id.lvList);
             TextView tvTitle = ((TextView) fragmentList.getView().findViewById(R.id.tvTitle));
             if (objects.size() > 0) {
                 Spinner spFilter = (Spinner)findViewById(R.id.spFilter);
                 ArrayList<Filter> spinnerArray = new ArrayList<Filter>();

                 // TODO: BUG - Problem when switching between category and available/in use in spinner, the spinner selected item is not always retained correctly
                 int spPos = 0;
                 Log.d(App.TAG, "Before counting");
                 if (lvList.getCount() > 0) {
                     Log.d(App.TAG, "Before checking objects");
                     if (objects.get(0).getClass().equals(lvList.getItemAtPosition(0).getClass()) ) {
                         spPos = spFilter.getSelectedItemPosition();
                         Log.d(App.TAG, "Settings spos" + Integer.toString(spPos));
                     }
                 }

                 if (updateSpinner == false) {
                     spinnerArray.add(Filter.FILTER_CATEGORY_ALL);
                     spinnerArray.add(Filter.FILTER_EQUIPMENT_AVAILABLE);
                     spinnerArray.add(Filter.FILTER_EQUIPMENT_INUSE);
                     tvTitle.setText(R.string.FRAGMENT_LIST_TITLE_EQUIPMENT);
                 } else if (objects.get(0) instanceof Equipment) {
                     if (Login.isLoggedOut()) {
                         spinnerArray.add(Filter.FILTER_EQUIPMENT_ALL);
                         tvTitle.setText(R.string.FRAGMENT_LIST_TITLE_EQUIPMENT);
                     } else {
                         spinnerArray.add(Filter.FILTER_EQUIPMENT_ALL);
                         spinnerArray.add(Filter.FILTER_EQUIPMENT_AVAILABLE_BYCATEGORY);
                         spinnerArray.add(Filter.FILTER_EQUIPMENT_INUSE_BYCATEGORY);
                         tvTitle.setText(((Equipment) objects.get(0)).getType());
                     }
                 } else if (objects.get(0) instanceof Category) {
                     spinnerArray.add(Filter.FILTER_CATEGORY_ALL);
                     spinnerArray.add(Filter.FILTER_EQUIPMENT_AVAILABLE);
                     spinnerArray.add(Filter.FILTER_EQUIPMENT_INUSE);
                     tvTitle.setText(R.string.FRAGMENT_LIST_TITLE_EQUIPMENT);
                 } else if (objects.get(0) instanceof User) {
                     spinnerArray.add(Filter.FILTER_USERS_ALL);
                     spinnerArray.add(Filter.FILTER_USERS_ACTIVE);
                     spinnerArray.add(Filter.FILTER_USERS_NOT_ACTIVE);
                     tvTitle.setText(R.string.FRAGMENT_LIST_TITLE_USERS);
                 } else if (objects.get(0) instanceof UserLogEntries) {
                     spinnerArray.add(Filter.FILTER_HISTORY_ALL);
                     tvTitle.setText(R.string.FRAGMENT_LIST_TITLE_HISTORY);
                 }
                 ArrayAdapter<Filter> adapterInstance;
                 adapterInstance = new ArrayAdapter<Filter>(this, android.R.layout.simple_list_item_1, spinnerArray);
                 spFilter.setAdapter(adapterInstance);
                 spFilter.setSelection(spPos);
                 spFilter.setTag(R.id.pos, spPos);
                 spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                         Spinner spFilter = (Spinner)findViewById(R.id.spFilter);
                         Log.d(App.TAG, "Position " + Integer.toString(position));


                         if ((Integer)spFilter.getTag(R.id.pos) != position) {
                             TextView tvttt = (TextView)findViewById(R.id.tvTitle);
                             Object selectedItem = parent.getItemAtPosition(position);
                             if (selectedItem.equals(Filter.FILTER_USERS_ALL)) {
                                 WebAPI.doGetUsers(view.getContext(), WebAPI.Method.GET_USERS);
                             } else if (selectedItem.equals(Filter.FILTER_USERS_ACTIVE)) {
                                 WebAPI.doGetUsers(view.getContext(), WebAPI.Method.GET_ACTIVE_USERS);
                             } else  if (selectedItem.equals(Filter.FILTER_USERS_NOT_ACTIVE)) {
                                 WebAPI.doGetUsers(view.getContext(), WebAPI.Method.GET_NOT_ACTIVED_USERS);
                             } else  if (selectedItem.equals(Filter.FILTER_EQUIPMENT_ALL)) {
                                 WebAPI.doGetEquipmentType(view.getContext(), tvttt.getText().toString());
                             } else  if (selectedItem.equals(Filter.FILTER_EQUIPMENT_AVAILABLE_BYCATEGORY)) {
                                 addToList((ArrayList<AssetManagerObjects>)EquipmentStatus.getAvailableEquipment(tvttt.getText().toString()).clone());
                             } else  if (selectedItem.equals(Filter.FILTER_EQUIPMENT_INUSE_BYCATEGORY)) {
                                 addToList((ArrayList<AssetManagerObjects>)EquipmentStatus.getInUseEquipment(tvttt.getText().toString()).clone());
                             } else  if (selectedItem.equals(Filter.FILTER_CATEGORY_ALL)) {
                                 addToList(Category.getCategories(), false);
                             } else  if (selectedItem.equals(Filter.FILTER_EQUIPMENT_AVAILABLE)) {
                                 addToList((ArrayList<AssetManagerObjects>)EquipmentStatus.getAvailableEquipment().clone(), false);
                             } else  if (selectedItem.equals(Filter.FILTER_EQUIPMENT_INUSE)) {
                                 addToList((ArrayList<AssetManagerObjects>)EquipmentStatus.getInUseEquipment().clone(), false);
                             }
                         }
                     }

                     public void onNothingSelected(AdapterView<?> parent) {

                     }
                 });
             } else {
                 App.notifyUser(R.string.FRAGMENT_LIST_NOTIFY_NOEQUIPMENT);
             }
             adapter = new AssetManagerAdapter(this, objects);
             lvList.setAdapter(adapter);
             lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
             registerForContextMenu(lvList);
         } catch (Exception e) {
             Log.d(App.TAG, e.toString());
         }
     }


     // TODO: IMPROVEMENT - Move method to FragmentLoan class
    /**
     * Method for populating the FragmentLoan with date
     *
     * @param users list of users we need to populate with
     */
     public void populateLoanListViewWithUsers(ArrayList<User> users) {
         ListView listViewLoan = (ListView)fragmentLoan.getView().findViewById(R.id.listViewLoanUsers);
         adapter = new AssetManagerAdapter(this, users);
         listViewLoan.setAdapter(adapter);

         listViewLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 final User clickedUser = (User)adapter.getItem(position);
                 final String comment = ((EditText)fragmentLoan.getView().findViewById(R.id.editTextLoanComment)).getText().toString();

                 AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityMain.this);
                 alertDialog.setTitle(getString(R.string.DIALOG_FRAGMENT_LOAN_OUT_TITLE));
                 alertDialog.setMessage(String.format(getString(R.string.DIALOG_FRAGMENT_LOAN_OUT_MESSAGE), clickedUser.getFirstname(), currentlyViewedEquipment.getModel()));

                 alertDialog.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener()
                 {
                     @Override
                     public void onClick(DialogInterface dialog, int which)
                     {
                         WebAPI.doRegisterReservationOut(ActivityMain.this, clickedUser.getU_id(), currentlyViewedEquipment.getE_id(), comment);
                         replaceFragmentContainerFragmentWith(fragmentList);
                         addToList(Category.getCategories());
                         EquipmentStatus.getUpdateFromDatabase(ActivityMain.this);
                     }
                 });

                 alertDialog.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which)
                     {

                     }
                 });

                 alertDialog.show();
             }
         });
     }


    // TODO: IMPROVEMENT - Move method to FragmentUser class
    /**
     * Method for populating the user history list
     *
     * @param logEntries list of logentries for user
     */
     public void populateUserListViewWithUsers(ArrayList<LogEntry> logEntries) {
         Log.d(App.TAG, "Populating listview with userlogentries");
         ListView lvUserHistory = (ListView)fragmentUser.getView().findViewById(R.id.lvUserHistory);
         adapter = new AssetManagerAdapter(this, logEntries);
         lvUserHistory.setAdapter(adapter);

         lvUserHistory.setOnItemClickListener(new AdapterView.OnItemClickListener()
         {
             @Override
             public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                 replaceFragmentContainerFragmentWith(fragmentAsset);
                 LogEntry logEntry = (LogEntry)adapter.getItemAtPosition(position);
                 Equipment equipment = EquipmentStatus.getEquipmentById(logEntry.getE_id());
                 fragmentAsset.populateAssetFragmentWithAssetData(equipment);
                 currentlyViewedEquipment = equipment;
             }
         });
     }

     public Equipment getCurrentlyViewedEquipment() {
         return this.currentlyViewedEquipment;
     }
 }
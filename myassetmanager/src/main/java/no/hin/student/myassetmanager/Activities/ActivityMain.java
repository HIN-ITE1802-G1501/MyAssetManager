package no.hin.student.myassetmanager.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
import no.hin.student.myassetmanager.Classes.AssetManagerObjects;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.EquipmentStatus;
import no.hin.student.myassetmanager.Classes.LogEntry;
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

    public static final int IS_ADMIN_USER = 1;
    public static final int IS_REGULAR_USER = 2;
    public static final int IS_LOGGED_OUT = 0;

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

    private static final String TAG = "MyAssetManger-log";

    private FragmentList fragmentList;
    private FragmentUser fragmentUser;
    private FragmentAsset fragmentAsset;
    private FragmentLogin fragmentLogin;
    private FragmentRegister fragmentRegister;
    private FragmentAccountSettings fragmentAccountSettings;
    private FragmentLoan fragmentLoan;
    private FragmentAddEquipment fragmentAddEquipment;
    private AssetManagerAdapter adapter;

    private int loggedInUserStatus = IS_LOGGED_OUT;
    private User user;
    private Equipment currentlyViewedEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnMenu).setOnClickListener(mGlobal_OnClickListener);

        File file = new File(Environment.getExternalStorageDirectory() + "/Download/products.txt");
        if (!(file.exists())) // If file doesn't exist, downloadCategoriesFile it
            Category.downloadCategoriesFile();

        WebAPI.setDatabaseLoginInformation(
                getResources().getString(R.string.db_user),
                getResources().getString(R.string.db_password)
        );

        fragmentList = new FragmentList();
        fragmentUser = new FragmentUser();
        fragmentAsset = new FragmentAsset();
        fragmentLogin = new FragmentLogin();
        fragmentRegister = new FragmentRegister();
        fragmentAddEquipment = new FragmentAddEquipment();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        //savedInstanceState.putInt(STATE_SCORE, mCurrentScore);
        //savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        replaceFragmentContainerFragmentWith(fragmentList);
        WebAPI.doGetEquipmentWithoutLogin(this);
    }

    private void replaceFragmentContainerFragmentWith(Fragment fragment) {
        TableLayout topMenu = (TableLayout)this.findViewById(R.id.tableLayoutTopMenu);

        //if (fragment instanceof FragmentLogin || fragment instanceof FragmentRegister)
        //    topMenu.setVisibility(View.GONE);
        //else
        //    topMenu.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loggedInUserStatus != IS_LOGGED_OUT)
            WebAPI.logOut(ActivityMain.this);
    }

    // When clicking show view button
    final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btnMenu:
                    PopupMenu popup = new PopupMenu(getApplication(), v);

                    if (loggedInUserStatus == IS_ADMIN_USER)
                        showAdminMenu(popup);
                    else if (loggedInUserStatus == IS_REGULAR_USER)
                        showRegularUserMenu(popup);
                    else if (loggedInUserStatus == IS_LOGGED_OUT)
                        showAnonymousUserMenu(popup);
                    break;
            }
        }
    };

    private void showAdminMenu(PopupMenu popup) {
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_ASSETS, Menu.NONE, R.string.MENU_BUTTON_SHOW_ASSETS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_USERS, Menu.NONE, R.string.MENU_BUTTON_SHOW_USERS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_HISTORY, Menu.NONE, R.string.MENU_BUTTON_SHOW_HISTORY).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_MY_PAGE, Menu.NONE, R.string.MENU_BUTTON_SHOW_MY_PAGE).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_ADD_EQUIPMENT, Menu.NONE, R.string.MENU_BUTTON_ADD_EQUIPMENT).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_LOGOUT, Menu.NONE, R.string.MENU_BUTTON_LOGOUT).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.show();
    }

    private void showRegularUserMenu(PopupMenu popup) {
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_ASSETS, Menu.NONE, R.string.MENU_BUTTON_SHOW_ASSETS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_MY_PAGE, Menu.NONE, R.string.MENU_BUTTON_SHOW_MY_PAGE).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_LOGOUT, Menu.NONE, R.string.MENU_BUTTON_LOGOUT).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.show();
    }

    private void showAnonymousUserMenu(PopupMenu popup) {
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_ASSETS, Menu.NONE, R.string.MENU_BUTTON_SHOW_ASSETS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.getMenu().add(Menu.NONE, MENU_BUTTON_LOGIN, Menu.NONE, R.string.MENU_BUTTON_LOGIN).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
        popup.show();
    }


    // Clicking on hamburger button
    final MenuItem.OnMenuItemClickListener mGlobal_OnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            replaceFragmentContainerFragmentWith(fragmentList);

            switch (menuItem.getItemId()) {
                case MENU_BUTTON_SHOW_ASSETS:
                    Log.d(TAG, "Showing assets");
                    if (loggedInUserStatus == IS_LOGGED_OUT)
                        WebAPI.doGetEquipmentWithoutLogin(ActivityMain.this);
                    else addToList(Category.getCategories());
                    return true;
                case MENU_BUTTON_SHOW_USERS:
                    Log.d(TAG, "Showing users");
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
                    fragmentAddEquipment.populateListViewWithCategories(ActivityMain.this);
                default:
                    return true;
            }
        }
    };





    // When clicking on a listview item
    final AdapterView.OnItemClickListener mGlobal_OnItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View row, int position, long index) {
            Log.d(TAG, "Clicking on equipment " + adapter.getItem(position).toString());

            boolean clickedCategory = adapter.getItem(position).getClass().equals(Category.class);
            boolean clickedEquipment = adapter.getItem(position).getClass().equals(Equipment.class);
            boolean clickedUser = adapter.getItem(position).getClass().equals(User.class);
            boolean clickedUserLogEntries = adapter.getItem(position).getClass().equals(UserLogEntries.class);

            if (clickedCategory) {
                Log.d(TAG, ((Category) adapter.getItem(position)).getListItemTitle());
                WebAPI.doGetEquipmentType(ActivityMain.this, ((Category) adapter.getItem(position)).getListItemTitle());
            }
            else if (clickedEquipment) {
                replaceFragmentContainerFragmentWith(fragmentAsset);
                Equipment equipment = (Equipment)adapter.getItem(position);
                fragmentAsset.populateAssetFragmentWithAssetData(equipment, loggedInUserStatus);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.lvList:

                super.onCreateContextMenu(menu, v, menuInfo);
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                String title = ((AssetManagerObjects)(adapter.getItem(info.position))).getListItemTitle();

                menu.setHeaderTitle(title);
                menu.add(Menu.NONE, MENU_CONTEXT_LIST_SHOW, Menu.NONE, R.string.MENU_CONTEXT_LIST_SHOW);
                menu.add(Menu.NONE, MENU_CONTEXT_LIST_EDIT, Menu.NONE, R.string.MENU_CONTEXT_LIST_EDIT);
                menu.add(Menu.NONE, MENU_CONTEXT_LIST_DELETE, Menu.NONE, R.string.MENU_CONTEXT_LIST_DELETE);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case MENU_CONTEXT_LIST_SHOW:
                if (adapter.getItem(info.position).getClass().equals(Category.class) ) {
                    WebAPI.doGetEquipmentType(ActivityMain.this, ((Category) adapter.getItem(info.position)).getListItemTitle());
                    Log.d(TAG, "Menu context show category!");
                }
                return true;
            case MENU_CONTEXT_LIST_DELETE:
                if ((adapter != null) &&(adapter.getItem(info.position).getClass().equals(Category.class))) {
                    Log.d(TAG, "Menu context delete category");

                }
                if ((adapter != null) &&(adapter.getItem(info.position).getClass().equals(User.class))) {
                    Log.d(TAG, "Menu context delete category");
                    WebAPI.doDeleteUser(ActivityMain.this, ((User) adapter.getItem(info.position)).getId());
                }


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void logIn(User user, boolean success, int userStatus) {
        if (success) {
            Log.d(TAG, "User logged in: " + user.getFirstname() + " " + user.getLastname());
            loggedInUserStatus = userStatus;
            this.user = user;
            replaceFragmentContainerFragmentWith(fragmentList);
            addToList(Category.getCategories());
            EquipmentStatus.getUpdateFromDatabase(this);
        }
        else {
            Toast.makeText(getApplicationContext(), "Feil brukernavn og/eller passord", Toast.LENGTH_LONG).show();
        }
    }

    public void logOut() {
        loggedInUserStatus = IS_LOGGED_OUT;
        replaceFragmentContainerFragmentWith(fragmentList);
        WebAPI.doGetEquipmentWithoutLogin(ActivityMain.this);
    }

    public void deleteUser() {
        WebAPI.doGetUsers(ActivityMain.this, WebAPI.Method.GET_USERS);
    }

    public void addToList(final ArrayList<AssetManagerObjects> objects) {
        addToList(objects, true);
    }

    public void addToList(final ArrayList<AssetManagerObjects> objects, Boolean updateSpinner) {
        try {
            ListView lvList = (ListView) fragmentList.getView().findViewById(R.id.lvList);
            TextView tvTitle = ((TextView) fragmentList.getView().findViewById(R.id.tvTitle));
            if (objects.size() > 0) {
                Spinner spFilter = (Spinner)findViewById(R.id.spFilter);
                ArrayList<Filter> spinnerArray = new ArrayList<Filter>();

                int spPos = 0;
                Log.d(TAG, "Before counting");
                if (lvList.getCount() > 0) {
                    Log.d(TAG, "Before checking objects");
                    if (objects.get(0).getClass().equals(lvList.getItemAtPosition(0).getClass()) ) {
                        spPos = spFilter.getSelectedItemPosition();
                        Log.d(TAG, "Settings spos" + Integer.toString(spPos));
                    }
                }

                if (updateSpinner == false) {
                    spinnerArray.add(Filter.FILTER_CATEGORY_ALL);
                    spinnerArray.add(Filter.FILTER_EQUIPMENT_AVAILABLE);
                    spinnerArray.add(Filter.FILTER_EQUIPMENT_INUSE);
                    tvTitle.setText("Utstyr");
                } else if (objects.get(0) instanceof Equipment) {
                    if (loggedInUserStatus == IS_LOGGED_OUT) {
                        spinnerArray.add(Filter.FILTER_EQUIPMENT_ALL);
                        tvTitle.setText("Utstyr");
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
                    tvTitle.setText("Utstyr");
                } else if (objects.get(0) instanceof User) {
                    spinnerArray.add(Filter.FILTER_USERS_ALL);
                    spinnerArray.add(Filter.FILTER_USERS_ACTIVE);
                    spinnerArray.add(Filter.FILTER_USERS_NOT_ACTIVE);
                    tvTitle.setText("Brukere");
                } else if (objects.get(0) instanceof UserLogEntries) {
                    spinnerArray.add(Filter.FILTER_HISTORY_ALL);
                    tvTitle.setText("Historie");
                }
                ArrayAdapter<Filter> adapterInstance;
                adapterInstance = new ArrayAdapter<Filter>(this, android.R.layout.simple_list_item_1, spinnerArray);
                spFilter.setAdapter(adapterInstance);
                spFilter.setSelection(spPos);
                spFilter.setTag(R.id.pos, spPos);
                spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner spFilter = (Spinner)findViewById(R.id.spFilter);
                        Log.d(TAG, "Position " + Integer.toString(position));


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
                Toast.makeText(this.getApplicationContext(), "Det finnes desverre ikke noe utstyr i denne kategorien.", Toast.LENGTH_SHORT).show();
            }
            adapter = new AssetManagerAdapter(this, objects);
            lvList.setAdapter(adapter);
            lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
            registerForContextMenu(lvList);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    public void onClickRegisterPageButton(View buttonView) {
        replaceFragmentContainerFragmentWith(fragmentRegister);
    }

    public void onClickSendRegistrationButton(View buttonView) {
        String firstname = ((EditText)fragmentRegister.getView().findViewById(R.id.editTextRegisterFirstname)).getText().toString();
        String lastname = ((EditText)fragmentRegister.getView().findViewById(R.id.editTextRegisterLastname)).getText().toString();
        String phoneNumber = ((EditText)fragmentRegister.getView().findViewById(R.id.editTextRegisterPhonenumber)).getText().toString();
        String username = ((EditText)fragmentRegister.getView().findViewById(R.id.editTextRegisterUsername)).getText().toString();
        String password = ((EditText)fragmentRegister.getView().findViewById(R.id.editTextRegisterPassword)).getText().toString();

        User user = new User(username, password, firstname, lastname, phoneNumber, false);
        WebAPI.doAddUserWithoutLogin(this, user);
        replaceFragmentContainerFragmentWith(fragmentLogin);
    }

    public void onClickUpdateUserInfoButton(View buttonView) {
        String firstname = ((EditText)fragmentAccountSettings.getView().findViewById(R.id.editTextSettingsFirstname)).getText().toString();
        String lastname = ((EditText)fragmentAccountSettings.getView().findViewById(R.id.editTextSettingsLastname)).getText().toString();
        String phoneNumber = ((EditText)fragmentAccountSettings.getView().findViewById(R.id.editTextSettingsPhonenumber)).getText().toString();
        String username = ((EditText)fragmentAccountSettings.getView().findViewById(R.id.editTextSettingsUsername)).getText().toString();

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhone(phoneNumber);
        user.setUserName(username);

        WebAPI.doUpdateUser(this, user);
    }

    public void onClickUpdateUserPasswordButton(View buttonView) {
        String password = ((EditText)fragmentAccountSettings.getView().findViewById(R.id.editTextSettingsNewPassword)).getText().toString();
        String repeatedPassword = ((EditText)fragmentAccountSettings.getView().findViewById(R.id.editTextSettingsRepeatPassword)).getText().toString();

        if (!password.equals(repeatedPassword))
            Toast.makeText(this, "Passord matchet ikke hverandre. Prøv på nytt", Toast.LENGTH_LONG).show();
        else
            WebAPI.doChangeUserPassword(this, user.getU_id(), password);
    }

    public void onClickLoanButton(View buttonView) {
        Button buttonLoan = (Button)buttonView;
        String buttonText = buttonLoan.getText().toString();

        if (buttonText.equals("Registrer utlån")) {
            fragmentLoan = new FragmentLoan();
            replaceFragmentContainerFragmentWith(fragmentLoan);

            if (currentlyViewedEquipment != null)
                fragmentLoan.populateLoanFragmentWithData(currentlyViewedEquipment, this);
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityMain.this);
            alertDialog.setTitle("Registrer innlevering");
            alertDialog.setMessage("Registrer innlevering for utstyr " + currentlyViewedEquipment.getModel() + "?");

            alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WebAPI.doGetOpenLogEntries(ActivityMain.this, WebAPI.Method.GET_OPEN_LOG_ENTRIES_FOR_REGISTER_RESERVATION_IN, user.getU_id());
                    replaceFragmentContainerFragmentWith(fragmentList);
                    addToList(Category.getCategories());
                }
            });

            alertDialog.setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
        }
    }

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
                alertDialog.setTitle("Registrer lån");
                alertDialog.setMessage("Vil du registrere et lån for bruker " + clickedUser.getFirstname() + " og utstyr " + currentlyViewedEquipment.getModel() + "?");

                alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener()
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

                alertDialog.setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                alertDialog.show();
            }
        });
    }

    public void populateUserListViewWithUsers(ArrayList<LogEntry> logEntries) {
        Log.d(TAG, "Populating listview with userlogentries");
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
                fragmentAsset.populateAssetFragmentWithAssetData(equipment, loggedInUserStatus);
                currentlyViewedEquipment = equipment;
            }
        });
    }

    public void onClickLoginButton(View buttonView)
    {
        String username = ((EditText)fragmentLogin.getView().findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText)fragmentLogin.getView().findViewById(R.id.editTextPassword)).getText().toString();
        boolean isAdmin = ((CheckBox)fragmentLogin.getView().findViewById(R.id.checkBoxIsAdmin)).isChecked();

        attemptLogin(username, password, isAdmin);
    }

    public void onClickSendUserSMS(View buttonView) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", fragmentUser.getUser().getPhone());
        smsIntent.putExtra("sms_body","Dette er en test");
        startActivity(smsIntent);
    }

    public void onClickCallUserPhone(View buttonView) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + fragmentUser.getUser().getPhone()));
        startActivity(intent);
    }

    public void onClickActivateUser(View buttonView) {

    }

    private void attemptLogin(String username, String password, boolean isAdmin) {
        if (isAdmin)
            WebAPI.doLoginAdmin(this, username, password);
        else
            WebAPI.doLogin(this, username, password);
    }

    public User getCurrentUser() {
        return user;
    }

    public Equipment getCurrentlyViewedEquipment() {
        return currentlyViewedEquipment;
    }

    public int getCurrentUserStatus() {
        return loggedInUserStatus;
    }

    public void sendToFragmentList() {
        replaceFragmentContainerFragmentWith(fragmentList);
        addToList(Category.getCategories());
    }
}
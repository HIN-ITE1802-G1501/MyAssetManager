package no.hin.student.myassetmanager.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
import no.hin.student.myassetmanager.Classes.AssetManagerObjects;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.Fragments.FragmentList;
import no.hin.student.myassetmanager.Fragments.FragmentLogin;
import no.hin.student.myassetmanager.Fragments.FragmentUser;
import no.hin.student.myassetmanager.R;


public class ActivityMain extends Activity {

    private static final int MENU_CONTEXT_LIST_SHOW = 10101;
    private static final int MENU_CONTEXT_LIST_EDIT = 10102;
    private static final int MENU_CONTEXT_LIST_DELETE = 10103;

    private static final int MENU_BUTTON_SHOW_ASSETS = 10201;
    private static final int MENU_BUTTON_SHOW_USERS = 10202;
    private static final int MENU_BUTTON_SHOW_HISTORY = 10203;

    private static final int MENU_BUTTON_LOGIN = 10301;
    private static final int MENU_BUTTON_LOGOUT = 10302;

    private static final String TAG = "MyAssetManger-log";

    private FragmentList fragmentList;
    private FragmentUser fragmentUser;
    private FragmentLogin fragmentLogin;
    private AssetManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnMenu).setOnClickListener(mGlobal_OnClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();

        WebAPI.setDatabaseLoginInformation(
                getResources().getString(R.string.db_user),
                getResources().getString(R.string.db_password)
        );

        fragmentList = new FragmentList();
        fragmentUser = new FragmentUser();
        fragmentLogin = new FragmentLogin();

        replaceFragmentContainerFragmentWith(fragmentLogin);
    }

    private void replaceFragmentContainerFragmentWith(Fragment fragment) {
        TableLayout topMenu = (TableLayout)this.findViewById(R.id.tableLayoutTopMenu);

        if (fragment instanceof FragmentLogin)
            topMenu.setVisibility(View.INVISIBLE);
        else
            topMenu.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    private void populateListViewWithCategories() {
        ListView lvList = (ListView)fragmentList.getView().findViewById(R.id.lvList);
        ((TextView)fragmentList.getView().findViewById(R.id.tvTitle)).setText("Kategori");
        adapter = new AssetManagerAdapter(this, Category.getCategories());
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
        registerForContextMenu(lvList);
    }

    @Override
    public void onStop() {
        WebAPI.logOut(ActivityMain.this);
        super.onStop();
    }

    // When clicking show view button
    final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.btnMenu:
                    PopupMenu popup = new PopupMenu(getApplication(), v);
                    popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_ASSETS, Menu.NONE, R.string.MENU_BUTTON_SHOW_ASSETS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                    popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_USERS, Menu.NONE, R.string.MENU_BUTTON_SHOW_USERS).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                    popup.getMenu().add(Menu.NONE, MENU_BUTTON_SHOW_HISTORY, Menu.NONE, R.string.MENU_BUTTON_SHOW_HISTORY).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                    popup.getMenu().add(Menu.NONE, MENU_BUTTON_LOGOUT, Menu.NONE, R.string.MENU_BUTTON_LOGOUT).setOnMenuItemClickListener(mGlobal_OnMenuItemClickListener);
                    popup.show();
                    Log.d(TAG, "Adding menu to button.");
                    break;
            }
        }
    };



    final MenuItem.OnMenuItemClickListener mGlobal_OnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            replaceFragmentContainerFragmentWith(fragmentList);

            switch (menuItem.getItemId()) {
                case MENU_BUTTON_SHOW_ASSETS:
                    Log.d(TAG, "Showing assets");
                    populateListViewWithCategories();
                    //initializeFilterSpinner();
                    return true;
                case MENU_BUTTON_SHOW_USERS:
                    Log.d(TAG, "Showing users");
                    WebAPI.doGetUsers(ActivityMain.this);
                    //initializeFilterSpinner();
                    return true;
                case MENU_BUTTON_SHOW_HISTORY:
                    //WebAPI.doGetAllLogEntriesForAllUser(ActivityMain.this);
                    return true;
                case MENU_BUTTON_LOGOUT:
                    Log.d(TAG, "Starting logout from MainMenu");
                    WebAPI.logOut(ActivityMain.this);
                    replaceFragmentContainerFragmentWith(fragmentLogin);
                    return true;
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

            if (clickedCategory) {
                Log.d(TAG, ((Category) adapter.getItem(position)).getListItemTitle());
                WebAPI.doGetEquipmentType(ActivityMain.this, ((Category) adapter.getItem(position)).getListItemTitle());
                initializeFilterSpinner();
            }
            else if (clickedEquipment) {
                replaceFragmentContainerFragmentWith(fragmentUser); // should be fragmentEquipment !! <<<<<
                ((TextView)fragmentUser.getView().findViewById(R.id.tvUserFullName)).setText(((Equipment) adapter.getItem(position)).getDescription());
            }
            else if (clickedUser) {
                replaceFragmentContainerFragmentWith(fragmentUser);
                User user = ((User) adapter.getItem(position));
                fillTextViewsWithUserData(user);
            }
        }
    };

    private void fillTextViewsWithUserData(User user) {
        TextView textViewFullName = (TextView)fragmentUser.getView().findViewById(R.id.tvUserFullName);
        TextView textViewUsername = (TextView)fragmentUser.getView().findViewById(R.id.tvUserName);
        TextView textViewPhoneNumber = (TextView)fragmentUser.getView().findViewById(R.id.tvUserPhoneNumber);

        String fullName = user.getFirstname() + " " + user.getLastname();
        String username = user.getUserName();
        String phoneNumber = user.getPhone();

        textViewFullName.setText(fullName);
        textViewUsername.setText(username);
        textViewPhoneNumber.setText(phoneNumber);
    }

    private void initializeFilterSpinner()
    {
        Spinner spFilter = (Spinner)findViewById(R.id.spFilter);
        ArrayList<Category> categoryArray = new ArrayList<Category>();
        ArrayAdapter<Category> adapterInstance;
        adapterInstance = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_2, categoryArray);
        spFilter.setAdapter(adapterInstance);
    }

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


    public void logIn(User user, boolean success) {
        if (success) {
            Log.d(TAG, "User logged in: " + user.getFirstname() + " " + user.getLastname());
            replaceFragmentContainerFragmentWith(fragmentList);
            populateListViewWithCategories();
        }
        else {
            Toast.makeText(getApplicationContext(), "Feil brukernavn og/eller passord", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteUser() {
        WebAPI.doGetUsers(ActivityMain.this);
    }



    public void addToList(ArrayList<AssetManagerObjects> objects) {

        if (objects != null) {
            ListView lvList = (ListView) fragmentList.getView().findViewById(R.id.lvList);
            TextView tvTitle = ((TextView) fragmentList.getView().findViewById(R.id.tvTitle));

            if (objects.get(0) instanceof Equipment)
                tvTitle.setText(((Equipment) objects.get(0)).getType());
            else
                tvTitle.setText("Brukere");

            adapter = new AssetManagerAdapter(this, objects);
            lvList.setAdapter(adapter);
            lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
            registerForContextMenu(lvList);
        } else {
            Toast.makeText(this.getApplicationContext(), "Det finnes desverre ikke noe utstyr i denne kategorien.", Toast.LENGTH_SHORT).show();
        }
    }



    public void onClickLoginButton(View buttonView)
    {
        String username = ((EditText)fragmentLogin.getView().findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText)fragmentLogin.getView().findViewById(R.id.editTextPassword)).getText().toString();
        boolean isAdmin = ((CheckBox)fragmentLogin.getView().findViewById(R.id.checkBoxIsAdmin)).isChecked();

        attemptLogin(username, password, isAdmin);
    }


    private void attemptLogin(String username, String password, boolean isAdmin) {
        if (isAdmin)
            WebAPI.doLoginAdmin(this, username, password);
        else
            WebAPI.doLogin(this, username, password);
    }
}

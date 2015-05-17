package no.hin.student.myassetmanager.Activities;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;


import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.Classes.MyAdapter;
import no.hin.student.myassetmanager.Classes.MyObjects;
import no.hin.student.myassetmanager.Classes.User;

import no.hin.student.myassetmanager.Fragments.FragmentAsset;
import no.hin.student.myassetmanager.Fragments.FragmentList;
import no.hin.student.myassetmanager.Fragments.FragmentUser;
import no.hin.student.myassetmanager.R;


public class ActivityMain extends Activity implements FragmentUser.OnFragmentInteractionListener, FragmentAsset.OnFragmentInteractionListener, FragmentList.OnFragmentInteractionListener {

    private static final int MENU_CONTEXT_LIST_SHOW = 10101;
    private static final int MENU_CONTEXT_LIST_EDIT = 10102;
    private static final int MENU_CONTEXT_LIST_DELETE = 10103;

    private static final int MENU_BUTTON_SHOW_ASSETS = 10201;
    private static final int MENU_BUTTON_SHOW_USERS = 10202;
    private static final int MENU_BUTTON_SHOW_HISTORY = 10203;

    private FragmentList fragmentList = new FragmentList();
    private FragmentUser fragmentUser = new FragmentUser();


    private static final String TAG = "MyAssetManger-log";

    private MyAdapter adapterInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnMenu).setOnClickListener(mGlobal_OnClickListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_application, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.d(TAG, "Starting settings from MainMenu");
            return true;
        }
        if (id == R.id.action_logout) {
            Log.d(TAG, "Starting settings from MainMenu");
            WebAPI.logOut(ActivityMain.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentList = new FragmentList();
        fragmentUser = new FragmentUser();
        fragmentTransaction.add(R.id.fragment_container, fragmentList);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        WebAPI.doLoginAdmin(ActivityMain.this);
        getCategories();
    }

    @Override
    public void onStop() {
        super.onStop();
        WebAPI.logOut(ActivityMain.this);
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
    public void onFragmentInteraction(Uri uri) {

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
                    popup.show();
                    Log.d(TAG, "Adding menu to button.");
                    break;
            }
        }
    };



    final MenuItem.OnMenuItemClickListener mGlobal_OnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragmentList);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();

            switch (menuItem.getItemId()) {
                case MENU_BUTTON_SHOW_ASSETS:
                    Log.d(TAG, "Showing assets");
                    getCategories();
                    initializeFilterSpinner();
                    return true;
                case MENU_BUTTON_SHOW_USERS:
                    Log.d(TAG, "Showing users");
                    WebAPI.doGetUsers(ActivityMain.this);
                    initializeFilterSpinner();
                    return true;
                case MENU_BUTTON_SHOW_HISTORY:
                    //WebAPI.doLogin(ActivityMain.this);
                    //WebAPI.doGetUsers(ActivityMain.this);
                    //String type, String brand, String model, String description, String it_no, String aquired, byte[] image
                    WebAPI.addEquipment(ActivityMain.this, new Equipment("PC", "Microsoft", "Surface 2 Pro", "128 GB", "ITE1721", "02.02.2015", null));
                    WebAPI.addEquipment(ActivityMain.this, new Equipment("PC", "Microsoft", "Surface 2 Pro", "128 GB", "ITE1723", "02.02.2015", null));
                    WebAPI.addEquipment(ActivityMain.this, new Equipment("PC", "Microsoft", "Surface 2 Pro", "128 GB", "ITE1724", "2015-05-17", null));
                    return true;
                default:
                    return true;
            }
        }
    };



    // When clicking on a listview item
    final AdapterView.OnItemClickListener mGlobal_OnItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View row, int position, long index) {
            Log.d(TAG, "Clicking on equipment " + adapterInstance.getItem(position).toString());
            if (adapterInstance.getItem(position).getClass().equals(Category.class)) {
                Log.d(TAG, ((Category)adapterInstance.getItem(position)).getListItemTitle());
                WebAPI.doGetEquipmentType(ActivityMain.this, ((Category)adapterInstance.getItem(position)).getListItemTitle());
                initializeFilterSpinner();
            } else if (adapterInstance.getItem(position).getClass().equals(Equipment.class)) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentUser);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                ((TextView)fragmentUser.getView().findViewById(R.id.tvUserFullName)).setText( ((Equipment)adapterInstance.getItem(position)).getDescription() );
            } else if (adapterInstance.getItem(position).getClass().equals(User.class)) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragmentUser);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
                User user = ((User)adapterInstance.getItem(position));
                ((TextView)fragmentUser.getView().findViewById(R.id.tvUserFullName)).setText( user.getFirstname() + " " + user.getLastname() );
                ((TextView)fragmentUser.getView().findViewById(R.id.tvUserName)).setText( user.getUserName() );
                ((TextView)fragmentUser.getView().findViewById(R.id.tvUserPhoneNumber)).setText( user.getPhone() );
            }
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.lvList:

                super.onCreateContextMenu(menu, v, menuInfo);
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                String title = ((MyObjects)(adapterInstance.getItem(info.position))).getListItemTitle();

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
                if (adapterInstance.getItem(info.position).getClass().equals(Category.class) ) {
                    WebAPI.doGetEquipmentType(ActivityMain.this, ((Category)adapterInstance.getItem(info.position)).getListItemTitle());
                    Log.d(TAG, "Menu context show category!");
                }
                return true;
            case MENU_CONTEXT_LIST_DELETE:
                if ((adapterInstance != null) &&(adapterInstance.getItem(info.position).getClass().equals(Category.class))) {
                    Log.d(TAG, "Menu context delete category");

                }
                if ((adapterInstance != null) &&(adapterInstance.getItem(info.position).getClass().equals(User.class))) {
                    Log.d(TAG, "Menu context delete category");
                    WebAPI.doDeleteUser(ActivityMain.this, ((User)adapterInstance.getItem(info.position)).getId());
                }


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }





    public void logIn(User user) {
        Log.d(TAG, "User logged in: " + user.getFirstname() + " " + user.getLastname());
    }

    public void deleteUser() {
        WebAPI.doGetUsers(ActivityMain.this);
    }



    public void addToList(ArrayList<MyObjects> objects) {

        if (objects != null) {
            ListView lvList = (ListView) fragmentList.getView().findViewById(R.id.lvList);
            TextView tvTitle = ((TextView) fragmentList.getView().findViewById(R.id.tvTitle));
            tvTitle.setText(((Equipment) objects.get(0)).getType());
            ArrayList<MyObjects> array = new ArrayList<MyObjects>();
            adapterInstance = new MyAdapter(this, array);
            for (int i = 0; i < objects.size(); i++) {
                    adapterInstance.add(objects.get(i));
            }
            lvList.setAdapter(adapterInstance);
            lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
            registerForContextMenu(lvList);
        } else {
            Toast.makeText(this.getApplicationContext(), "Det finnes desverre ikke noe utstyr i denne kategorien.", Toast.LENGTH_SHORT).show();
        }
    }



    private void getCategories() {
        ListView lvList = (ListView)fragmentList.getView().findViewById(R.id.lvList);
        ((TextView)fragmentList.getView().findViewById(R.id.tvTitle)).setText("Kategori");
        ArrayList<Category> categoryArray = new ArrayList<Category>();
        adapterInstance = new MyAdapter(this, categoryArray);
        Category.showCategories(adapterInstance);
        lvList.setAdapter(adapterInstance);
        lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
        registerForContextMenu(lvList);
    }





}

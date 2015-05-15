package no.hin.student.myassetmanager.Activities;


import android.app.Activity;
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

import java.util.ArrayList;

import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
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

        return super.onOptionsItemSelected(item);
    }


    private <T extends MyObjects> void initializeList(Class<T> classType)
    {
        final ListView lvList = (ListView)findViewById(R.id.lvList);
        if (classType.equals(Category.class)) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Kategori");
            ArrayList<Category> categoryArray = new ArrayList<Category>();
            adapterInstance = new MyAdapter(this, categoryArray);
            Category.showCategories(adapterInstance);
        }

        if (classType.equals(User.class)) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Brukere");
            ArrayList<User> userArray = new ArrayList<User>();
            adapterInstance = new MyAdapter(this, userArray);
            User.showUsers(adapterInstance);
        }

        if (classType.equals(Equipment.class)) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Utstyr");
            ArrayList<Equipment> equipmentArray = new ArrayList<Equipment>();
            adapterInstance = new MyAdapter(this, equipmentArray);
            Equipment.showEquipment(adapterInstance);
        }
        lvList.setAdapter(adapterInstance);
        lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
        registerForContextMenu(lvList);
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
            switch (menuItem.getItemId()) {
                case MENU_BUTTON_SHOW_ASSETS:
                    Log.d(TAG, "Showing assets");
                    initializeList(Category.class);
                    initializeFilterSpinner();
                    return true;
                case MENU_BUTTON_SHOW_USERS:
                    Log.d(TAG, "Showing users");
                    initializeList(User.class);
                    initializeFilterSpinner();
                    return true;
                default:
                    return true;
            }
        }
    };

    final AdapterView.OnItemClickListener mGlobal_OnItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View row, int position, long index) {
            Log.d(TAG, "Clicking on equipment " + adapterInstance.getItem(position).toString());
            if (adapterInstance.getItem(position).getClass().equals(Category.class)) {
                initializeList(Equipment.class);
                initializeFilterSpinner();
            }
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.lvList:

                super.onCreateContextMenu(menu, v, menuInfo);
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                String title = (adapterInstance.getItem(info.position)).toString();

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
                    Log.d(TAG, "Working to filter on generic!");
                }
                return true;
            case MENU_CONTEXT_LIST_DELETE:
                if ((adapterInstance != null) &&(adapterInstance.getItem(info.position).getClass().equals(Category.class))) {
                    Log.d(TAG, "Menu context delete category");
                    Category.deleteCategory(adapterInstance, (Category)adapterInstance.getItem(info.position));
                }
                if ((adapterInstance != null) &&(adapterInstance.getItem(info.position).getClass().equals(User.class))) {
                    Log.d(TAG, "Menu context delete category");
                    User.deleteUser(adapterInstance, (User)adapterInstance.getItem(info.position));
                }


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

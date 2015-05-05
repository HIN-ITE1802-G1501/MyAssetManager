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

    private ArrayAdapter<Category> adapterInstanceCategory;
    private ArrayAdapter<User> adapterInstanceUser;
    private ArrayAdapter<Equipment> adapterInstanceEquipment;
    //private ArrayAdapter<? extends MyObjects> adapterInstance;

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
        if (classType.equals(Category.class)) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Kategorier");
            final ListView lvList = (ListView)findViewById(R.id.lvList);

            ArrayList<Category> categoryArray = new ArrayList<Category>();

            adapterInstanceCategory = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categoryArray);
            Category.showCategories(adapterInstanceCategory);

            lvList.setAdapter(adapterInstanceCategory);
            lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
            registerForContextMenu(lvList);
        }

        if (classType.equals(User.class)) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Brukere");
            final ListView lvList = (ListView)findViewById(R.id.lvList);

            ArrayList<User> userArray = new ArrayList<User>();

            adapterInstanceUser = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, userArray);
            User.showUsers(adapterInstanceUser);

            lvList.setAdapter(adapterInstanceUser);
            lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
            registerForContextMenu(lvList);
        }

        if (classType.equals(Equipment.class)) {
            ((TextView)findViewById(R.id.tvTitle)).setText("Utstyr");
            final ListView lvList = (ListView)findViewById(R.id.lvList);

            ArrayList<Equipment> equipmentArray = new ArrayList<Equipment>();

            adapterInstanceEquipment = new ArrayAdapter<Equipment>(this, android.R.layout.simple_list_item_1, equipmentArray);
            Equipment.showEquipment(adapterInstanceEquipment);

            lvList.setAdapter(adapterInstanceEquipment);
            lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
            registerForContextMenu(lvList);
        }
    }


    private void initializeFilterSpinner()
    {
        Spinner spFilter = (Spinner)findViewById(R.id.spFilter);
        ArrayList<Category> categoryArray = new ArrayList<Category>();
        ArrayAdapter<Category> adapterInstance;
        adapterInstance = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categoryArray);
        spFilter.setAdapter(adapterInstance);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


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
            Log.d(TAG, "Clicking on equipment " + adapterInstanceCategory.getItem(position).toString());
            if (adapterInstanceCategory.getItem(position).getClass().equals(Category.class)) {
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
                String title = ((Category) adapterInstanceCategory.getItem(info.position)).toString();

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
                if (adapterInstanceCategory.getItem(info.position).getClass().equals(Category.class) ) {
                    Log.d(TAG, "Working to filter on generic!");
                }
                return true;
            case MENU_CONTEXT_LIST_DELETE:
                if ((adapterInstanceCategory != null) &&(adapterInstanceCategory.getItem(info.position).getClass().equals(Category.class))) {
                    Log.d(TAG, "Menu context delete category");
                    Category.deleteCategory(adapterInstanceCategory, adapterInstanceCategory.getItem(info.position));
                }
                if ((adapterInstanceUser != null) &&(adapterInstanceUser.getItem(info.position).getClass().equals(User.class))) {
                    Log.d(TAG, "Menu context delete category");
                    User.deleteUser(adapterInstanceUser, adapterInstanceUser.getItem(info.position));
                }


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

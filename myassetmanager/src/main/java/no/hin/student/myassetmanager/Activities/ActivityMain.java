package no.hin.student.myassetmanager.Activities;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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

import no.hin.student.myassetmanager.Classes.Asset;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.Fragments.FragmentAsset;
import no.hin.student.myassetmanager.Fragments.FragmentList;
import no.hin.student.myassetmanager.Fragments.FragmentUser;
import no.hin.student.myassetmanager.R;


public class ActivityMain extends Activity implements FragmentUser.OnFragmentInteractionListener, FragmentAsset.OnFragmentInteractionListener, FragmentList.OnFragmentInteractionListener {


    private static final int MENU_CONTEXT_DELETE_ASSET = 10101;
    private static final int MENU_CONTEXT_DELETE_EDIT = 10102;
    private static final String TAG = "MyAssetManger-log";


    private ArrayAdapter<Category> adapterInstanceCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeCategoryList();
        initializeFilterSpinner();
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
            return true;
        }

        if (id == R.id.action_delete) {
            Log.d("Test","Jippp");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUserList()
    {
        ListView lvList = (ListView)findViewById(R.id.lvList);
        ArrayList<User> userArray = new ArrayList<User>();
        ArrayAdapter<User> adapterInstance;
        adapterInstance = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, userArray);
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        lvList.setAdapter(adapterInstance);
    }

    private void initializeAssetList()
    {
        ListView lvList = (ListView)findViewById(R.id.lvList);
        ArrayList<Asset> userArray = new ArrayList<Asset>();
        ArrayAdapter<Asset> adapterInstance;
        adapterInstance = new ArrayAdapter<Asset>(this, android.R.layout.simple_list_item_1, userArray);
        adapterInstance.add(new Asset("Lumia 640"));
        adapterInstance.add(new Asset("Lumia 1020"));
        adapterInstance.add(new Asset("Lumia 930"));
        adapterInstance.add(new Asset("Lumia 820"));
        lvList.setAdapter(adapterInstance);
    }

    private void initializeCategoryList()
    {
        final TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("Kategorier");
        final ListView lvList = (ListView)findViewById(R.id.lvList);
        ArrayList<Category> userArray = new ArrayList<Category>();

        adapterInstanceCategory = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, userArray);
        adapterInstanceCategory.add(new Category("PC"));
        adapterInstanceCategory.add(new Category("Telefon"));
        adapterInstanceCategory.add(new Category("Switch"));
        adapterInstanceCategory.add(new Category("Server"));
        lvList.setAdapter(adapterInstanceCategory);
        registerForContextMenu(lvList);
    }


    private void initializeFilterSpinner()
    {
        Spinner spFilter = (Spinner)findViewById(R.id.spFilter);
        ArrayList<Category> categoryArray = new ArrayList<Category>();
        ArrayAdapter<Category> adapterInstance;
        adapterInstance = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categoryArray);
        adapterInstance.add(new Category("Alle"));
        adapterInstance.add(new Category("Tilgjengelig"));
        adapterInstance.add(new Category("Utlånt"));
        spFilter.setAdapter(adapterInstance);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void showMenuMain(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, popup.getMenu());
        popup.show();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId()==R.id.lvList) {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String title = ((Category) adapterInstanceCategory.getItem(info.position)).toString();

            menu.setHeaderTitle(title);

            menu.setHeaderTitle("Handlinger");
            menu.add( Menu.NONE, MENU_CONTEXT_DELETE_EDIT, Menu.NONE, "Rediger");
            menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ASSET, Menu.NONE, "Slett");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_CONTEXT_DELETE_ASSET:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Log.d(TAG, "removing item pos=" + info.position);
                adapterInstanceCategory.remove(adapterInstanceCategory.getItem(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

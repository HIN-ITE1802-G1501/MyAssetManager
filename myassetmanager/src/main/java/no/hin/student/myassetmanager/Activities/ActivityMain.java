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
        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("Kategorier");
        final ListView lvList = (ListView)findViewById(R.id.lvList);
        ArrayList<Category> userArray = new ArrayList<Category>();
        ArrayAdapter<Category> adapterInstance;
        adapterInstance = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, userArray);
        adapterInstance.add(new Category("PC"));
        adapterInstance.add(new Category("Telefon"));
        adapterInstance.add(new Category("Switch"));
        adapterInstance.add(new Category("Server"));
        lvList.setAdapter(adapterInstance);
        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                PopupMenu popup = new PopupMenu(ActivityMain.this, lvList);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_list_actions, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                        Log.d("Test","test");
                        Toast.makeText(ActivityMain.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
                return false;
            }
        });
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

}

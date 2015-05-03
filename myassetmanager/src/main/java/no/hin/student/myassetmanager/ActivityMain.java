package no.hin.student.myassetmanager;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import java.util.ArrayList;


public class ActivityMain extends Activity implements FragmentUsers.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUserList();
        initializeCategoryList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUserList()
    {
        ListView lvUsers = (ListView)findViewById(R.id.lvUsers);
        ArrayList<User> userArray = new ArrayList<User>();
        ArrayAdapter<User> adapterInstance;
        adapterInstance = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, userArray);
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        adapterInstance.add(new User("Test", "Kurt-Erik", "Karlsen"));
        lvUsers.setAdapter(adapterInstance);
    }

    private void initializeCategoryList()
    {
        Spinner spCategories = (Spinner)findViewById(R.id.spCategories);
        ArrayList<Category> categoryArray = new ArrayList<Category>();

        ArrayAdapter<Category> adapterInstance;

        adapterInstance = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categoryArray);
        adapterInstance.add(new Category("Tablet"));
        adapterInstance.add(new Category("Phone"));
        adapterInstance.add(new Category("Tablet"));
        adapterInstance.add(new Category("PC"));

        spCategories.setAdapter(adapterInstance);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    public void showMenuMain(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();
    }
}

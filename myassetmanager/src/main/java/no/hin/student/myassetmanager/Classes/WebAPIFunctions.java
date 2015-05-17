package no.hin.student.myassetmanager.Classes;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import no.hin.student.myassetmanager.Fragments.FragmentList;
import no.hin.student.myassetmanager.Fragments.FragmentUser;
import no.hin.student.myassetmanager.R;

/**
 * Created by kurt-erik on 17.05.2015.
 */
public class WebAPIFunctions {

    private FragmentList fragmentList = new FragmentList();
    private FragmentUser fragmentUser = new FragmentUser();
    private MyAdapter adapterInstance;

    public void addToList(ArrayList<MyObjects> objects) {
        ListView lvList = (ListView)fragmentList.getView().findViewById(R.id.lvList);

        ((TextView)fragmentList.getView().findViewById(R.id.tvTitle)).setText("Utstyr");
        ArrayList<MyObjects> array = new ArrayList<MyObjects>();
        //adapterInstance = new MyAdapter(this, array);
        if (objects != null) {
            for (int i = 0; i < objects.size(); i++) {
                adapterInstance.add(objects.get(i));
            }
        }
        lvList.setAdapter(adapterInstance);
        //lvList.setOnItemClickListener(mGlobal_OnItemClickListener);
        //Context.registerForContextMenu(lvList);
    }
}

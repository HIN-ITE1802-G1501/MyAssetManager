/**
* This class implements the FragmentList
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.AssetManagerAdapter;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;



public class FragmentList extends Fragment {
    View view;
    ViewGroup container;
    public FragmentList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.container = container;
        this.view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(App.TAG, "landscare from list fragment");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d(App.TAG, "portrait from list fragment");
        }

        //LayoutInflater inflater = LayoutInflater.from(getActivity());
        //populateViewForOrientation(inflater, this.container);
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        ActivityMain activityMain = ((ActivityMain)getActivity());
        viewGroup.removeAllViewsInLayout();
        this.view = inflater.inflate(R.layout.fragment_list, viewGroup);
        WebAPI.doGetEquipmentWithoutLogin(App.getContext());
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText("Test");
        // Find your buttons in subview, set up onclicks, set up callbacks to your parent fragment or activity here.
        // You can create ViewHolder or separate method for that.
        // example of accessing views: TextView textViewExample = (TextView) view.findViewById(R.id.text_view_example);
        // textViewExample.setText("example");
     }


}

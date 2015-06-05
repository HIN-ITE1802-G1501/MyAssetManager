/**
 * This is the List fragment that allows the app to display users, categories, equipment and history
 * @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
 * @version 1.1
 */


package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;



public class FragmentList extends Fragment {
    View view;                  // View of this fragment for handling orientation change
    ViewGroup container;        // ViewGroup for this fragment for handling orientation change


    /**
     * Default constructor
     */
    public FragmentList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        this.view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // TODO: IMPROVEMENT - Handle orientation change for this fragment
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(App.TAG, "landscape from list fragment");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d(App.TAG, "portrait from list fragment");
        }

        //LayoutInflater inflater = LayoutInflater.from(getActivity());
        //populateViewForOrientation(inflater, this.container);
    }

    /**
     * Method recreating view when orientation change.
     */
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

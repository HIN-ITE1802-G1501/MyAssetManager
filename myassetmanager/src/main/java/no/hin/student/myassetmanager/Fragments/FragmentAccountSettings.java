/**
* This is the AccountSettings fragment that displayes the user's account
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.Login;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.R;

public class FragmentAccountSettings extends Fragment
{
    private ActivityMain parentActivity;    // Variable for parentActivity context
    private User user;                      // Variable for user

    public FragmentAccountSettings() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        parentActivity = (ActivityMain)activity;
        user = Login.getLoggedInUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((EditText)getView().findViewById(R.id.editTextSettingsFirstname)).setText(user.getFirstname());
        ((EditText)getView().findViewById(R.id.editTextSettingsLastname)).setText(user.getLastname());
        ((EditText)getView().findViewById(R.id.editTextSettingsPhonenumber)).setText(user.getPhone());
        ((EditText)getView().findViewById(R.id.editTextSettingsUsername)).setText(user.getUserName());
    }

}

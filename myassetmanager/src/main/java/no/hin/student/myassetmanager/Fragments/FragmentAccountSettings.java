/**
* This is the AccountSettings fragment that displayes the user's account
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.Login;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;

public class FragmentAccountSettings extends Fragment implements View.OnClickListener
{
    private ActivityMain parentActivity;    // Variable for parentActivity context
    private User user;                      // Variable for user

    /**
     * Default constructor
     */
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

        Button btnUpdateUser = (Button)getView().findViewById(R.id.buttonSettingsUpdateInfo);
        btnUpdateUser.setOnClickListener(this);

        Button btnUpdatePassword = (Button)getView().findViewById(R.id.buttonSettingsUpdatePassword);
        btnUpdatePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch(v.getId()) {
                case R.id.buttonSettingsUpdateInfo: // When the user clicks the update user info button
                    updateUserInfo();
                    break;
                case R.id.buttonSettingsUpdatePassword: // When the user clicks the update user password button
                    updateUserPassword();
                    break;
            }
        } catch (Exception e) {
            Log.d(App.TAG, e.toString());
        }
    }

    private void updateUserInfo() {
        String firstname = ((EditText)getView().findViewById(R.id.editTextSettingsFirstname)).getText().toString();
        String lastname = ((EditText)getView().findViewById(R.id.editTextSettingsLastname)).getText().toString();
        String phoneNumber = ((EditText)getView().findViewById(R.id.editTextSettingsPhonenumber)).getText().toString();
        String username = ((EditText)getView().findViewById(R.id.editTextSettingsUsername)).getText().toString();

        Login.getLoggedInUser().setFirstname(firstname);
        Login.getLoggedInUser().setLastname(lastname);
        Login.getLoggedInUser().setPhone(phoneNumber);
        Login.getLoggedInUser().setUserName(username);

        WebAPI.doUpdateUser(parentActivity, Login.getLoggedInUser());
    }

    private void updateUserPassword() {
        String password = ((EditText)getView().findViewById(R.id.editTextSettingsNewPassword)).getText().toString();
        String repeatedPassword = ((EditText)getView().findViewById(R.id.editTextSettingsRepeatPassword)).getText().toString();

        if (!password.equals(repeatedPassword))
            Toast.makeText(parentActivity, "Passord matchet ikke hverandre. Prøv på nytt", Toast.LENGTH_LONG).show();
        else
            WebAPI.doChangeUserPassword(parentActivity, Login.getLoggedInUser().getU_id(), password);
    }

}

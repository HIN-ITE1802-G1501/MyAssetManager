package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.Login;
import no.hin.student.myassetmanager.R;

public class FragmentLogin extends Fragment  implements View.OnClickListener
{
    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button btnLogin = (Button)getView().findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Button btnRegister = (Button)getView().findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            ActivityMain activityMain = ((ActivityMain)getActivity());
            switch(v.getId()) {
                case R.id.btnLogin:
                    Login.setUsername( ((EditText)activityMain.fragmentLogin.getView().findViewById(R.id.editTextUsername)).getText().toString() );
                    Login.setPassword( ((EditText) activityMain.fragmentLogin.getView().findViewById(R.id.editTextPassword)).getText().toString() );
                    Login.setAdmin(((CheckBox) activityMain.fragmentLogin.getView().findViewById(R.id.checkBoxIsAdmin)).isChecked());

                    activityMain.attemptLogin(Login.getUsername(), Login.getPassword(), Login.getAdmin());
                    break;
                case R.id.btnRegister:
                    activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentRegister);
                    activityMain.fragmentRegister.populateNew();
                    break;
            }
        } catch (Exception e) {
            Log.d(App.TAG, e.toString());
        }
    }
}

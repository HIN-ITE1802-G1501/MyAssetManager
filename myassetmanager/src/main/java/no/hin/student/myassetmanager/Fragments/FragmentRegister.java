package no.hin.student.myassetmanager.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;

public class FragmentRegister extends Fragment implements View.OnClickListener {
    private User user;
    private Boolean update = false;
    public FragmentRegister() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }



    public void populateNew() {
             try {
                 this.user = null;
                 update = false;
                 ((TextView)getView().findViewById(R.id.tvAddRegisterHeader)).setText(getString(R.string.fragment_Register_tvAddRegisterHeader_New));
                 ((EditText) getView().findViewById(R.id.etAddRegisterFirstname)).setText("");
                 ((EditText)getView().findViewById(R.id.etAddRegisterLastname)).setText("");
                 ((EditText)getView().findViewById(R.id.etAddRegisterPhone)).setText("");
                 ((EditText)getView().findViewById(R.id.etAddRegisterUsername)).setText("");
                 ((CheckBox)getView().findViewById(R.id.cbAddRegisterUpdatePassword)).setVisibility(View.INVISIBLE);
                 ((EditText)getView().findViewById(R.id.etAddRegisterPassword)).setEnabled(false);
                 ((EditText)getView().findViewById(R.id.etAddRegisterPassword)).setText("");

                 ImageButton btnRegisterCreate = (ImageButton) getView().findViewById(R.id.btnRegisterCreate);
                 ImageButton btnRegisterCancel = (ImageButton) getView().findViewById(R.id.btnRegisterCancel);
                 btnRegisterCreate.setImageResource(R.drawable.btn_adduser);
                 btnRegisterCreate.setOnClickListener(this);
                 btnRegisterCancel.setOnClickListener(this);
             } catch (Exception e) {
                 Log.d("-log", e.toString());
             }
         }

    public void populatEdit(User user) {
        try {
            this.user = user;
            update = true;
            ((TextView)getView().findViewById(R.id.tvAddRegisterHeader)).setText(getString(R.string.fragment_Register_tvAddRegisterHeader_Edit));
            ((EditText)getView().findViewById(R.id.etAddRegisterFirstname)).setText(this.user.getFirstname());
            ((EditText)getView().findViewById(R.id.etAddRegisterLastname)).setText(this.user.getLastname());
            ((EditText)getView().findViewById(R.id.etAddRegisterPhone)).setText(this.user.getPhone());
            ((EditText)getView().findViewById(R.id.etAddRegisterUsername)).setText(this.user.getUserName());
            ((CheckBox)getView().findViewById(R.id.cbAddRegisterUpdatePassword)).setVisibility(View.VISIBLE);
            ((EditText)getView().findViewById(R.id.etAddRegisterPassword)).setEnabled(false);
            ((EditText) getView().findViewById(R.id.etAddRegisterPassword)).setText("");

            ImageButton btnRegisterCreate = (ImageButton) getView().findViewById(R.id.btnRegisterCreate);
            ImageButton btnRegisterCancel = (ImageButton) getView().findViewById(R.id.btnRegisterCancel);
            CheckBox cbAddRegisterUpdatePassword = ((CheckBox) getView().findViewById(R.id.cbAddRegisterUpdatePassword));

            btnRegisterCreate.setImageResource(R.drawable.btn_save);
            btnRegisterCreate.setOnClickListener(this);
            btnRegisterCancel.setOnClickListener(this);
            cbAddRegisterUpdatePassword.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("-log", e.toString());
        }
    }


    @Override
    public void onClick(View v) {
      try {
          ActivityMain activityMain = ((ActivityMain)getActivity());
          switch(v.getId()) {
              case R.id.btnRegisterCreate:
                  String firstname = ((EditText)getView().findViewById(R.id.etAddRegisterFirstname)).getText().toString();
                  String lastname = ((EditText)getView().findViewById(R.id.etAddRegisterLastname)).getText().toString();
                  String phoneNumber = ((EditText)getView().findViewById(R.id.etAddRegisterPhone)).getText().toString();
                  String username = ((EditText)getView().findViewById(R.id.etAddRegisterUsername)).getText().toString();
                  String password = ((EditText)getView().findViewById(R.id.etAddRegisterPassword)).getText().toString();

                  if (activityMain.getCurrentUserStatus() == ActivityMain.IS_LOGGED_OUT) {
                      User user = new User(username, password, firstname, lastname, phoneNumber, false);
                      WebAPI.doAddUserWithoutLogin(v.getContext(), user);
                      activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentLogin);
                  } else if (activityMain.getCurrentUserStatus() == ActivityMain.IS_ADMIN_USER) {
                      if (update) {
                          Boolean checked =  ((CheckBox)getView().findViewById(R.id.cbAddRegisterUpdatePassword)).isChecked();
                          this.user.setFirstname(firstname);
                          this.user.setLastname(lastname);
                          this.user.setPhone(phoneNumber);
                          this.user.setUserName(username);

                          WebAPI.doUpdateUser(v.getContext(), this.user);
                          if (checked)
                            WebAPI.doChangeUserPassword(v.getContext(), this.user.getId(), password);
                      } else {
                          User user = new User(username, password, firstname, lastname, phoneNumber, false);
                          WebAPI.doAddUser(v.getContext(), user);
                      }
                  } else {
                      Log.d("-log", "Could not add user");
                  }
                  break;
              case R.id.btnRegisterCancel:

                  break;
              case R.id.cbAddRegisterUpdatePassword:
                  Boolean checked =  ((CheckBox)getView().findViewById(R.id.cbAddRegisterUpdatePassword)).isChecked();
                  ((EditText)getView().findViewById(R.id.etAddRegisterPassword)).setEnabled(checked);
                  break;
          }
      } catch (Exception e) {
          Log.d("-log", e.toString());
      }
    }
}

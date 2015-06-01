package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;



public class FragmentUser extends Fragment {
    private User user;

    public FragmentUser() {

    }

    public User getUser() {
        return this.user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    public void populateUserFragmentWithUserData(User user, Context context) {
        this.user = user;

        ImageView ivUserFragment = (ImageView)getView().findViewById(R.id.ivUserFragment);
        TextView textViewFullName = (TextView)getView().findViewById(R.id.textViewEquipmentBrand);
        TextView textViewUsername = (TextView)getView().findViewById(R.id.textViewEquipmentModel);
        TextView textViewPhoneNumber = (TextView)getView().findViewById(R.id.textViewEquipmentId);

        ImageButton btnUserCall = (ImageButton)getView().findViewById(R.id.btnUserCall);
        ImageButton btnUserSMS = (ImageButton)getView().findViewById(R.id.btnUserSMS);
        ImageButton btnUserEdit = (ImageButton)getView().findViewById(R.id.btnUserEdit);
        ImageButton btnUserActivate = (ImageButton)getView().findViewById(R.id.btnUserActivate);

        ivUserFragment.setImageResource((this.user.isUser_activated()) ? R.drawable.user : R.drawable.user_notactive);
        btnUserActivate.setImageResource((this.user.isUser_activated()) ? R.drawable.user_activate : R.drawable.user_deactivate);

        textViewFullName.setText(user.getFirstname() + " " + user.getLastname());
        textViewUsername.setText(user.getUserName());
        textViewPhoneNumber.setText(user.getPhone());


        WebAPI.doGetOpenLogEntriesForUser(context, WebAPI.Method.GET_LOG_ENTRIES_FOR_USER_FRAGMENT, user.getId());
    }
}

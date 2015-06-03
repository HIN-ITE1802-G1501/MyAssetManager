package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;



public class FragmentUser extends Fragment implements View.OnClickListener {
    private User user;

    public FragmentUser() {

    }

    public User getUser() {
        return this.user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);
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
        btnUserActivate.setImageResource((this.user.isUser_activated()) ? R.drawable.user_deactivate : R.drawable.user_activate);

        textViewFullName.setText(user.getFirstname() + " " + user.getLastname());
        textViewUsername.setText(user.getUserName());
        textViewPhoneNumber.setText(user.getPhone());

        WebAPI.doGetOpenLogEntriesForUser(context, WebAPI.Method.GET_LOG_ENTRIES_FOR_USER_FRAGMENT, user.getId());

        btnUserCall.setOnClickListener(this);
        btnUserSMS.setOnClickListener(this);
        btnUserEdit.setOnClickListener(this);
        btnUserActivate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch(v.getId()) {
                case R.id.btnUserCall:
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.user.getPhone()));
                    startActivity(intent);
                    break;
                case R.id.btnUserSMS:
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", this.user.getPhone());
                    smsIntent.putExtra("sms_body",getString(R.string.fragment_user_tvUserSMSMessage));
                    startActivity(smsIntent);
                    break;
                case R.id.btnUserActivate:
                        this.user.setUser_activated(!user.isUser_activated());
                        ImageButton btnUserActivate = (ImageButton)v.findViewById(R.id.btnUserActivate);
                        btnUserActivate.setImageResource((this.user.isUser_activated()) ? R.drawable.user_deactivate : R.drawable.user_activate);

                        ImageView ivUserFragment = (ImageView)getView().findViewById(R.id.ivUserFragment);
                        ivUserFragment.setImageResource((this.user.isUser_activated()) ? R.drawable.user : R.drawable.user_notactive);

                        WebAPI.doUpdateUser(v.getContext(),this.user);
                    break;
                case R.id.btnUserEdit:

                    break;
                case R.id.btnUserDelete:
                    WebAPI.doDeleteUser(v.getContext(), this.user.getId());
                    break;
            }
        } catch (Exception e) {
            Log.d("-log", e.toString());
        }
    }
}

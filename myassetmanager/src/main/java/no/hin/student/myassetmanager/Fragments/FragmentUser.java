package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.hin.student.myassetmanager.Classes.User;
import no.hin.student.myassetmanager.R;



public class FragmentUser extends Fragment {

    public FragmentUser() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    public void populateUserFragmentWithUserData(User user) {
        TextView textViewFullName = (TextView)getView().findViewById(R.id.textViewEquipmentBrand);
        TextView textViewUsername = (TextView)getView().findViewById(R.id.textViewEquipmentModel);
        TextView textViewPhoneNumber = (TextView)getView().findViewById(R.id.textViewEquipmentId);

        textViewFullName.setText(user.getFirstname() + " " + user.getLastname());
        textViewUsername.setText(user.getUserName());
        textViewPhoneNumber.setText(user.getPhone());
    }
}

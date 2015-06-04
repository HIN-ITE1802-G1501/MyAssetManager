package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.R;

public class FragmentLogin extends Fragment  implements View.OnClickListener
{
    public FragmentLogin()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onClick(View v) {
        try {
            ActivityMain activityMain = ((ActivityMain)getActivity());
            switch(v.getId()) {
                case R.id.btnRegisterCreate:

                    break;
                case R.id.btnRegisterCancel:
                    activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentList);
                    break;
            }
        } catch (Exception e) {
            Log.d("-log", e.toString());
        }
    }
}

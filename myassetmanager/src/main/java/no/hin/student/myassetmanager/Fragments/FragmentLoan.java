package no.hin.student.myassetmanager.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;

public class FragmentLoan extends Fragment
{

    public FragmentLoan()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan, container, false);
    }

    public void populateLoanFragmentWithData(Equipment equipment, Context context) {
        TextView textViewId = (TextView)getView().findViewById(R.id.textViewLoanEquipmentId);
        TextView textViewBrand = (TextView)getView().findViewById(R.id.textViewLoanEquipmentBrand);
        TextView textViewModel = (TextView)getView().findViewById(R.id.textViewLoanEquipmentModel);

        textViewId.setText("" + equipment.getE_id());
        textViewBrand.setText(equipment.getBrand());
        textViewModel.setText(equipment.getModel());

        WebAPI.doGetUsers(context, WebAPI.Method.GET_USERS_FOR_LOAN_FRAGMENT);
    }

}

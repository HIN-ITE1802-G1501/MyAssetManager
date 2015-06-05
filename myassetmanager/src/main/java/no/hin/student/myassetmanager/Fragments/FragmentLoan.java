/**
 * This is the Loan fragment that allows the administrator to loan an equipment to a user
 * @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
 * @version 1.1
 */

package no.hin.student.myassetmanager.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;

public class FragmentLoan extends Fragment {

    // TODO: IMPROVEMENT - Update GUI to be consistent with other fragments
    public FragmentLoan() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loan, container, false);
    }

    public void populateLoanFragmentWithData(Equipment equipment, Context context) {
        TextView textViewId = (TextView)getView().findViewById(R.id.textViewLoanEquipmentId);
        TextView textViewBrand = (TextView)getView().findViewById(R.id.textViewLoanEquipmentBrand);
        TextView textViewModel = (TextView)getView().findViewById(R.id.textViewLoanEquipmentModel);

        textViewId.setText("" + equipment.getE_id());
        textViewBrand.setText(equipment.getBrand());
        textViewModel.setText(equipment.getModel());

        Log.d(App.TAG, "Before getting users");
        WebAPI.doGetUsers(context, WebAPI.Method.GET_USERS_FOR_LOAN_FRAGMENT);
    }

}

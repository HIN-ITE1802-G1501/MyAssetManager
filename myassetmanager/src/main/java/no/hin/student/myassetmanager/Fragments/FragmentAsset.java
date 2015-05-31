package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.EquipmentStatus;
import no.hin.student.myassetmanager.R;



public class FragmentAsset extends Fragment {

    public FragmentAsset() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asset, container, false);
    }

    public void populateAssetFragmentWithAssetData(Equipment equipment, int userStatus) {
        TextView textViewId = (TextView)getView().findViewById(R.id.textViewEquipmentId);
        TextView textViewBrand = (TextView)getView().findViewById(R.id.textViewEquipmentBrand);
        TextView textViewModel = (TextView)getView().findViewById(R.id.textViewEquipmentModel);

        textViewId.setText("" + equipment.getE_id());
        textViewBrand.setText(equipment.getBrand());
        textViewModel.setText(equipment.getModel());

        Button buttonLoan = (Button)getView().findViewById(R.id.buttonEquipmentLoan);
        TextView textViewStatusLabel = (TextView)getView().findViewById(R.id.textViewEquipmentStatusLabel);
        TextView textViewStatus = (TextView)getView().findViewById(R.id.textViewEquipmentStatus);

        if (userStatus == ActivityMain.IS_ADMIN_USER && EquipmentStatus.isEquipmentAvailable(equipment)) {
            buttonLoan.setVisibility(View.VISIBLE);
            buttonLoan.setText("Registrer utlån");
            textViewStatusLabel.setVisibility(View.VISIBLE);
            textViewStatus.setVisibility(View.VISIBLE);
            textViewStatus.setText("På lager");
        }
        else if (userStatus == ActivityMain.IS_ADMIN_USER && !EquipmentStatus.isEquipmentAvailable(equipment)) {
            buttonLoan.setText("Registrer innlevering");
            textViewStatus.setText("Utlånt");
        }
        else if (userStatus == ActivityMain.IS_REGULAR_USER) {
            buttonLoan.setVisibility(View.INVISIBLE);
            textViewStatusLabel.setVisibility(View.INVISIBLE);
            textViewStatus.setVisibility(View.INVISIBLE);
        }
    }

}

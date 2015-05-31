package no.hin.student.myassetmanager.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView textViewStatus = (TextView)getView().findViewById(R.id.textViewEquipmentStatus);

        textViewId.setText("" + equipment.getE_id());
        textViewBrand.setText(equipment.getBrand());
        textViewModel.setText(equipment.getModel());

        if (userStatus == ActivityMain.IS_ADMIN_USER && EquipmentStatus.isEquipmentAvailable(equipment)) {
            getView().findViewById(R.id.buttonEquipmentRegisterLoan).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.textViewEquipmentStatusLabel).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.textViewEquipmentStatus).setVisibility(View.VISIBLE);
            textViewStatus.setText("På lager");
        }
        else if (userStatus == ActivityMain.IS_ADMIN_USER && !EquipmentStatus.isEquipmentAvailable(equipment)) {
            getView().findViewById(R.id.buttonEquipmentRegisterLoan).setVisibility(View.INVISIBLE);
            textViewStatus.setText("Utlånt");
        }
        else if (userStatus == ActivityMain.IS_REGULAR_USER) {
            getView().findViewById(R.id.buttonEquipmentRegisterLoan).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.textViewEquipmentStatusLabel).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.textViewEquipmentStatus).setVisibility(View.INVISIBLE);
        }
    }

}

/**
  * This is the Asset fragment that allows the user to add equipment to database
  * @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
  * @version 1.1
  */


package no.hin.student.myassetmanager.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.EquipmentStatus;
import no.hin.student.myassetmanager.Classes.Login;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;


public class FragmentAsset extends Fragment implements View.OnClickListener {
     private Equipment equipment;       // Thw equipment object that is displayed


    /**
     * Default constructor
     */
    // TODO: IMPROVEMENT - Updated GUI with picture and add labels for user
     public FragmentAsset() {

     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_asset, container, false);
     }


    /**
     * Method for populating the Asset fragment with data
     *
     * @param equipment the equipment object we are displaying
     */
     public void populateAssetFragmentWithAssetData(Equipment equipment) {
         try {
             this.equipment = equipment;
             Log.d(App.TAG, "Adding equipment " + this.equipment.getModel());

             ImageView ivEquipmentFragment = (ImageView) getView().findViewById(R.id.ivEquipmentFragment);

             TextView textViewEquipmentBrand = (TextView) getView().findViewById(R.id.textViewEquipmentBrand);
             TextView textViewEquipmentModel = (TextView) getView().findViewById(R.id.textViewEquipmentModel);
             TextView textViewEquipmentITNumber = (TextView) getView().findViewById(R.id.textViewEquipmentITNumber);
             TextView textViewEquipmentAquired = (TextView) getView().findViewById(R.id.textViewEquipmentAquired);
             TextView textViewEquipmentDescription = (TextView) getView().findViewById(R.id.textViewEquipmentDescription);
             TextView textViewEquipmentStatus = (TextView) getView().findViewById(R.id.textViewEquipmentStatus);
             ImageView ivAssetImage = (ImageView) getView().findViewById(R.id.ivAssetImage);

             ivEquipmentFragment.setImageResource(equipment.getListItemImage());

             textViewEquipmentBrand.setText(equipment.getBrand());
             textViewEquipmentModel.setText(equipment.getModel());
             textViewEquipmentITNumber.setText(equipment.getIt_no());
             textViewEquipmentAquired.setText(equipment.getAquired());
             textViewEquipmentDescription.setText(equipment.getDescription());
             if (equipment.getImage() != null)
                 ivAssetImage.setImageBitmap(BitmapFactory.decodeByteArray(equipment.getImage(), 0, equipment.getImage().length));

             ImageButton btnEquipmentLoan = (ImageButton) getView().findViewById(R.id.btnEquipmentLoan);
             ImageButton btnEquipmentPicture = (ImageButton) getView().findViewById(R.id.btnEquipmentPicture);
             ImageButton btnEquipmentEdit = (ImageButton) getView().findViewById(R.id.btnEquipmentEdit);
             ImageButton btnEquipmentDelete = (ImageButton) getView().findViewById(R.id.btnEquipmentDelete);

             btnEquipmentLoan.setOnClickListener(this);
             btnEquipmentPicture.setOnClickListener(this);
             btnEquipmentEdit.setOnClickListener(this);
             btnEquipmentDelete.setOnClickListener(this);

             if (Login.isLoggedInAsAdminUser() && EquipmentStatus.isEquipmentAvailable(equipment)) {
                 textViewEquipmentStatus.setVisibility(View.VISIBLE);
                 textViewEquipmentStatus.setText(getString(R.string.fragment_Equipment_tvEquipmentStatusIn));
                 btnEquipmentLoan.setImageResource(R.drawable.equipment_out);
                 btnEquipmentLoan.setEnabled(true);
                 btnEquipmentEdit.setEnabled(true);
                 btnEquipmentDelete.setEnabled(true);
             } else if (Login.isLoggedInAsAdminUser() && !EquipmentStatus.isEquipmentAvailable(equipment)) {
                 textViewEquipmentStatus.setVisibility(View.VISIBLE);
                 textViewEquipmentStatus.setText(getString(R.string.fragment_Equipment_tvEquipmentStatusOut));
                 btnEquipmentLoan.setImageResource(R.drawable.equipment_out);
                 btnEquipmentLoan.setEnabled(true);
                 btnEquipmentEdit.setEnabled(true);
                 btnEquipmentDelete.setEnabled(true);
             } else {
                 textViewEquipmentStatus.setVisibility(View.INVISIBLE);
                 btnEquipmentLoan.setEnabled(false);
                 btnEquipmentEdit.setEnabled(false);
                 btnEquipmentDelete.setEnabled(false);
             }
         } catch (Exception e) {
             Log.d(App.TAG, e.toString());
         }
     }

     @Override
     public void onClick(View v) {
         try {
             ActivityMain activityMain = ((ActivityMain) getActivity());
             switch (v.getId()) {
                 case R.id.btnEquipmentLoan: // When clicking the loan/return button
                     // TODO: Feature - Change in/out image in ImageButton
                     if (EquipmentStatus.isEquipmentAvailable(equipment)) {
                         activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentLoan);

                         if (this.equipment != null)
                             activityMain.fragmentLoan.populateLoanFragmentWithData(this.equipment, activityMain);
                     } else {
                         AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                         alertDialog.setTitle(getString(R.string.dialog_loan_Title));
                         alertDialog.setMessage(getString(R.string.dialog_loan_Message_out) + activityMain.getCurrentlyViewedEquipment().getModel() + "?");

                         alertDialog.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 ActivityMain activityMain = ((ActivityMain) getActivity());
                                 WebAPI.doGetOpenLogEntries(activityMain, WebAPI.Method.GET_OPEN_LOG_ENTRIES_FOR_REGISTER_RESERVATION_IN, Login.getLoggedInUser().getU_id());
                                 activityMain.replaceFragmentContainerFragmentWith(((ActivityMain) getActivity()).fragmentList);
                                 activityMain.addToList(Category.getCategories());
                             }
                         });

                         alertDialog.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {

                             }
                         });

                         alertDialog.show();
                     }
                     break;
                 case R.id.btnEquipmentEdit: // When clicking the edit equipment button
                     Log.d(App.TAG, "Edit equipment" + this.equipment.getModel());
                     activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentAddEquipment);
                     activityMain.fragmentAddEquipment.editEquipment(this.equipment);

                     break;
                 case R.id.btnEquipmentDelete: // When clicking the delete equipment button
                     AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                     alertDialog.setTitle(getString(R.string.dialog_deleteequipment_Title));
                     alertDialog.setMessage(getString(R.string.dialog_deleteequipment_Message_out) + " " + activityMain.getCurrentlyViewedEquipment().getModel() + "?");

                     alertDialog.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             ActivityMain activityMain = ((ActivityMain) getActivity());
                             WebAPI.doDeleteEquipment(activityMain, equipment.getE_id());
                         }
                     });

                     alertDialog.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                            Log.d(App.TAG, "Clicked cancel");
                         }
                     });

                     alertDialog.show();
                     break;
             }
         } catch (Exception e) {
             Log.d(App.TAG, e.toString());
         }
     }
 }

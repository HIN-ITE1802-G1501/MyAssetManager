package no.hin.student.myassetmanager.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import no.hin.student.myassetmanager.Activities.ActivityMain;
import no.hin.student.myassetmanager.Classes.App;
import no.hin.student.myassetmanager.Classes.Category;
import no.hin.student.myassetmanager.Classes.Equipment;
import no.hin.student.myassetmanager.Classes.EquipmentStatus;
import no.hin.student.myassetmanager.Classes.Login;
import no.hin.student.myassetmanager.Classes.WebAPI;
import no.hin.student.myassetmanager.R;


public class FragmentAsset extends Fragment implements View.OnClickListener {
     private Equipment equipment;

     public FragmentAsset() {

     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_asset, container, false);
     }

    private void sendSMS(String phoneNumber, String message)
         {
             String SENT = "SMS_SENT";
             String DELIVERED = "SMS_DELIVERED";

             PendingIntent sentPI = PendingIntent.getBroadcast(App.getContext(), 0, new Intent(SENT), 0);

             PendingIntent deliveredPI = PendingIntent.getBroadcast(App.getContext(), 0, new Intent(DELIVERED), 0);

             //---when the SMS has been sent---
             App.getContext().registerReceiver(new BroadcastReceiver() {
                 @Override
                 public void onReceive(Context arg0, Intent arg1) {
                     switch (getResultCode()) {
                         case Activity.RESULT_OK:
                             Toast.makeText(App.getContext(), "SMS sent",
                                     Toast.LENGTH_SHORT).show();
                             break;
                         case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                             Toast.makeText(App.getContext(), "Generic failure",
                                     Toast.LENGTH_SHORT).show();
                             break;
                         case SmsManager.RESULT_ERROR_NO_SERVICE:
                             Toast.makeText(App.getContext(), "No service",
                                     Toast.LENGTH_SHORT).show();
                             break;
                         case SmsManager.RESULT_ERROR_NULL_PDU:
                             Toast.makeText(App.getContext(), "Null PDU",
                                     Toast.LENGTH_SHORT).show();
                             break;
                         case SmsManager.RESULT_ERROR_RADIO_OFF:
                             Toast.makeText(App.getContext(), "Radio off",
                                     Toast.LENGTH_SHORT).show();
                             break;
                     }
                 }
             }, new IntentFilter(SENT));

             //---when the SMS has been delivered---
             App.getContext().registerReceiver(new BroadcastReceiver() {
                 @Override
                 public void onReceive(Context arg0, Intent arg1) {
                     switch (getResultCode()) {
                         case Activity.RESULT_OK:
                             Toast.makeText(App.getContext(), "SMS delivered",
                                     Toast.LENGTH_SHORT).show();
                             break;
                         case Activity.RESULT_CANCELED:
                             Toast.makeText(App.getContext(), "SMS not delivered",
                                     Toast.LENGTH_SHORT).show();
                             break;
                     }
                 }
             }, new IntentFilter(DELIVERED));

             SmsManager sms = SmsManager.getDefault();
             sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
         }

     public void populateAssetFragmentWithAssetData(Equipment equipment, Login.UserRole userStatus) {
         try {
             this.equipment = equipment;

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

             if (userStatus == Login.UserRole.ADMIN_USER && EquipmentStatus.isEquipmentAvailable(equipment)) {
                 textViewEquipmentStatus.setVisibility(View.VISIBLE);
                 textViewEquipmentStatus.setText(getString(R.string.fragment_Equipment_tvEquipmentStatusIn));
                 btnEquipmentLoan.setImageResource(R.drawable.equipment_out);
                 btnEquipmentLoan.setEnabled(true);
                 btnEquipmentEdit.setEnabled(true);
                 btnEquipmentDelete.setEnabled(true);
             } else if (userStatus == Login.UserRole.ADMIN_USER && !EquipmentStatus.isEquipmentAvailable(equipment)) {
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
                 case R.id.btnEquipmentLoan:
                     if (EquipmentStatus.isEquipmentAvailable(equipment)) {
                         activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentLoan);

                         if (activityMain.getCurrentlyViewedEquipment() != null)
                             activityMain.fragmentLoan.populateLoanFragmentWithData(((ActivityMain) getActivity()).getCurrentlyViewedEquipment(), v.getContext());
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
                 case R.id.btnEquipmentPicture:
                     sendSMS("91755967", "test");
                     break;
                 case R.id.btnEquipmentEdit:
                     activityMain.replaceFragmentContainerFragmentWith(((ActivityMain) getActivity()).fragmentAddEquipment);
                     ((ActivityMain) getActivity()).fragmentAddEquipment.editEquipment(this.equipment);
                     break;
                 case R.id.btnEquipmentDelete:
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

/**
* This is the User fragment that displays user account information and lan history for the user
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/


package no.hin.student.myassetmanager.Fragments;


import android.app.AlertDialog;
 import android.app.Fragment;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.net.Uri;
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
 import no.hin.student.myassetmanager.Classes.DeviceAPI;
 import no.hin.student.myassetmanager.Classes.User;
 import no.hin.student.myassetmanager.Classes.WebAPI;
 import no.hin.student.myassetmanager.R;


public class FragmentUser extends Fragment implements View.OnClickListener {
     private User user;         // The user that we are viewing

    /**
     * Default constructor
     */
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
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_user, container, false);
         return view;
     }


    /**
     * Method for populating fragment with data
     *
     * @param user the user that we are viewing
     * @param context the context for AcivityMain
     */
     public void populateUserFragmentWithUserData(User user, Context context) {
         this.user = user;

         ImageView ivUserFragment = (ImageView) getView().findViewById(R.id.ivUserFragment);
         TextView textViewFullName = (TextView) getView().findViewById(R.id.textViewEquipmentBrand);
         TextView textViewUsername = (TextView) getView().findViewById(R.id.textViewEquipmentModel);
         TextView textViewPhoneNumber = (TextView) getView().findViewById(R.id.textViewEquipmentId);

         ImageButton btnUserCall = (ImageButton) getView().findViewById(R.id.btnUserCall);
         ImageButton btnUserSMS = (ImageButton) getView().findViewById(R.id.btnUserSMS);
         ImageButton btnUserEdit = (ImageButton) getView().findViewById(R.id.btnUserEdit);
         ImageButton btnUserActivate = (ImageButton) getView().findViewById(R.id.btnUserActivate);
         ImageButton btnUserDelete = (ImageButton) getView().findViewById(R.id.btnUserDelete);

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
         btnUserDelete.setOnClickListener(this);
     }

     @Override
     public void onClick(View v) {
         try {
             ActivityMain activityMain = ((ActivityMain) getActivity());
             switch (v.getId()) {
                 case R.id.btnUserCall: // When the user clicks the call button
                     // TODO: BUG - Crashes if device does not have SIM/CALL capability, add check
                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.user.getPhone()));
                     startActivity(intent);
                     break;
                 case R.id.btnUserSMS: // When user clicking the send message button
                     // TODO: BUG - Crashes if device does not have SIM/SMS capability, add check
                     Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                     smsIntent.setType("vnd.android-dir/mms-sms");
                     smsIntent.putExtra("address", this.user.getPhone());
                     smsIntent.putExtra("sms_body", getString(R.string.fragment_user_tvUserSMSMessage));
                     startActivity(smsIntent);
                     break;
                 case R.id.btnUserActivate: // WHen the user clicks activate button
                     this.user.setUser_activated(!user.isUser_activated());
                     ImageButton btnUserActivate = (ImageButton) v.findViewById(R.id.btnUserActivate);
                     btnUserActivate.setImageResource((this.user.isUser_activated()) ? R.drawable.user_deactivate : R.drawable.user_activate);

                     ImageView ivUserFragment = (ImageView) getView().findViewById(R.id.ivUserFragment);
                     ivUserFragment.setImageResource((this.user.isUser_activated()) ? R.drawable.user : R.drawable.user_notactive);

                     WebAPI.doUpdateUser(v.getContext(), this.user);

                     DeviceAPI.sendSMS(this.user.getPhone(), getString(R.string.fragment_user_ActivateSMS));
                     break;
                 case R.id.btnUserEdit: // When the user clicks the edit button
                     activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentRegister);
                     activityMain.fragmentRegister.populatEdit(this.user);
                     break;
                 case R.id.btnUserDelete: // When the user click the delete button
                     AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                     alertDialog.setTitle(getString(R.string.dialog_deleteuser_Title));
                     alertDialog.setMessage(getString(R.string.dialog_deleteuser_Message_out) + " " + this.user.getFirstname() + " " + this.user.getLastname() + "?");

                     alertDialog.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             ActivityMain activityMain = ((ActivityMain) getActivity());
                             WebAPI.doDeleteUser(activityMain, user.getId());
                         }
                     });

                     alertDialog.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
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

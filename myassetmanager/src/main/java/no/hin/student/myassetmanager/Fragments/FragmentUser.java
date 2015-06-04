package no.hin.student.myassetmanager.Fragments;


import android.app.AlertDialog;
 import android.app.Fragment;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.res.Configuration;
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
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_user, container, false);
         return view;
     }

     public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);

         // Get a layout inflater (inflater from getActivity() or getSupportActivity() works as well)
         LayoutInflater inflater = (LayoutInflater) App.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View newView = inflater.inflate(R.layout.fragment_user, null);
         // This just inflates the view but doesn't add it to any thing.
         // You need to add it to the root view of the fragment
         ViewGroup rootView = (ViewGroup) getView();
         // Remove all the existing views from the root view.
         // This is also a good place to recycle any resources you won't need anymore
         rootView.removeAllViews();
         rootView.addView(newView);
         // Viola, you have the new view setup
     }

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
                 case R.id.btnUserCall:
                     Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.user.getPhone()));
                     startActivity(intent);
                     break;
                 case R.id.btnUserSMS:
                     Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                     smsIntent.setType("vnd.android-dir/mms-sms");
                     smsIntent.putExtra("address", this.user.getPhone());
                     smsIntent.putExtra("sms_body", getString(R.string.fragment_user_tvUserSMSMessage));
                     startActivity(smsIntent);
                     break;
                 case R.id.btnUserActivate:
                     this.user.setUser_activated(!user.isUser_activated());
                     ImageButton btnUserActivate = (ImageButton) v.findViewById(R.id.btnUserActivate);
                     btnUserActivate.setImageResource((this.user.isUser_activated()) ? R.drawable.user_deactivate : R.drawable.user_activate);

                     ImageView ivUserFragment = (ImageView) getView().findViewById(R.id.ivUserFragment);
                     ivUserFragment.setImageResource((this.user.isUser_activated()) ? R.drawable.user : R.drawable.user_notactive);

                     WebAPI.doUpdateUser(v.getContext(), this.user);

                     DeviceAPI.sendSMS(this.user.getPhone(), getString(R.string.fragment_user_ActivateSMS));
                     break;
                 case R.id.btnUserEdit:
                     activityMain.replaceFragmentContainerFragmentWith(activityMain.fragmentRegister);
                     activityMain.fragmentRegister.populatEdit(this.user);
                     break;
                 case R.id.btnUserDelete:
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

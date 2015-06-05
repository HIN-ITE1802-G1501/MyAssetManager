/**
* This is the DeviceAPI class that contains methods for sending SMS, handling camera, storage etc..
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Classes;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import no.hin.student.myassetmanager.R;

public class DeviceAPI {

    /**
    * Method for sending SMS
    *
    * @param phoneNumber is the phonenumber we will send the SMS to.
    * @param message is the message that we will send in the SMS.
    */
     public static void sendSMS(String phoneNumber, String message) {
         final String SENT = "SMS_SENT";
         final String DELIVERED = "SMS_DELIVERED";

         PendingIntent sentPI = PendingIntent.getBroadcast(App.getContext(), 0, new Intent(SENT), 0);

         PendingIntent deliveredPI = PendingIntent.getBroadcast(App.getContext(), 0, new Intent(DELIVERED), 0);

         // When the SMS has been sent
         App.getContext().registerReceiver(new BroadcastReceiver() {
             @Override
             public void onReceive(Context arg0, Intent arg1) {
                 switch (getResultCode()) {
                     case Activity.RESULT_OK:
                         App.notifyUser(R.string.DEVICEAPI_SMS_SENT_RESULT_OK);
                         break;
                     case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                         App.notifyUser(R.string.DEVICEAPI_SMS_SENT_RESULT_ERROR_GENERIC_FAILURE);
                         break;
                     case SmsManager.RESULT_ERROR_NO_SERVICE:
                         App.notifyUser(R.string.DEVICEAPI_SMS_SENT_RESULT_ERROR_NO_SERVICE);
                         break;
                     case SmsManager.RESULT_ERROR_NULL_PDU:
                         App.notifyUser(R.string.DEVICEAPI_SMS_SENT_RESULT_ERROR_NULL_PDU);
                         break;
                     case SmsManager.RESULT_ERROR_RADIO_OFF:
                         App.notifyUser(R.string.DEVICEAPI_SMS_SENT_RESULT_ERROR_RADIO_OFF);
                         break;
                 }
             }
         }, new IntentFilter(SENT));

         // When the SMS has been delivered
         App.getContext().registerReceiver(new BroadcastReceiver() {
             @Override
             public void onReceive(Context arg0, Intent arg1) {
                 switch (getResultCode()) {
                     case Activity.RESULT_OK:
                         App.notifyUser(R.string.DEVICEAPI_SMS_DELIVERED_RESULT_OK);
                         break;
                     case Activity.RESULT_CANCELED:
                         App.notifyUser(R.string.DEVICEAPI_SMS_DELIVERED_RESULT_CANCELED);
                         break;
                 }
             }
         }, new IntentFilter(DELIVERED));
         SmsManager sms = SmsManager.getDefault();
         sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
     }
 }

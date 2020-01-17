package com.example.readsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class ReceptorSMS  extends BroadcastReceiver {
    private FunctionInterface functionInterface;

    public ReceptorSMS(FunctionInterface functionInterface) {
        this.functionInterface = functionInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ReceiverValue.NETWORK_CHANGE_RECEIVER)){
            networkChange(context);
        }
        else if (intent.getAction().equals(ReceiverValue.SMS_RECEIVER)){
            smsReceiver(context, intent);
        }
        else {

        }
    }

    private void networkChange(Context context){
        // Get system service object.
        Object systemServiceObj = context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Convert the service object to ConnectivityManager instance.
        ConnectivityManager connectivityManager = (ConnectivityManager) systemServiceObj;

        // Get network info object.
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check whether network is available or not.
        boolean networkIsAvailable = false;

        if (networkInfo != null) {
            if (networkInfo.isAvailable()) {
                networkIsAvailable = true;
            }
        }

        // Display message based on whether network is available or not.
        String networkMessage = "";
        if (networkIsAvailable) {
            networkMessage = "Network is available";
        } else {
            networkMessage = "Network is not available";
        }
        functionInterface.showContentMessage(networkMessage);
    }

    private void smsReceiver(Context  context, Intent intent){
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];

            String idMessage = "";
            String contentMessage = "";

            for (int i=0; i<pdus.length; i++){
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                idMessage = messages[i].getOriginatingAddress();
                contentMessage = messages[i].getMessageBody();

                Log.d("SMS RECEIVER Origen", idMessage);
                Log.d("SMS RECEIVER Contenido", contentMessage);
            }
            functionInterface.showContentMessage(idMessage + ": " + contentMessage);
        }
        else {
            Log.d("RECEIVER", "Bundle es nulo :(");
        }
    }
}

package com.example.readsms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;

public class MainActivity extends AppCompatActivity implements FunctionInterface{
    private TextView tvContent;
    private ReceptorSMS networkChangeReceiver = null;
    private static int REQUEST_CODE_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvContent = findViewById(R.id.tvContent);

        validatePermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.networkChangeReceiver!=null) {
            unregisterReceiver(this.networkChangeReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SMS){
            if (permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initReceiver();
            }
            else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)){
                    Toast.makeText(this, "Fuiste causa !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "No podras autocompletar el texto SMS", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
        }
    }

    private void initReceiver(){
        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add network connectivity change action.
        intentFilter.addAction(ReceiverValue.NETWORK_CHANGE_RECEIVER);
        intentFilter.addAction(ReceiverValue.SMS_RECEIVER);
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");

        // Set broadcast receiver priority.
        intentFilter.setPriority(100);

        // Create a network change broadcast receiver.
        networkChangeReceiver = new ReceptorSMS(this);

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void validatePermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_SMS);
        }
        else {
            initReceiver();
        }
    }

    public void setTextView(String content){
        tvContent.setText(content);
    }

    @Override
    public void showContentMessage(String message) {
        setTextView(message);
    }
}

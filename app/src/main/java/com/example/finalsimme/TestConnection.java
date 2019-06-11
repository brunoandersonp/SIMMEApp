package com.example.finalsimme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class TestConnection {

    private ConnectivityManager manager;
    private Context context;

    public TestConnection(Context context) {
        this.context = context;
    }

    public Boolean isNetworkAvailable(){

        try{
            manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;

            if(manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }

            return networkInfo != null && networkInfo.isConnected();
        } catch (NullPointerException ex){
            return false;
        }

    }

}

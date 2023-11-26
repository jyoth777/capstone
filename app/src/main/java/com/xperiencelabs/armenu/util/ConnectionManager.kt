package com.xperiencelabs.armenu.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager

class ConnectionManager { //returns a boolean value and tells us whether the device is connected to internet or not

    fun checkConnectivity(context: Context): Boolean {
        var connectivityManager =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork:NetworkInfo?=connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected!=null){
            return activeNetwork.isConnected
        }else{//if value returned by .. is null return false
            return false
        }
    }


}
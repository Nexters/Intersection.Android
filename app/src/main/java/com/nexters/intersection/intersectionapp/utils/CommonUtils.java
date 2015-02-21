package com.nexters.intersection.intersectionapp.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by BoBinLee on 2014-09-04.
 */
public class CommonUtils {
    public final static int INET4ADDRESS = 1;
    public final static int INET6ADDRESS = 2;

    public static String getLocalIpAddress(int type) {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        switch (type) {
                            case INET6ADDRESS:
                                if (inetAddress instanceof Inet6Address) {
                                    return inetAddress.getHostAddress().toString();
                                }
                                break;
                            case INET4ADDRESS:
                                if (inetAddress instanceof Inet4Address) {
                                    return inetAddress.getHostAddress().toString();
                                }
                                break;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static String getAndroidId(Context context) {
        String str = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        Log.d("android_id", "android_id : " + str);
        return str;
    }


    public static void getMyLocation(Context context, LocationListener locationListener){
        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

   /* *//* Class My Location Listener *//*
    public class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location loc){
            Log.d("MyLocationListener", "Latitudine = " + loc.getLatitude() + "Longitudine = " + loc.getLongitude());
//            String Text = "La mia posizione: "+"Latitudine = "+loc.getLatitude()+"Longitudine = "+loc.getLongitude();
        }
        @Override
        public void onProviderDisabled(String provider){
//            Toast.makeText( getApplicationContext(),"Gps disabilitato",Toast.LENGTH_SHORT ).show();
        }
        @Override
        public void onProviderEnabled(String provider){
//            Toast.makeText( getApplicationContext(),"Gps abilitato",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
        }
    }*/
}

package com.example.studentcatalogue.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.studentcatalogue.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object PermissionUtil {

    fun requestAccessFineLocationPermission(activity: AppCompatActivity, requestId: Int) {
        ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                requestId
        )
    }

    fun isAccessFineLocationGranted(context: Context): Boolean {
        return ContextCompat
                .checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showGPSNotEnabledDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
                .setTitle("GPS needed")
                .setMessage("Please turn on your GPS")
                .setCancelable(false)
                .setPositiveButton("OK"){
                    dialog, which ->
                    context.startActivity(object : Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS){

                    })
                }
                .show()

//        AlertDialog.Builder(context)
//                .setTitle(context.getString(R.string.enable_gps))
//                .setMessage(context.getString(R.string.required_for_this_app))
//                .setCancelable(false)
//                .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
//                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                }
//                .show()
    }

}
package com.emilyangirov.weatherproject.Fragments

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.hasPermission(name: String): Boolean {
    return(ContextCompat.checkSelfPermission(
            activity as AppCompatActivity, name)==PackageManager.PERMISSION_GRANTED)
}
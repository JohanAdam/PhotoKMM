package com.nyan.photokmm.android

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {

    private const val REQUEST_CODE_PERMISSION = 123

    fun checkPermission(activity: Activity, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, permission: String) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            REQUEST_CODE_PERMISSION
        )
    }

    fun isPermissionGranted(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        return if (requestCode == REQUEST_CODE_PERMISSION) {
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }
}

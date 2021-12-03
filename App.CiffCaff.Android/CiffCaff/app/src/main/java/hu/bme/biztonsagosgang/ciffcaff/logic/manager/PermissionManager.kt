package com.heartbit.heartbit.presentation.manager

import android.content.Context
import android.content.pm.PackageManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.flow.MutableSharedFlow

object PermissionManager {
    val permissions = MutableSharedFlow<List<Permission>>(1)

    fun isPermissionsGranted(context: Context, vararg permissions: String): List<Permission> {
        return permissions.map {
            Permission(name = it, granted = isPermissionGranted(context, it))
        }
    }
    fun isPermissionGranted(context: Context, name: String): Boolean {
        return context.checkSelfPermission(name) == PackageManager.PERMISSION_GRANTED
    }
    fun askForPermission(context: Context, vararg permissions: String) {
        Dexter.withContext(context)
                .withPermissions(*permissions)
                .withListener(getCompositePermissionsListener(context, *permissions))
                .withErrorListener (getErrorListener(context, *permissions))
                .check()
    }


    private fun getCompositePermissionsListener(
        context: Context, vararg requestedPermissions: String
    ): MultiplePermissionsListener = object : MultiplePermissionsListener {

        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
            val permissions = mutableListOf<Permission>()
            report?.grantedPermissionResponses?.forEach {
                permissions.add(Permission(name = it.permissionName, granted = true))
            }
            report?.deniedPermissionResponses?.forEach {
                permissions.add(
                    Permission(
                        name = it.permissionName,
                        granted = false,
                        isPermanentlyDenied = it.isPermanentlyDenied,
                        needsRationale = true
                    )
                )
            }
            this@PermissionManager.permissions.tryEmit(permissions)
        }

        override fun onPermissionRationaleShouldBeShown(
            permissions: MutableList<PermissionRequest>?,
            token: PermissionToken?
        ) {
            permissions?.map {
                Permission(
                    name = it.name,
                    granted = false,
                    needsRationale = true,
                )
            }?.let {
                this@PermissionManager.permissions.tryEmit(it)
            }
            token?.continuePermissionRequest()
        }
    }

    fun getErrorListener(context: Context, vararg permissions: String) : (error: DexterError) -> Unit  {
        return {error ->
            if (error == DexterError.NO_PERMISSIONS_REQUESTED) {
                permissions.map {
                    Permission(
                        name = it,
                        granted = false,
                        needsRationale = true
                    )
                }.let {
                    this.permissions.tryEmit(it)
                }
            }
        }
    }

    class Permission(
            val name: String,
            val granted: Boolean,
            val isPermanentlyDenied: Boolean = false,
            val needsRationale: Boolean = false
    )
}
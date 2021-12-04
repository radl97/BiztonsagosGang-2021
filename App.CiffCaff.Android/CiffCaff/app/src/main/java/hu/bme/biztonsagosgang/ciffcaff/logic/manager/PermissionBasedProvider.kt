package hu.bme.biztonsagosgang.ciffcaff.logic.manager

import android.Manifest
import android.content.Intent
import android.net.Uri
import com.heartbit.heartbit.presentation.manager.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import java.lang.Error
import kotlin.coroutines.CoroutineContext

abstract class PermissionBasedProvider() : CoroutineScope {
    final override val coroutineContext: CoroutineContext = Dispatchers.IO
    abstract val permissions : Array<String>

    private val providerPermissionState: ProviderPermissionState = ProviderPermissionState()
    val errorChannel = MutableSharedFlow<Error>(1)
    object PermissionDeniedError : Error()
    object RationalNeededError : Error()
    object PermanentlyDeniedError : Error()


    init {
        launch(coroutineContext){
            launch {
                PermissionManager.permissions.collect { grantedPermissions ->
                    if (grantedPermissions.size == permissions.size && grantedPermissions.map { it.name }
                            .containsAll(permissions.toList())) {
                        providerPermissionState.allGranted = grantedPermissions.all { it.granted }
                        providerPermissionState.containsRationalNeeded =
                            grantedPermissions.any { it.needsRationale }
                        providerPermissionState.containsPermanentlyDenied =
                            grantedPermissions.any { it.isPermanentlyDenied }

                        if (providerPermissionState.allGranted) doing()
                        else if (providerPermissionState.containsPermanentlyDenied) errorChannel.tryEmit(
                            PermanentlyDeniedError
                        )
                    }
                }
            }
        }
    }

    fun tryDoing(){
        when {
            providerPermissionState.allGranted -> doing()
            providerPermissionState.containsPermanentlyDenied -> errorChannel.tryEmit(
                PermanentlyDeniedError
            ) //THIS CHECKS AGAIN, AND ONLY THEN DOES IT GIVE PERMANENTLY DENIED TO AVOID INFINITE LOOP
            providerPermissionState.containsRationalNeeded -> errorChannel.tryEmit(RationalNeededError)
            providerPermissionState.containsNotGranted -> errorChannel.tryEmit(PermissionDeniedError)
        }
    }

    abstract fun doing()
}
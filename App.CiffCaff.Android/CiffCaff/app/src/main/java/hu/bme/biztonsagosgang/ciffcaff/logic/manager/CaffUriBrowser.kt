package hu.bme.biztonsagosgang.ciffcaff.logic.manager

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.heartbit.heartbit.presentation.manager.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Error
import kotlin.coroutines.CoroutineContext

const val  CAFF_TYPE_FILE = "application/octet-stream"

object CaffUriBrowserProvider : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    private val providerPermissionState: ProviderPermissionState = ProviderPermissionState()

    val requestChannel = MutableSharedFlow<Intent>(1)
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

                        if (providerPermissionState.allGranted) requestChannel.tryEmit(intent)
                        else if (providerPermissionState.containsPermanentlyDenied) errorChannel.tryEmit(
                            PermanentlyDeniedError
                        )
                    }
                }
            }
        }
    }


    val intent: Intent = Intent(
        Intent.ACTION_PICK
    ).apply {
        type = CAFF_TYPE_FILE
        action = Intent.ACTION_OPEN_DOCUMENT
    }

    fun callback(data: Intent?): Uri? {
        var caffUri: Uri? = null
        data?.data?.let { caffUri = Uri.parse(it.toString()) }
        return caffUri
    }

    fun click(){
        when {
            providerPermissionState.allGranted -> requestChannel.tryEmit(intent)
            providerPermissionState.containsPermanentlyDenied -> errorChannel.tryEmit(
                PermanentlyDeniedError
            ) //THIS CHECKS AGAIN, AND ONLY THEN DOES IT GIVE PERMANENTLY DENIED TO AVOID INFINITE LOOP
            providerPermissionState.containsRationalNeeded -> errorChannel.tryEmit(RationalNeededError)
            providerPermissionState.containsNotGranted -> errorChannel.tryEmit(PermissionDeniedError)
        }
    }
}
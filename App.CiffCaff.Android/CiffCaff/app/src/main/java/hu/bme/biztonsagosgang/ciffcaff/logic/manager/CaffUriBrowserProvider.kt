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

object CaffUriBrowserProvider : PermissionBasedProvider(){

    override val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    val requestChannel = MutableSharedFlow<Intent>(1)

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

    override fun doing() {
        requestChannel.tryEmit(intent)
    }
}
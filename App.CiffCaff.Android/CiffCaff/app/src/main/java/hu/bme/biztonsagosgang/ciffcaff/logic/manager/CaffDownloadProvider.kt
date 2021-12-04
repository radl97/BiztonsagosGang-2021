package hu.bme.biztonsagosgang.ciffcaff.logic.manager

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.IOException

import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

import java.io.FileOutputStream

import java.io.OutputStream

import java.io.InputStream

import java.io.File




object CaffDownloadProvider : PermissionBasedProvider(){

    override val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    val canStartDownloading = MutableSharedFlow<Boolean>(1)

    override fun doing() {
        canStartDownloading.tryEmit(true)
    }

    fun saveCaff(context: Context, body: ResponseBody){
        launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                var input: InputStream? = null
                var output: FileOutputStream? = null
                try {
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "caff34.caff")
                    val fileReader = ByteArray(4096)

                    output = FileOutputStream(file)
                    input = body.byteStream()

                    while (true) {
                        val read = input.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        read.let { output.write(fileReader, 0, read) }
                    }
                    output.flush()
                } catch (e: IOException) {
                    //throw e //todo
                } finally {
                    output?.close()
                    input?.close()
                }
            }
        }
    }
}
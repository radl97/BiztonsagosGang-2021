package hu.bme.biztonsagosgang.ciffcaff.logic.filedownloadupload

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface FileLoader {
    val download: Flow<ResponseBody?>
    fun uploadCaff(name: String, uri: Uri)
    fun downloadCaff(caffId: Int)
}
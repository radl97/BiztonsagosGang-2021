package hu.bme.biztonsagosgang.ciffcaff.domain.filedownloadupload

import android.net.Uri

interface FileLoader {
    suspend fun uploadCaff(name: String, uri: Uri)
    suspend fun downloadCaff(caffId: Int)
}
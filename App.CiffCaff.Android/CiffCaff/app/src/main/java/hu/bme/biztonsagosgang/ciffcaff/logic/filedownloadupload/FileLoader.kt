package hu.bme.biztonsagosgang.ciffcaff.logic.filedownloadupload

import android.net.Uri

interface FileLoader {
    fun uploadCaff(name: String, uri: Uri)
    fun downloadCaff(caffId: Int)
}
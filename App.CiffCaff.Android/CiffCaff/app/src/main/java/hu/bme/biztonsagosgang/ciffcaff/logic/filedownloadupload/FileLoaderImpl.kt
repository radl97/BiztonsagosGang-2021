package hu.bme.biztonsagosgang.ciffcaff.logic.filedownloadupload

import hu.bme.biztonsagosgang.ciffcaff.domain.api.APIService
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

import okhttp3.MultipartBody

import java.io.File

import android.net.Uri
import com.squareup.moshi.JsonEncodingException
import hu.bme.biztonsagosgang.ciffcaff.logic.manager.CAFF_TYPE_FILE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.lang.Exception


class FileLoaderImpl (
override val coroutineContext: CoroutineContext = Dispatchers.IO,
private val api: APIService,
private val appSettingsRepository: AppSettingsRepository
): FileLoader, CoroutineScope {

    override val download = MutableSharedFlow<ResponseBody?>(1)
    init {
        download.tryEmit(null)
    }

    override fun uploadCaff(name: String, uri: Uri) {
        launch(coroutineContext) {

            uri.path?.let { path ->
                try{

                    val filePath = path.substringAfter(':').substringBefore(':')
                    val file = File(filePath)

                    // create RequestBody instance from file
                    val requestFile = file.asRequestBody(CAFF_TYPE_FILE.toMediaType())

                    // MultipartBody.Part is used to send also the actual file name
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                    val description = name.toRequestBody(MultipartBody.FORM)

                    api.uploadCaff(description, body)
                    appSettingsRepository.emitNetworkMessage("Upload successful")
                }catch(e: Exception) {
                    if (e is java.net.SocketTimeoutException) {
                        appSettingsRepository.emitNetworkMessage("Connection timed out. Try again!")
                    } else if (e is JsonEncodingException){
                        appSettingsRepository.emitNetworkMessage("Upload successful")
                    } else {
                        appSettingsRepository.emitNetworkMessage("This caff has already been uploaded.")
                    }
                }
            }
        }
    }


    override fun downloadCaff(caffId: Int) {
        launch(coroutineContext) {
            try{

                val response = api.downloadCaff(caffId)
                val body = response.body()
                if(response.isSuccessful && body != null){
                    download.tryEmit(body)
                }else{
                    appSettingsRepository.emitNetworkMessage("Error while downloading file.")
                }

            }catch(e: Exception){
                appSettingsRepository.networkError(e)
            }

        }
    }

}
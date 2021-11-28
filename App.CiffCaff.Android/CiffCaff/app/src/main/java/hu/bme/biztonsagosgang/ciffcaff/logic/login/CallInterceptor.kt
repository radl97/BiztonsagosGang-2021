package hu.bme.biztonsagosgang.ciffcaff.logic.login

import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import okhttp3.Interceptor
import okhttp3.Request

class CallInterceptor (
    val appSettingsRepository: AppSettingsRepository
        ){
    fun createHeaderChangingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newRequest = chain.request().newBuilder()

            val credentials = appSettingsRepository.getCredentials()
            if (credentials != null) {
                newRequest.header("Cookie", credentials)
            }

            try {
                chain.proceed(newRequest.build() ?: chain.request())
            } catch (e: Exception) {
                chain.proceed(chain.request())
            }
        }
    }

    fun createHeaderCatcherInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request: Request = chain.request()
            val response = chain.proceed(request)
            val credentials = response.headers["Set-Cookie"]

            if(credentials!= null){
                appSettingsRepository.saveCredentials(credentials)
            }

            try {
                when (response.code) {
                    500 -> {
                        appSettingsRepository.emitNetworkErrorMessage("There seems to be an error in the server")
                    }
                    408 -> {
                        appSettingsRepository.emitNetworkErrorMessage("You are not connected to the internet.")
                    }
                    403 -> {
                        appSettingsRepository.emitNetworkErrorMessage("Login Error")
                    }
                }
            } catch (e: Exception) {
                //the code fetching did not work out, there is nothing we can do at this point
            }

            response
        }
    }

}
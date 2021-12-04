package hu.bme.biztonsagosgang.ciffcaff.logic.repository.login

import hu.bme.biztonsagosgang.ciffcaff.domain.networkdatasource.NetworkDatasource
import hu.bme.biztonsagosgang.ciffcaff.logic.models.isAdmin
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class LoginRepositoryImpl (
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val networkSource: NetworkDatasource,
    private val appSettingsRepository: AppSettingsRepository
): LoginRepository, CoroutineScope{
    override val loginError = MutableSharedFlow<String>(1)
    override val registerError = MutableSharedFlow<String>(1)

    override fun register(name: String, email: String, password1: String, password2: String) {
        launch(coroutineContext) {
            try{
                val registerResponse = networkSource.register(
                    name = name,
                    email = email,
                    password1 = password1,
                    password2 = password2
                )
                if(registerResponse.registerFailed && registerResponse.reason != null){
                    registerError.tryEmit(registerResponse.reason)
                }
                if(registerResponse.registerFailed.not()){
                    appSettingsRepository.emitNetworkMessage("Succesful registration!")
                }
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
                registerError.tryEmit("Could not register. Try again")
            }
        }
    }

    override fun login(email: String, password: String) {
        launch(coroutineContext) {
            try{
                val loginResponse = networkSource.login(
                    password = password,
                    email = email,
                )
                println(loginResponse.isAdmin())
                appSettingsRepository.changeIsAdmin(loginResponse.isAdmin())
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
                loginError.tryEmit("Incorrect username or password")
            }
        }
    }

    override fun onLogout() {
        //for later use
    }

}
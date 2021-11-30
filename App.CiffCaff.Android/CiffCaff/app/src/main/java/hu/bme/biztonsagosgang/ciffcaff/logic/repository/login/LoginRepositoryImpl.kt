package hu.bme.biztonsagosgang.ciffcaff.logic.repository.login

import hu.bme.biztonsagosgang.ciffcaff.android.admin
import hu.bme.biztonsagosgang.ciffcaff.domain.api.NetworkDatasource
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
    override val loginError = MutableSharedFlow<Boolean>(1)

    override fun register(name: String, email: String, password1: String, password2: String) {
        launch(coroutineContext) {
            try{
                networkSource.register(
                    name = name,
                    email = email,
                    password1 = password1,
                    password2 = password2
                )
            }catch (e: Exception){
                //todo
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
                appSettingsRepository.changeIsAdmin(loginResponse.isAdmin())
            }catch (e: Exception){
                throw e
            }
        }
    }

    override fun onLogout() {
        //for later use
    }

}
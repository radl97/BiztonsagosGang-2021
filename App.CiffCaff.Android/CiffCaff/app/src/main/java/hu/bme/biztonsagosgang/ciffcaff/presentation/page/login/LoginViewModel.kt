package hu.bme.biztonsagosgang.ciffcaff.presentation.page.login

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hu.bme.biztonsagosgang.ciffcaff.logic.login.LogoutHandler
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.home.LoginRepository
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class LoginViewModel(
    val loginRepository: LoginRepository,
    val logoutHandler: LogoutHandler,
    appSettingsRepository: AppSettingsRepository
) : BaseViewModel(appSettingsRepository) {
    //Actions
    init{
        viewModelScope.launch {
            UIActionFlow.collect{
                when (it) {
                    is Login -> {

                    }
                    is Register -> {

                    }
                }
            }
        }
    }

    val isLoggedIn = appSettingsRepository.isLoggedIn.distinctUntilChanged().asLiveData()
    val loginError = loginRepository.loginError.asLiveData()
}
package hu.bme.biztonsagosgang.ciffcaff.logic.login

import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.login.LoginRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs.CaffsRepository

class LogoutHandlerImpl(
    private val appSettingsRepository: AppSettingsRepository,
    private val loginRepository: LoginRepository,
    private val caffsRepository: CaffsRepository
) : LogoutHandler {
    override fun handleLogout() {
        appSettingsRepository.logout()
        loginRepository.onLogout()
        caffsRepository.onLogout()
    }
}
package hu.bme.biztonsagosgang.ciffcaff.logic.login

import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.home.LoginRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.projects.CaffsRepository

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
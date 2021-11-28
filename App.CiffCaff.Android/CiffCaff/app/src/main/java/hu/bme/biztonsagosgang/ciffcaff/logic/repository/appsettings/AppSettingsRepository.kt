package hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings

import kotlinx.coroutines.flow.Flow

//DO NOT PUT NETWORK DATASOURCE IN THIS, CIRCULAR DEPENDENCY
interface AppSettingsRepository {
    val isLoggedIn: Flow<Boolean>
    fun isLoggedIn(): Boolean
    fun getCredentials(): String?
    fun saveCredentials(credentials: String)

    val isAdmin: Flow<Boolean>
    fun isAdmin(): Boolean
    fun changeIsAdmin(isAdmin: Boolean)

    fun logout()

    val networkErrorMessage: Flow<String>
    fun emitNetworkErrorMessage(message: String)

}
package hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings

import hu.uni.corvinus.my.app.data.datasources.base.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


//DO NOT PUT NETWORK DATASOURCE IN IS, CIRCULAR DEPENDENCY
class AppSettingsRepositoryImpl(
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val localDataSource: DataSource<AppSettingsModel>
): AppSettingsRepository, CoroutineScope {
    override val isLoggedIn: Flow<Boolean> = localDataSource.output.map { it.credentials != null }
    override val isAdmin: Flow<Boolean> = localDataSource.output.map{it.isAdmin}
    override val networkErrorMessage = MutableSharedFlow<String>(1)

    override fun getCredentials(): String? {
        return localDataSource.getData()?.credentials
    }

    override fun saveCredentials(credentials: String) {
        localDataSource.saveData(getData().copy(credentials = credentials))
    }

    override fun changeIsAdmin(isAdmin: Boolean) {
        localDataSource.saveData(getData().copy(isAdmin = isAdmin))
    }

    override fun isLoggedIn(): Boolean {
        return localDataSource.getData()?.credentials != null
    }

    override fun isAdmin(): Boolean {
        return localDataSource.getData()?.isAdmin == true
    }

    override fun logout() {
        localDataSource.saveData(getData().copy(credentials = null, isAdmin = false))
    }

    override fun emitNetworkErrorMessage(message: String) {
        networkErrorMessage.tryEmit(message)
    }

    override fun networkError(e: Exception) {
        emitNetworkErrorMessage("Network Error")
        //throw e
    }

    private fun getData(): AppSettingsModel{
        return localDataSource.getData()?: AppSettingsModel()
    }
}
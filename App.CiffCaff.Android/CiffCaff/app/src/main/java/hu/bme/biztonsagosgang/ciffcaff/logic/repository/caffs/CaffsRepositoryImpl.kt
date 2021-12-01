package hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs

import hu.bme.biztonsagosgang.ciffcaff.domain.api.NetworkDatasource
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.uni.corvinus.my.app.data.datasources.base.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CaffsRepositoryImpl(
    override val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val networkSource: NetworkDatasource,
    private val localDataSource: DataSource<List<CaffItem>>,
    private val appSettingsRepository: AppSettingsRepository
) : CaffsRepository, CoroutineScope {
    override val caffList: Flow<List<CaffItem>> = localDataSource.output

    override fun fetchCaffsList(filter: String?) {
        launch(coroutineContext){
            try{
                localDataSource.saveData(networkSource.fetchCaffsList(filter))
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
            }
        }
    }

    override fun fetchCaffDetails(id: Int) {
        launch(coroutineContext){
            try{
                val caffWithDetails = networkSource.fetchCaffDetails(id)
                localDataSource.saveData(updateSingleFeedItemInList(getData(), caffWithDetails))
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
            }
        }
    }

    override fun deleteCaff(id: Int) {
        launch(coroutineContext) {
            try{
                networkSource.deleteCaff(id)
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
            }
        }
    }

    override fun deleteComment(caffId: Int, commentId: Int) {
        launch(coroutineContext) {
            try{
                networkSource.deleteComment(caffId = caffId, commentId = commentId)
                val caffWithDetails = networkSource.fetchCaffDetails(caffId)
                localDataSource.saveData(updateSingleFeedItemInList(getData(), caffWithDetails))
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
            }
        }
    }

    override fun updateComment(caffId: Int, commentId: Int, text: String) {
        launch(coroutineContext) {
            try{
                networkSource.updateComment(caffId = caffId, commentId = commentId, text = text)
                val caffWithDetails = networkSource.fetchCaffDetails(caffId)
                localDataSource.saveData(updateSingleFeedItemInList(getData(), caffWithDetails))
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
            }
        }
    }

    override fun addComment(caffId: Int, text: String) {
        launch(coroutineContext) {
            try{
                networkSource.addComment(caffId = caffId, text = text)
                val caffWithDetails = networkSource.fetchCaffDetails(caffId)
                localDataSource.saveData(updateSingleFeedItemInList(getData(), caffWithDetails))
            }catch (e: Exception){
                appSettingsRepository.networkError(e)
            }
        }
    }

    override fun onLogout() {
        localDataSource.saveData(emptyList())
    }


    //helper
    private fun getData() : List<CaffItem>{
        return localDataSource.getData() ?: emptyList<CaffItem>()
    }

    private fun updateSingleFeedItemInList(
        listOfNews: List<CaffItem>,
        caff: CaffItem,
    ): List<CaffItem> {
        val newList = listOfNews.toMutableList()
        val indexOfItem = newList.indexOfFirst {
            it.id == caff.id
        }
        if (indexOfItem != -1) {
                newList[indexOfItem] = caff
        } else {
            newList.add(caff)
        }
        return newList
    }
}
package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.logic.filedownloadupload.FileLoader
import hu.bme.biztonsagosgang.ciffcaff.logic.manager.CaffDownloadProvider
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs.CaffsRepository
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.viewmodels.BaseViewModel
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CommentCell
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.NewCommentCell
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CaffDetailsViewModel(
    caffId: Int,
    private val caffsRepository: CaffsRepository,
    private val fileLoader: FileLoader,
    appSettingsRepository: AppSettingsRepository,
) : BaseViewModel(appSettingsRepository) {

    //actions
    init{
        viewModelScope.launch {
            UIActionFlow.collect{
                when (it) {
                    is PageReloadRequest -> {
                        caffsRepository.fetchCaffDetails(caffId)
                    }
                    is DeleteCaff -> {
                        caffsRepository.deleteCaff(it.caffId)
                    }
                    is Comment -> {
                        caffsRepository.addComment(caffId = it.caffId, text = it.text)
                    }
                    is ModifyComment -> {
                        caffsRepository.updateComment(caffId = it.caffId, commentId = it.commentId, text = it.text)
                    }
                    is DeleteComment -> {
                        caffsRepository.deleteComment(caffId = it.caffId, commentId = it.commentId)
                    }
                    is DownloadCaff -> {
                        fileLoader.downloadCaff(caffId = it.caffId)
                        fragmentActionFlow.tryEmit(MakeToast("Downloading..."))
                    }
                }
            }
        }
    }

    //data
    val caff : LiveData<CaffItem> = liveData{
        caffsRepository.fetchCaffDetails(caffId)
        emitSource(
            caffsRepository.caffList.mapNotNull { caffList ->
                caffList.firstOrNull {
                    it.id == caffId
                }?.copy(
                    comments = emptyList()
                )
            }.distinctUntilChanged().asLiveData()
        )
    }

    val comments : LiveData<List<GenericListItem>> = liveData{
        caffsRepository.fetchCaffDetails(caffId)
        emitSource(
            caffsRepository.caffList.combine(appSettingsRepository.isAdmin){ caffList, isAdmin ->
                val list = mutableListOf<GenericListItem>(NewCommentCell(caffId))
                caffList.firstOrNull { caffItem ->
                    caffItem.id == caffId
                }?.comments?.forEach { comment ->
                    list.add(CommentCell(comment, isAdmin = isAdmin, caffId = caffId))
                }
                list
            }.asLiveData()
        )
    }

    fun isAdmin(): Boolean {
        return appSettingsRepository.isAdmin()
    }

    //caff download
    init{
        viewModelScope.launch {
            CaffDownloadProvider.canStartDownloading.drop(1).collect{
                fileLoader.downloadCaff(caffId)
            }
        }
    }

    init{
        viewModelScope.launch {
            CaffDownloadProvider.completionMessage.drop(1).collect{
                fragmentActionFlow.tryEmit(MakeToast(it))
            }
        }
    }

    val caffToSave = fileLoader.download.drop(1).mapNotNull{it}.asLiveData()
}
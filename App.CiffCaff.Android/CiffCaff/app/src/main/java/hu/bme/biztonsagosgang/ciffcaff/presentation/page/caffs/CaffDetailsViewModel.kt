package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.domain.filedownloadupload.FileLoader
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs.CaffsRepository
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.viewmodels.BaseViewModel
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CommentCell
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.NewCommentCell
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class CaffDetailsViewModel(
    caffId: Int,
    private val caffsRepository: CaffsRepository,
    private val fileLoader: FileLoader,
    appSettingsRepository: AppSettingsRepository,
) : BaseViewModel(appSettingsRepository) {

    init{
        viewModelScope.launch {
            UIActionFlow.collect{
                when (it) {
                    is PageReloadRequest -> {
                        caffsRepository.fetchCaffDetails(caffId)
                    }
                    is UpdateCaff -> {
                        caffsRepository.updateCaffDetails(it.caff)
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
                }
            }
        }
    }

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
}
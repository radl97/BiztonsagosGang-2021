package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.projects.CaffsRepository
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class CaffDetailsViewModel(
    caffId: Int,
    private val caffsRepository: CaffsRepository,
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
                }
            }.asLiveData()
        )
    }

}
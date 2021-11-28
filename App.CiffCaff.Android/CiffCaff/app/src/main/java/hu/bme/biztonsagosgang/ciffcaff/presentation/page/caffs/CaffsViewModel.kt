package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.logic.login.LogoutHandler
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.projects.CaffsRepository
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.viewmodels.BaseViewModel
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CaffCell
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CaffClickedAction
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CaffsViewModel(
    val caffsRepository: CaffsRepository,
    appSettingsRepository: AppSettingsRepository,
    val logoutHandler: LogoutHandler
): BaseViewModel(appSettingsRepository) {
    //Actions
    init{
        viewModelScope.launch {
            UIActionFlow.collect{
                when (it) {
                    is PageReloadRequest -> {
                        caffsRepository.fetchCaffsList()
                    }
                    is SearchRequest ->{
                        caffsRepository.fetchCaffsList(filter = it.term)
                    }
                    is CaffClickedAction -> {
                        fragmentActionFlow.emit(NavigateToCaffDetails(it.caffId))
                    }
                    is Logout ->{
                        logoutHandler.handleLogout()
                    }
                }
            }
        }
    }

    //Data
    val caffs : LiveData<List<GenericListItem>> = caffsRepository.caffList.map { cafflist ->
            cafflist.map { CaffCell(it) }
    }.asLiveData()

    val isLoggedIn = appSettingsRepository.isLoggedIn.asLiveData()

}
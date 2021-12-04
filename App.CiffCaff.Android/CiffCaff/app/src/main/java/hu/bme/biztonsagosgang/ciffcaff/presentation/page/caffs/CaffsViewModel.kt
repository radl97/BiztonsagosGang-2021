package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.logic.filedownloadupload.FileLoader
import hu.bme.biztonsagosgang.ciffcaff.logic.login.LogoutHandler
import hu.bme.biztonsagosgang.ciffcaff.logic.manager.CaffUriBrowserProvider
import hu.bme.biztonsagosgang.ciffcaff.logic.manager.PermissionBasedProvider
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs.CaffsRepository
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.viewmodels.BaseViewModel
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CaffCell
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CaffClickedAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CaffsViewModel(
    val caffsRepository: CaffsRepository,
    appSettingsRepository: AppSettingsRepository,
    private val fileLoader: FileLoader,
    val logoutHandler: LogoutHandler
): BaseViewModel(appSettingsRepository) {
    //Actions
    init{
        viewModelScope.launch {
            UIActionFlow.collect {
                when (it) {
                    is PageReloadRequest -> {
                        caffsRepository.fetchCaffsList()
                    }
                    is SearchRequest -> {
                        caffsRepository.fetchCaffsList(filter = it.term)
                    }
                    is CaffClickedAction -> {
                        fragmentActionFlow.emit(NavigateToCaffDetails(it.caffId))
                    }
                    is Logout -> {
                        logoutHandler.handleLogout()
                    }
                    is IntentCallback -> {
                        _chosenCaffUri.tryEmit(CaffUriBrowserProvider.callback(it.data))
                    }
                    is Browse -> {
                        CaffUriBrowserProvider.tryDoing()
                    }
                    is UploadCaff -> {
                        fileLoader.uploadCaff(name = it.caffTitle, uri = it.uri)
                        fragmentActionFlow.tryEmit(MakeToast("Uploading..."))
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


    //CaffShare
    val _chosenCaffUri = MutableSharedFlow<Uri?>(1)
    val chosenCaffUri = _chosenCaffUri.asLiveData()
    init{
        viewModelScope.launch{
            CaffUriBrowserProvider.errorChannel.collect{
                when(it){
                    PermissionBasedProvider.PermissionDeniedError -> {
                        fragmentActionFlow.tryEmit(AskForPermission(CaffUriBrowserProvider.permissions.toList()))
                    }
                    PermissionBasedProvider.RationalNeededError -> {
                        fragmentActionFlow.tryEmit(ShowPermissionDialog(isNeverAskAgain = false, CaffUriBrowserProvider.permissions.toList()))
                    }
                    PermissionBasedProvider.PermanentlyDeniedError ->{
                        fragmentActionFlow.tryEmit(ShowPermissionDialog(isNeverAskAgain = true, CaffUriBrowserProvider.permissions.toList()))
                    }
                }
            }
        }
    }
    init{
        viewModelScope.launch {
            CaffUriBrowserProvider.requestChannel.collect{
                fragmentActionFlow.tryEmit(StartIntent(intent = it))
            }
        }
    }
}
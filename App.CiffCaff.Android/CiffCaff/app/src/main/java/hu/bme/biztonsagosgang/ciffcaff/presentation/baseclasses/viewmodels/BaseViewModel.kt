package hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.DoNothing
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.FragmentAction
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.UIAction
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.MakeToast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

open class BaseViewModel(
    protected val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    val UIActionFlow = MutableSharedFlow<UIAction>(1)

    protected val fragmentActionFlow = MutableSharedFlow<FragmentAction>(1)
    val fragmentActionLiveData = fragmentActionFlow.asLiveData()

    init{
        viewModelScope.launch {
            appSettingsRepository.networkErrorMessage.drop(1).collect {
                fragmentActionFlow.tryEmit(MakeToast(it))
                fragmentActionFlow.tryEmit(DoNothing())
            }
        }
    }
}
package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import hu.bme.spacedumpling.worktimemanager.R
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CaffDetailsFragment : Fragment(
    R.layout.fragment_caff_details
) {

    private val args by navArgs<CaffDetailsFragmentArgs>()
    private val viewModel by viewModel<CaffDetailsViewModel>{
        parametersOf(args.caffId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setText()
        reloadPage()
        subscribeFragmentActions()
    }

    private fun setText() {

    }

    private fun reloadPage() {
        viewModel.UIActionFlow.tryEmit(PageReloadRequest())
    }

    private fun subscribeFragmentActions(){
        lifecycleScope.launch {
            viewModel.fragmentActionLiveData.observe(viewLifecycleOwner){
                when(it){
                    is MakeToast -> Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

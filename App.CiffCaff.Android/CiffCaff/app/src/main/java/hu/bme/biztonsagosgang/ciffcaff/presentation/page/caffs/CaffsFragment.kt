package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import hu.bitraptors.recyclerview.setupRecyclerView
import hu.bme.spacedumpling.worktimemanager.R
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CaffCell
import kotlinx.android.synthetic.main.fragment_caffs.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class CaffsFragment: Fragment(
    R.layout.fragment_caffs
) {
    private val viewModel by viewModel<ProjectViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpList()
        subscribeFragmentActions()
        reloadPage()
        setText()
        setUpSearch()
    }

    private fun setUpSearch(){
        search_container.addOnEndIconChangedListener{ _, _ ->
            viewModel.UIActionFlow.tryEmit(SearchRequest(search_term.text.toString()))
        }
    }

    private fun setText(){
        search_text.text = getString(R.string.caff_search_text)
    }

    private fun reloadPage(){
        viewModel.UIActionFlow.tryEmit(PageReloadRequest())
    }

    private fun setUpList(){
        this.setupRecyclerView(
            recyclerView = caff_list,
            items = viewModel.caffs,
            delegates = listOf(
                CaffCell.getDelegate(viewModel.UIActionFlow)
            ).toTypedArray(),
        )
    }

    private fun subscribeFragmentActions(){
        lifecycleScope.launch {
            viewModel.fragmentActionLiveData.observe(viewLifecycleOwner){
                when(it){
                    is MakeToast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                    is NavigateToCaffDetails -> findNavController().navigate(CaffsFragmentDirections.toCaffDetailsFragment(it.caffId))
                }
            }
        }
    }
}
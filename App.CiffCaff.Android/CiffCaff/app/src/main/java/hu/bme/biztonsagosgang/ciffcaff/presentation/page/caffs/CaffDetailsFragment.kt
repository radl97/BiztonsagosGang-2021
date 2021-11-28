package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.bitraptors.recyclerview.setupRecyclerView
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CommentCell
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.NewCommentCell
import hu.bme.biztonsagosgang.ciffcaff.util.loadUrl
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.util.gone
import hu.bme.biztonsagosgang.ciffcaff.util.visible
import kotlinx.android.synthetic.main.fragment_caff_details.*
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
        setupBackButton()
        subscribeToCaffData()
        setUpCommentList()
        setAdminSettings()
    }

    private fun setAdminSettings(){
        if(viewModel.isAdmin()){
            delete_button.visible()
        }else{
            delete_button.gone()
        }
        delete_button.setOnClickListener {
            viewModel.UIActionFlow.tryEmit(DeleteCaff(args.caffId))
        }
    }

    private fun subscribeToCaffData(){
        lifecycleScope.launch {
            viewModel.caff.observe(viewLifecycleOwner){
                caff_title.text = it.name
                caff_image.loadUrl(it.imageUrl, this@CaffDetailsFragment)
                caff_author.text = it.author.name
            }
        }
    }

    private fun setUpCommentList(){
        this.setupRecyclerView(
            recyclerView = comment_list,
            items = viewModel.comments,
            delegates = listOf(
                NewCommentCell.getDelegate(viewModel.UIActionFlow),
                CommentCell.getDelegate(viewModel.UIActionFlow)
            ).toTypedArray(),
        )
    }

    private fun setText() {
        comment_header.text=getString(R.string.cafff_details_comment)
    }

    private fun setupBackButton(){
        back_button.setOnClickListener {
            findNavController().navigate(CaffDetailsFragmentDirections.toCaffsFragment())
        }
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

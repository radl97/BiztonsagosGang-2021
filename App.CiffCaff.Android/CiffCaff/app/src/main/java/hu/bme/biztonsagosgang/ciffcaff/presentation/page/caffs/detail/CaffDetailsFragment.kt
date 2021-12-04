package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.heartbit.heartbit.presentation.manager.PermissionManager
import hu.bitraptors.recyclerview.setupRecyclerView
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.CommentCell
import hu.bme.biztonsagosgang.ciffcaff.presentation.cell.NewCommentCell
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.logic.manager.CaffDownloadProvider
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.*
import hu.bme.biztonsagosgang.ciffcaff.util.*
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
        subscribeToCaffData()
        setUpCommentList()
        setAdminSettings()
        setUpDownload()
        subscribeToCaffSaving()
    }

    private fun setUpDownload(){
        download_button.setOnClickListener {
            viewModel.UIActionFlow.tryEmit(DownloadCaff(args.caffId))
        }
    }

    private fun subscribeToCaffSaving(){
        viewModel.caffToSave.observe(viewLifecycleOwner){
            context?.let{ctx -> CaffDownloadProvider.saveCaff(ctx, it)}
        }
    }


    private fun setAdminSettings(){
        if(viewModel.isAdmin()){
            delete_button.visible()
        }else{
            delete_button.gone()
        }
        delete_button.setOnClickListener {
            viewModel.UIActionFlow.tryEmit(DeleteCaff(args.caffId))
            findNavController().popBackStack()
            findNavController().navigate(CaffDetailsFragmentDirections.toCaffsFragment())
        }
    }

    private fun subscribeToCaffData(){
        lifecycleScope.launch {
            viewModel.caff.observe(viewLifecycleOwner){ caffItem ->
                caff_title.text = caffItem.name
                caff_image.loadUrl(caffItem.imageUrl, this@CaffDetailsFragment)
                caff_author.text = caffItem.author?.name
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

    private fun reloadPage() {
        viewModel.UIActionFlow.tryEmit(PageReloadRequest())
    }

    private fun subscribeFragmentActions(){
        lifecycleScope.launch {
            viewModel.fragmentActionLiveData.observe(viewLifecycleOwner){ fragmentAction ->
                when(fragmentAction){
                    is MakeToast -> {
                        Toast.makeText(context, fragmentAction.text, Toast.LENGTH_LONG).show()
                    }
                    is AskForPermission -> {
                        context?.let { ctx ->
                            PermissionManager.askForPermission(ctx, *fragmentAction.permissions.toTypedArray())
                        }
                    }
                    is ShowPermissionDialog -> {
                        context?.let { ctx ->
                            showPermissionDialog(ctx, fragmentAction.isNeverAskAgain, *fragmentAction.permissions.toTypedArray())
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionDialog (
        context: Context,
        isNeverAskAgain: Boolean,
        vararg requestedPermissions: String
    ) {
        context.showDialog(
            R.string.download_caff_rationale_title,
            R.string.dowload_caff_rationale,
            if (isNeverAskAgain) R.string.open_settings else R.string.button_ok,
            R.string.button_cancel,
            onPositiveResponse = getOnPositiveResponse(
                isNeverAskAgain,
                context,
                *requestedPermissions
            )
        )
    }

    private fun getOnPositiveResponse(
        isNeverAskAgain: Boolean,
        context: Context,
        vararg requestedPermissions: String
    ): (() -> Unit) = {
        if (isNeverAskAgain) this@CaffDetailsFragment.openSetting()
        else PermissionManager.askForPermission(context, *requestedPermissions)
    }
}

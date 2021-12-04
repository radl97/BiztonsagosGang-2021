package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.heartbit.heartbit.presentation.manager.PermissionManager
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.fragments.BaseFragment
import hu.bme.biztonsagosgang.ciffcaff.util.openSetting
import hu.bme.biztonsagosgang.ciffcaff.util.showDialog
import kotlinx.android.synthetic.main.fragment_caff_upload.*
import kotlinx.android.synthetic.main.fragment_caffs.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class CaffUploadFragment: BaseFragment(R.layout.fragment_caff_upload) {
    private val viewModel by viewModel<CaffsViewModel>()
    private val caffChooserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK){
                viewModel.UIActionFlow.tryEmit(IntentCallback(activityResult.data))
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupText()
        subscribeFragmentActions()
        setOnClickBrowse()
        setUploadClickListener()
    }

    private fun setupText(){
        upload_caff_title.text = getString(R.string.upload_caff_title)
        choose_file_text.text = getString(R.string.choose_caff_text)
        give_title_text.text = getString(R.string.uploadcaff_title_of_caff)
        send_button.text = getString(R.string.caff_upload_upload)
    }

    private fun setOnClickBrowse(){
        browse_button.setOnClickListener {
           viewModel.UIActionFlow.tryEmit(Browse())
        }
    }

    private fun setUploadClickListener(){
        send_button.setOnClickListener {
            val uri = viewModel.chosenCaffUri.value
            val title = title.text.toString()
            if(title.isNotBlank() &&  uri != null){
                viewModel.UIActionFlow.tryEmit(UploadCaff(caffTitle = title, uri = uri))
            }else{
                Toast.makeText(context, "Invalis field(s)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeFragmentActions(){
        lifecycleScope.launch {
            viewModel.fragmentActionLiveData.observe(viewLifecycleOwner){fragmentAction ->
                when(fragmentAction){
                    is MakeToast -> {
                        Toast.makeText(context, fragmentAction.text, Toast.LENGTH_SHORT).show()
                    }
                    is AskForPermission -> {
                        context?.let { ctx ->
                            PermissionManager.askForPermission(ctx, *fragmentAction.permissions.toTypedArray())
                        }
                    }
                    is ShowPermissionDialog -> {
                        context?.let { ctx ->
                            if(PermissionManager.isPermissionsGranted(ctx, *fragmentAction.permissions.toTypedArray()).all { it.granted}){
                                PermissionManager.askForPermission(ctx, *fragmentAction.permissions.toTypedArray())
                            }else{
                                showPermissionDialog(ctx, fragmentAction.isNeverAskAgain, *fragmentAction.permissions.toTypedArray())
                            }
                        }
                    }
                    is StartIntent -> {
                        caffChooserLauncher.launch(fragmentAction.intent)
                    }
                }
            }
            viewModel.chosenCaffUri.observe(viewLifecycleOwner){
                uri.setText(it.toString())
            }
        }
    }

    private fun showPermissionDialog (
        context: Context,
        isNeverAskAgain: Boolean,
        vararg requestedPermissions: String
        ) {
            context.showDialog(
                R.string.upload_caff_rationale_title,
                R.string.upload_caff_rationale,
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
        if (isNeverAskAgain) this@CaffUploadFragment.openSetting()
        else PermissionManager.askForPermission(context, *requestedPermissions)
    }
}
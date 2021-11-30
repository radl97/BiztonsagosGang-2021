package hu.bme.biztonsagosgang.ciffcaff.presentation.page.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.MakeToast
import hu.bme.biztonsagosgang.ciffcaff.util.gone
import hu.bme.biztonsagosgang.ciffcaff.util.visible
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment: Fragment(
    R.layout.fragment_login
) {
    private val viewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTexts()
        subscribeFragmentActions()
        setUpMain()
        setUpRegister()
        setUpLogin()
        subscribeIsLoggedIn()
        showButtons()
    }


    private fun setUpLogin() {
        login_button_login.setOnClickListener {
            if (email_login.text != null && password_login.text != null) {
                viewModel.UIActionFlow.tryEmit(
                    Login(
                        email = email_login.text.toString(),
                        password = password_login.text.toString()
                    )
                )
            }else{
                Toast.makeText(context, getString(R.string.login_field_is_empty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpRegister(){
        register_button.setOnClickListener {
            if (name_register.text != null &&
                email_register.text != null &&
                password1_register.text != null &&
                password2_register.text != null
                    ) {
                viewModel.UIActionFlow.tryEmit(
                    Register(
                        name = name_register.text.toString(),
                        email = email_register.text.toString(),
                        password1 = password1_register.text.toString(),
                        password2 = password2_register.text.toString())
                    )
            }else{
                Toast.makeText(context, getString(R.string.login_field_is_empty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpMain(){
        buttons_login_button.setOnClickListener {
            showLogin()
        }
        buttons_register_button.setOnClickListener {
            showRegister()
        }
    }

    private fun subscribeIsLoggedIn(){
        lifecycleScope.launch {
            viewModel.isLoggedIn.observe(viewLifecycleOwner){ isLoggedIn ->
                if(isLoggedIn){
                    findNavController().navigate(LoginFragmentDirections.toCaffsFragment())
                }
            }
        }
    }

    private fun showButtons(){
        buttons.visible()
        login.gone()
        register.gone()
    }

    private fun showLogin(){
        buttons.gone()
        login.visible()
        register.gone()
    }

    private fun showRegister(){
        buttons.gone()
        login.gone()
        register.visible()
    }

    private fun setUpTexts(){
        caffka_logo.text = getString(R.string.caffka_logo_text)
        buttons_login_button.text = getString(R.string.login_login)
        buttons_register_button.text = getString(R.string.login_register)
        email_container_login.helperText = getString(R.string.login_email)
        password_container_login.helperText = getString(R.string.login_password)
        login_button_login.text = getString(R.string.login_login)
        name_container_register.helperText = getString(R.string.login_name)
        email_container_register.helperText = getString(R.string.login_email)
        password1_container_register.helperText = getString(R.string.login_password)
        password2_container_register.helperText = getString(R.string.login_confirm_password)
        register_button.text = getString(R.string.login_register)
    }


    private fun subscribeFragmentActions(){
        lifecycleScope.launch {
            viewModel.fragmentActionLiveData.observe(viewLifecycleOwner){
                when(it){
                    is MakeToast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
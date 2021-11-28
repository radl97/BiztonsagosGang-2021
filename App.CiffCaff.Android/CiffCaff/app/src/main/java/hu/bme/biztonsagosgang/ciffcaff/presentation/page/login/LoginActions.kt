package hu.bme.biztonsagosgang.ciffcaff.presentation.page.login

import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.UIAction

data class Login(val email: String, val password: String): UIAction
data class Register(
    val name: String,
    val email: String,
    val password1: String,
    val password2: String): UIAction
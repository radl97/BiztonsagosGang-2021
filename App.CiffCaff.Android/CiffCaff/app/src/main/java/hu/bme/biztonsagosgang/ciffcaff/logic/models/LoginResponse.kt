package hu.bme.biztonsagosgang.ciffcaff.logic.models

import hu.bme.biztonsagosgang.ciffcaff.android.admin

data class LoginResponse (
    val role: String?
)

fun LoginResponse.isAdmin(): Boolean{
    return role == admin
}
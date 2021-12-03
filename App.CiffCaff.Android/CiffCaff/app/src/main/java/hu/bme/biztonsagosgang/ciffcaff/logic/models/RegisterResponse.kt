package hu.bme.biztonsagosgang.ciffcaff.logic.models

data class RegisterResponse (
    val registerFailed: Boolean,
    val reason: String?
)
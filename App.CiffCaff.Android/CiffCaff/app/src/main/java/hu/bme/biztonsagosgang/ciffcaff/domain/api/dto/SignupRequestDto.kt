package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

data class SignupRequestDto(
    val email: String,
    val name: String,
    val password1: String,
    val password2: String
)
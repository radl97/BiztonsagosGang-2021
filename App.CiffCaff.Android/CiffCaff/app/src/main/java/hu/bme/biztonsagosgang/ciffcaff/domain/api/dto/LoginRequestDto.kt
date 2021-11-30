package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import com.squareup.moshi.Json

data class LoginRequestDto(
    val password: String,
    val username: String
)
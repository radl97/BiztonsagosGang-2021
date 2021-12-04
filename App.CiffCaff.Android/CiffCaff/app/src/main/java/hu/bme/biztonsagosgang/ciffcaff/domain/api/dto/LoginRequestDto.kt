package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import com.squareup.moshi.Json

data class LoginRequestDto(
    val password: String,
    @Json(name="email")
    val username: String
)
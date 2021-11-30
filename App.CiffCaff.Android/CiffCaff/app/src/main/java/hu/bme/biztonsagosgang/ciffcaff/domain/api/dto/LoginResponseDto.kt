package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import hu.bme.biztonsagosgang.ciffcaff.logic.models.LoginResponse

data class LoginResponseDto(
    val role: String?
)

fun LoginResponseDto.toLoginResponse(): LoginResponse{
    return LoginResponse(
        role = this.role
    )
}
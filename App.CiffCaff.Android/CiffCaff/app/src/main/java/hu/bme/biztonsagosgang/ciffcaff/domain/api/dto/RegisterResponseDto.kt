package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import hu.bme.biztonsagosgang.ciffcaff.logic.models.RegisterResponse

data class RegisterResponseDto (
    val success: Boolean,
    val reason: String?
        )

fun RegisterResponseDto.mapToModel(): RegisterResponse {
    return RegisterResponse(
        registerFailed = !this.success,
        reason = reason
    )
}
package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem

data class CaffListResponseDto(
    val caffs: List<CaffDto>
)

fun CaffListResponseDto.mapToModelList(): List<CaffItem>{
    return this.caffs.map { it.toModel() }
}
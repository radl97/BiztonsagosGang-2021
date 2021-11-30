package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import hu.bme.biztonsagosgang.ciffcaff.logic.models.Author
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem

data class CaffDto(
    val id: Int,
    val name: String,
    val number_of_comments: Int
)

fun CaffDto.toModel(): CaffItem{
    return CaffItem(
        id = this.id,
        name = this.name,
        numberOfComments = this.number_of_comments,
    )
}


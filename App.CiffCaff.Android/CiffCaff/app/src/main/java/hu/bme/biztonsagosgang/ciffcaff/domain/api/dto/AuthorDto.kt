package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import hu.bme.biztonsagosgang.ciffcaff.logic.models.Author

class AuthorDto (
    val id: Int,
    val name: String,
        )

fun AuthorDto.mapToModelAuthor() : Author{
    return Author(
        id = this.id,
        name = this.name,
    )
}
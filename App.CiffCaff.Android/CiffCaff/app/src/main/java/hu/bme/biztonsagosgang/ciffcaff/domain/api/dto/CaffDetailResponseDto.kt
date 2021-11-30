package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import hu.bme.biztonsagosgang.ciffcaff.logic.models.Author
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem

data class CaffDetailResponseDto (
    val id : Int,
    val preview_url : String,
    val name: String,
    val comments: List<CommentDto>,
    val number_of_comments: Int,
    val uploader: AuthorDto
)

fun CaffDetailResponseDto.toModel(): CaffItem{
    return CaffItem(
        id = this.id,
        name = this.name,
        numberOfComments = this.number_of_comments,
        imageUrl = this.preview_url,
        author = this.uploader.mapToModelAuthor(),
        comments = this.comments.map{it.toModel()}
    )
}
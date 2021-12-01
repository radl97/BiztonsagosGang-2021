package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

import hu.bme.biztonsagosgang.ciffcaff.logic.models.CommentItem


data class CommentDto (
    val id: Int,
    val text: String,
    val author: String
        )

fun CommentDto.toModel(): CommentItem{
    return CommentItem(
        id = this.id, text = this.text,
        author = this.author
    )
}
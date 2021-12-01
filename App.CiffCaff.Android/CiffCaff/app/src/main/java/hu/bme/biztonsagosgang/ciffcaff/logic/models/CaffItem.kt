package hu.bme.biztonsagosgang.ciffcaff.logic.models

data class CaffItem (
    val id: Int,
    val name: String?,
    val numberOfComments: Int,
    val imageUrl: String? = null,
    val author: Author? = null,
    val comments: List<CommentItem> = emptyList(),
)

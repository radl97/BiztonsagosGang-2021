package hu.bme.biztonsagosgang.ciffcaff.domain.api.dto

data class CaffNewCommentRequestDto (
    val text: String
)

fun String.toCaffNewCommentRequest(): CaffNewCommentRequestDto{
    return CaffNewCommentRequestDto(
        text = this
    )
}
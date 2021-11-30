package hu.bme.biztonsagosgang.ciffcaff.logic.models

data class CaffItem (
    val id: Int,
    val name: String,
    val numberOfComments: Int,
    val imageUrl: String? = null,
    val author: Author? = null,
    val comments: List<CommentItem> = emptyList(),
){
    //todo
    companion object{
        fun getDefault(title: String): CaffItem{
            val uth = Author("Berni mEster", 4)
            return CaffItem(
                id = 6,
                name = title,
                numberOfComments = 5,
                imageUrl = "https://http.cat/200.jpg",
                author = uth.copy(),
                comments = listOf(
                    CommentItem(1, author = uth.copy(), text = "Lol vicces"),
                    CommentItem(1, author = uth.copy(), text = "Lol vicces"),
                    CommentItem(1, author = uth.copy(), text = "Lol vicces"),
                    CommentItem(1, author = uth.copy(), text = "Lol vicces"),
                    CommentItem(1, author = uth.copy(), text = "Lol vicces")
                )
            )
        }
    }
}


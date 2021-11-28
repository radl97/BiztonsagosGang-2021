package hu.bme.biztonsagosgang.ciffcaff.logic.models

data class CaffItem (
    val id: Int,
    val name: String,
    val numberOfComments: Int,
    val imageUrl: String,
    val author: Author,
    val comments: List<Comment> = emptyList(),
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
                    Comment(1, author = uth.copy(), text = "Lol vicces"),
                    Comment(1, author = uth.copy(), text = "Lol vicces"),
                    Comment(1, author = uth.copy(), text = "Lol vicces"),
                    Comment(1, author = uth.copy(), text = "Lol vicces"),
                    Comment(1, author = uth.copy(), text = "Lol vicces")
                )
            )
        }
    }
}


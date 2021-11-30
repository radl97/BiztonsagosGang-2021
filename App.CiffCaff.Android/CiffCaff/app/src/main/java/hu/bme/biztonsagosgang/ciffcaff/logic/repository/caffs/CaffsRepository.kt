package hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs

import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import kotlinx.coroutines.flow.Flow

interface CaffsRepository {
    val caffList: Flow<List<CaffItem>>
    fun fetchCaffsList(filter: String? = null)
    fun fetchCaffDetails(id: Int)

    fun deleteCaff(id: Int)

    fun deleteComment(caffId: Int, commentId: Int)
    fun updateComment(caffId: Int, commentId: Int, text: String)
    fun addComment(caffId: Int, text: String)
    fun onLogout()
}
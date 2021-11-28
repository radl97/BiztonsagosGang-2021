package hu.bme.biztonsagosgang.ciffcaff.domain.api

import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.models.LoginReply

interface NetworkDatasource{
    suspend fun login(email: String, password: String): LoginReply
    suspend fun register(name: String, email: String, password1: String, password2: String)

    suspend fun fetchCaffsList(filter: String? = null): List<CaffItem>
    suspend fun fetchCaffDetails(id: Int): CaffItem
    suspend fun deleteCaff(id: Int)
    suspend fun updateCaffDetails(caffItem: CaffItem)

    suspend fun deleteComment(caffId: Int, commentId: Int)
    suspend fun updateComment(caffId: Int, commentId: Int, text: String)
    suspend fun addComment(caffId: Int, text: String)

}
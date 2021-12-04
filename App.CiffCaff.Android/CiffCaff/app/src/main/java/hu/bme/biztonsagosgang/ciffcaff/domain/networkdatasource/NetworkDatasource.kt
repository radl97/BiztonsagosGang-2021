package hu.bme.biztonsagosgang.ciffcaff.domain.networkdatasource

import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.models.LoginResponse
import hu.bme.biztonsagosgang.ciffcaff.logic.models.RegisterResponse

interface NetworkDatasource{
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun register(name: String, email: String, password1: String, password2: String): RegisterResponse

    suspend fun fetchCaffsList(filter: String? = null): List<CaffItem>
    suspend fun fetchCaffDetails(id: Int): CaffItem
    suspend fun deleteCaff(id: Int)

    suspend fun deleteComment(caffId: Int, commentId: Int)
    suspend fun updateComment(caffId: Int, commentId: Int, text: String)
    suspend fun addComment(caffId: Int, text: String)

}
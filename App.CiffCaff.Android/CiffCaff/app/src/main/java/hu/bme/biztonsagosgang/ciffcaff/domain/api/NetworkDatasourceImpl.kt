package hu.bme.biztonsagosgang.ciffcaff.domain.api

import hu.bme.biztonsagosgang.ciffcaff.domain.api.dto.*
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.models.LoginResponse

class NetworkDatasourceImpl (
    val api : APIService
    ): NetworkDatasource {
    override suspend fun login(email: String, password: String): LoginResponse {
        api.login(LoginRequestDto(password = password, username = email))//.toLoginResponse() todo
        return LoginResponse("todo")
    }

    override suspend fun register(
        name: String,
        email: String,
        password1: String,
        password2: String
    ) {
        api.signup(SignupRequestDto(
            email = email, name = name, password1 = password1, password2 = password2
        ))
    }

    override suspend fun fetchCaffsList(filter: String?) : List<CaffItem>{
        return api.fetchCaffList(filter ?: "").mapToModelList()
    }

    override suspend fun fetchCaffDetails(id: Int): CaffItem {
       return api.fetchCaffDetails(id).toModel()
    }

    override suspend fun deleteCaff(id: Int) {
       api.deleteCaff(id)
    }

    override suspend fun deleteComment(caffId: Int, commentId: Int) {
        api.deleteComment(caffId = caffId, commentId = commentId)
    }

    override suspend fun updateComment(caffId: Int, commentId: Int, text: String) {
        //todo
    }

    override suspend fun addComment(caffId: Int, text: String) {
        api.addComment(caffId = caffId, body = text.toCaffNewCommentRequest())
    }


}
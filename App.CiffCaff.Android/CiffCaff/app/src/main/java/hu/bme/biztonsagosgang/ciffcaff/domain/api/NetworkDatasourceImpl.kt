package hu.bme.biztonsagosgang.ciffcaff.domain.api

import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.models.LoginReply

class NetworkDatasourceImpl (
    val api : APIService
    ): NetworkDatasource {
    override suspend fun login(email: String, password: String): LoginReply {
        //api....
        return LoginReply(role = "admin")
    }

    override suspend fun register(
        name: String,
        email: String,
        password1: String,
        password2: String
    ) {
        //api....
    }

    override suspend fun fetchCaffsList(filter: String?) : List<CaffItem>{
        //api
        return if(filter == null)listOf(
            CaffItem.getDefault("CiffCaff1"),
            CaffItem.getDefault("CiffCaff2"),
            CaffItem.getDefault("CiffCaff3"),
            CaffItem.getDefault("CiffCaff4"),
            CaffItem.getDefault("CiffCaff5"),
        ) else listOf(
            CaffItem.getDefault("CiffCaff3"),
            CaffItem.getDefault("CiffCaff4"),
            CaffItem.getDefault("CiffCaff5"),
        )
    }

    override suspend fun fetchCaffDetails(id: Int): CaffItem {
       //api
        return CaffItem.getDefault("CiffCaff3")
    }

    override suspend fun deleteCaff(id: Int) {
       //api
    }

    override suspend fun updateCaffDetails(caffItem: CaffItem) {
       //api
    }

    override suspend fun deleteComment(caffId: Int, commentId: Int) {
        //api
    }

    override suspend fun updateComment(caffId: Int, commentId: Int, text: String) {
        //api
    }

    override suspend fun addComment(caffId: Int, text: String) {
        //api
    }


}
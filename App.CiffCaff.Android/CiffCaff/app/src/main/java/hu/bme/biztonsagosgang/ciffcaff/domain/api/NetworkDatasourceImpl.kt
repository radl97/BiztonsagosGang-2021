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
            CaffItem(id = 1, name = "CiffCaff1", comment = 5),
            CaffItem(id = 2, name = "CiffCaff2", comment = 5),
            CaffItem(id = 3, name = "CiffCaff3", comment = 5),
            CaffItem(id = 4, name = "CiffCaff4", comment = 5),
            CaffItem(id = 5, name = "CiffCaff5", comment = 5),
        ) else listOf(
            CaffItem(id = 3, name = "CiffCaff3", comment = 5),
            CaffItem(id = 4, name = "CiffCaff4", comment = 5),
            CaffItem(id = 5, name = "CiffCaff5", comment = 5),
        )
    }

    override suspend fun fetchCaffDetails(id: Int): CaffItem {
       //api
        return CaffItem(id = 3, name = "CiffCaff3", comment = 5)
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
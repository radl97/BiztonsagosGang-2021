package hu.bme.biztonsagosgang.ciffcaff.logic.repository.home

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    val loginError: Flow<Boolean>
    fun register(name: String, email: String, password1: String, password2: String)
    fun login (email: String, password: String)
    fun onLogout()
}
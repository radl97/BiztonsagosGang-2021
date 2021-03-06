package hu.bme.biztonsagosgang.ciffcaff.logic.repository.login

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    val loginError: Flow<String>
    val registerError: Flow<String>
    fun register(name: String, email: String, password1: String, password2: String)
    fun login (email: String, password: String)
    fun onLogout()
}
package hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings

data class AppSettingsModel (
    val credentials: String? =null,
    val isAdmin: Boolean = false
)
package hu.bme.biztonsagosgang.ciffcaff.logic.manager

data class ProviderPermissionState(
    var allGranted: Boolean = false,
    var containsRationalNeeded: Boolean = false,
    var containsPermanentlyDenied: Boolean = false
) {
    val containsNotGranted: Boolean
        get() {
            return !allGranted && !containsPermanentlyDenied && !containsPermanentlyDenied
        }
}
package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import android.content.Intent
import android.net.Uri
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.FragmentAction
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.UIAction


data class MakeToast(val text: String): FragmentAction

class PageReloadRequest(): UIAction
data class SearchRequest(val term: String): UIAction
class Logout(): UIAction

data class NavigateToCaffDetails(val caffId: Int): FragmentAction


data class DeleteCaff(val caffId: Int): UIAction
data class Comment(val caffId: Int, val text: String): UIAction
data class ModifyComment(val caffId: Int, val commentId: Int, val text: String): UIAction
data class DeleteComment(val caffId: Int, val commentId: Int): UIAction



data class IntentCallback(val data: Intent?): UIAction
data class UploadCaff(val caffTitle: String, val uri: Uri): UIAction

data class AskForPermission(val permissions: List<String>): FragmentAction
data class ShowPermissionDialog(val isNeverAskAgain: Boolean, val permissions: List<String>): FragmentAction
data class StartIntent(val intent: Intent): FragmentAction
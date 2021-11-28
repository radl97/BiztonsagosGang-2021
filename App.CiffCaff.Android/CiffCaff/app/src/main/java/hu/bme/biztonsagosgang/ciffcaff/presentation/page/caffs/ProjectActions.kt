package hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs

import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.FragmentAction
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.UIAction


data class MakeToast(val text: String): FragmentAction
data class NavigateToCaffDetails(val caffId: Int): FragmentAction

class PageReloadRequest(): UIAction
data class SearchRequest(val term: String): UIAction
data class UpdateCaff(val caff: CaffItem): UIAction
data class DeleteCaff(val caffId: Int): UIAction
data class Comment(val caffId: Int, val text: String): UIAction
data class ModifyComment(val caffId: Int, val commentId: Int, val text: String): UIAction
data class DeleteComment(val caffId: Int, val commentId: Int): UIAction
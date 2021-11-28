package hu.bme.biztonsagosgang.ciffcaff.presentation.cell

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.UIAction
import hu.bme.biztonsagosgang.ciffcaff.databinding.CellCommentNewBinding
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.Comment
import kotlinx.coroutines.flow.MutableSharedFlow

data class NewCommentCell(
    val caffId: Int,
) : GenericListItem {
    override fun getItemHash(): Int {
        return hashCode()
    }

    override fun getItemId(): String {
        return this::class.java.name+caffId
    }

    companion object {
        fun getDelegate(callback: MutableSharedFlow<UIAction>) =
            adapterDelegateViewBinding<NewCommentCell, GenericListItem, CellCommentNewBinding>(
                viewBinding = { layoutInflater, root ->
                    CellCommentNewBinding.inflate(layoutInflater, root, false)
                },
                block = {
                    bind {
                        binding.addCommentHeader.text = context.getString(R.string.new_comment_header_text)
                        binding.sendButton.setOnClickListener {
                            if(!binding.commentText.text.isNullOrBlank()){
                                callback.tryEmit(Comment(caffId = item.caffId, text = binding.commentText.text.toString()))
                                binding.commentText.setText("")
                            }
                        }
                    }
                }
            )
    }
}
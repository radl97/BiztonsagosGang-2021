package hu.bme.biztonsagosgang.ciffcaff.presentation.cell

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.logic.models.Comment
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.UIAction
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.DeleteComment
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.ModifyComment
import hu.bme.biztonsagosgang.ciffcaff.databinding.CellCommentBinding
import hu.bme.biztonsagosgang.ciffcaff.util.gone
import hu.bme.biztonsagosgang.ciffcaff.util.invisible
import hu.bme.biztonsagosgang.ciffcaff.util.visible
import kotlinx.android.synthetic.main.cell_comment.view.*
import kotlinx.coroutines.flow.MutableSharedFlow

class CommentCell(
    val model: Comment,
    val caffId: Int,
    val isAdmin: Boolean
) : GenericListItem {
    override fun getItemHash(): Int {
        return hashCode()
    }

    override fun getItemId(): String {
        return this::class.java.name+model.id
    }

    companion object {
        fun getDelegate(callback: MutableSharedFlow<UIAction>) =
            adapterDelegateViewBinding<CommentCell, GenericListItem, CellCommentBinding>(
                viewBinding = { layoutInflater, root ->
                    CellCommentBinding.inflate(layoutInflater, root, false)
                },
                block = {
                    bind {
                        binding.commentText.setText(item.model.text)
                        binding.author.text = item.model.author.name
                        if(item.isAdmin){
                            binding.deleteButton.apply {
                                visible()
                                setOnClickListener {
                                    callback.tryEmit(DeleteComment(caffId = item.caffId, commentId = item.model.id))
                                }
                            }

                            binding.editButton.apply {
                                visible()
                                setOnClickListener {
                                    invisible()
                                    binding.saveButton.visible()
                                    binding.commentText.isEnabled = true
                                }
                            }
                            binding.saveButton.apply {
                                setOnClickListener {
                                    invisible()
                                    binding.editButton.visible()
                                    binding.commentText.isEnabled = false
                                    callback.tryEmit(ModifyComment(caffId = item.caffId, commentId = item.model.id, text = binding.commentText.toString()))
                                }
                            }
                        }else{
                            binding.deleteButton.gone()
                            binding.editButton.gone()
                            binding.saveButton.gone()
                        }
                    }
                }
            )
    }
}
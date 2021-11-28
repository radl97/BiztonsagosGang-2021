package hu.bme.biztonsagosgang.ciffcaff.presentation.cell


import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.actions.UIAction
import hu.bme.biztonsagosgang.ciffcaff.R
import hu.bme.biztonsagosgang.ciffcaff.databinding.CellCaffBinding
import kotlinx.coroutines.flow.MutableSharedFlow

class CaffCell(
    val model: CaffItem
) : GenericListItem{
    override fun getItemHash(): Int {
        return hashCode()
    }

    override fun getItemId(): String {
        return this::class.java.name+model.id
    }

    companion object {
        fun getDelegate(callback: MutableSharedFlow<UIAction>) =
            adapterDelegateViewBinding<CaffCell, GenericListItem, CellCaffBinding>(
                viewBinding = { layoutInflater, root ->
                    CellCaffBinding.inflate(layoutInflater, root, false)
                },
                block = {
                    bind {
                        binding.caffTitle.text = item.model.name
                        binding.numOfComments.text = context.getString(R.string.caff_num_of_comments, item.model.numberOfComments.toString())
                        binding.caffAuthor.text = item.model.author.name
                        binding.root.setOnClickListener {
                            callback.tryEmit(CaffClickedAction(item.model.id))
                        }
                    }
                }
            )
    }
}

class CaffClickedAction(val caffId: Int): UIAction

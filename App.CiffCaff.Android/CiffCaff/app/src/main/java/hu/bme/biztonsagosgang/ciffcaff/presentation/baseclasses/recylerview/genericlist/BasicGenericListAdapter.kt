package hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.recylerview.genericlist

import android.util.Log
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import hu.bitraptors.recyclerview.genericlist.GenericListAdapter
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bitraptors.recyclerview.genericlist.fallbackDelegate

class BasicGenericListAdapter(
    vararg adapters: AdapterDelegate<List<GenericListItem>>
) : ListDelegationAdapter<List<GenericListItem>>(*adapters), GenericListAdapter {

    override val listItems: List<GenericListItem> get() = items
    override val itemsCount: Int get() = itemCount

    init {
        items = emptyList()
        setFallbackDelegate(fallbackDelegate)
    }

    override fun updateData(list: List<GenericListItem>) {
        items = list
        try {
            notifyDataSetChanged()
        } catch (e: IllegalStateException) {
            // strange error: Cannot call this method while RecyclerView is computing a layout or scrolling RecyclerView
            Log.d("recyclerview", "Failed to update list")
        }
    }
    override fun notifyItemUpdate(pos: Int) {
        notifyItemChanged(pos)
    }
    override fun addDelegates(vararg delegates: AdapterDelegate<List<GenericListItem>>) {
        delegates.forEach {
            delegatesManager.addDelegate(it)
        }
    }
    override fun setFallbackDelegate(delegate: AdapterDelegate<List<GenericListItem>>) {
        delegatesManager.fallbackDelegate = delegate
    }
}
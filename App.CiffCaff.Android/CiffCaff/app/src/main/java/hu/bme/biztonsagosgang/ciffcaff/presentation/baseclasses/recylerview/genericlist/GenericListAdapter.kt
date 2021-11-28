package hu.bitraptors.recyclerview.genericlist

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter



interface GenericListAdapter {

    val listItems: List<GenericListItem>

    val itemsCount: Int

    fun updateData(list: List<GenericListItem>)
    fun notifyItemUpdate(pos: Int)

    fun addDelegates(vararg delegates: AdapterDelegate<List<GenericListItem>>)
    fun setFallbackDelegate(delegate: AdapterDelegate<List<GenericListItem>>)
}
package hu.bitraptors.recyclerview

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import hu.bitraptors.recyclerview.genericlist.GenericListAdapter
import hu.bitraptors.recyclerview.genericlist.GenericListItem
import hu.bme.biztonsagosgang.ciffcaff.presentation.baseclasses.recylerview.genericlist.BasicGenericListAdapter


fun Fragment.setupRecyclerView(
    recyclerView: RecyclerView,
    manager: RecyclerView.LayoutManager? = null,
    items: LiveData<List<GenericListItem>>,
    vararg delegates: AdapterDelegate<List<GenericListItem>>,
): GenericListAdapter {
    //setUpAdapter
    val itemsListAdapter = recyclerView.setup(
        manager = manager, delegates = delegates
    )

    //subscribeOnItemChange
    items.observe(viewLifecycleOwner) {
        itemsListAdapter.updateData(it)
    }

    return itemsListAdapter
}

/**
 * Set-up recycler view delegates with click listener.
 * @property manager Set only if you didn't set it from xml
 */
fun RecyclerView.setup(
    manager: RecyclerView.LayoutManager? = null,
    viewPool: RecyclerView.RecycledViewPool? = null,
    vararg delegates: AdapterDelegate<List<GenericListItem>>
): GenericListAdapter {

    viewPool?.let { setRecycledViewPool(viewPool) }
    val itemsListAdapter = BasicGenericListAdapter(*delegates)
    // in case we provided manager from xml
    if (layoutManager == null && manager != null) layoutManager = manager
    adapter = itemsListAdapter
    return itemsListAdapter
}
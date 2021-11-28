package hu.bitraptors.recyclerview.genericlist

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import hu.bme.spacedumpling.worktimemanager.databinding.CellFallbackBinding

val fallbackDelegate =
    adapterDelegateViewBinding<GenericListItem, GenericListItem, CellFallbackBinding>(

        //~onCreateViewHolder() - creation of the cell layout
        viewBinding = { layoutInflater, root ->
            CellFallbackBinding.inflate(layoutInflater, root, false)
        },
        block = {}
    )
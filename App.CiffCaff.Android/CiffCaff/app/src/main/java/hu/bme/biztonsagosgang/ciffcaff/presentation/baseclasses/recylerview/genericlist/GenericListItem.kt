package hu.bitraptors.recyclerview.genericlist

interface GenericListItem {
    fun getItemId(): String = this::class.java.name
    fun getItemHash(): Int = hashCode()
}
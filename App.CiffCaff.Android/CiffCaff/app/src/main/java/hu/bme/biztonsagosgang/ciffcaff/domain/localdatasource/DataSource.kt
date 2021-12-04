package hu.uni.corvinus.my.app.data.datasources.base

import kotlinx.coroutines.flow.*


interface DataSource<T> {
    val isInitialised: Flow<Boolean>
    val output: Flow<T>
    fun saveData(data: T?)
    fun getData(): T?
}
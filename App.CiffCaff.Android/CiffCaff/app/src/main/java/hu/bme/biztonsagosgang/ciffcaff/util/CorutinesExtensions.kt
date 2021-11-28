package hu.bme.biztonsagosgang.ciffcaff.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

fun <T> SendChannel<T>.safeOffer(value: T) {
    try {
        if (!isClosedForSend) offer(value)
    } catch (e: Exception) {
    }
}

fun <T> Flow<T>.flowOnIO() = flowOn(Dispatchers.IO)
fun <T> Flow<T>.flowOnMain() = flowOn(Dispatchers.Main)
fun <T> Flow<T>.flowOnDefault() = flowOn(Dispatchers.Default)
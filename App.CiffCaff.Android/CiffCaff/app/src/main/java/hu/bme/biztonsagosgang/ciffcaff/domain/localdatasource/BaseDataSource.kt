package hu.uni.corvinus.my.app.data.datasources.base

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import hu.bme.biztonsagosgang.ciffcaff.util.flowOnDefault
import hu.bme.biztonsagosgang.ciffcaff.util.flowOnIO
import hu.bme.biztonsagosgang.ciffcaff.util.safeOffer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

typealias FromCache = Boolean

const val TAG_P = "PERSISTENCE"

abstract class BaseDataSource<T : Any>(
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : DataSource<T>, CoroutineScope {
    //Internal
    private val internalCache = ConflatedBroadcastChannel<Pair<FromCache, T?>>()
    final override val isInitialised = MutableStateFlow(false)
    override val output: Flow<T> =
        internalCache.asFlow().flowOnDefault().combine(isInitialised) { state, isInit ->
            Pair(state, isInit)
        }.filter { (_, isInit) ->
            isInit
        }.mapNotNull { (state, _) ->
            state.second
        }

    init {
        listenToCacheStream()
        launch(coroutineContext) { asyncInit() }
    }

    final override fun getData(): T? = internalCache.valueOrNull?.second
    final override fun saveData(data: T?) {
        internalCache.safeOffer(false to data)
    }

    suspend fun asyncInit() {
//        Log.d(TAG_P, "init DS")
        val cachedData = loadDataFromDisk()
        internalCache.safeOffer(true to cachedData)
    }

    private fun listenToCacheStream() {
        launch(coroutineContext) {
            internalCache.asFlow().flowOnIO().collect { (isFromCache, data) ->
                if (!isFromCache) persistDataToTheDisk(data)
                if (!isInitialised.value) isInitialised.value = true
            }
        }
    }

    protected abstract suspend fun loadDataFromDisk(): T?
    protected abstract suspend fun persistDataToTheDisk(data: T?)
}

inline fun <reified T : Any> createDataSourceForListBasedObjects(
    defaultValue: T?,
    typeForHandlingLists: ParameterizedType?,
    sharedPreferences: SharedPreferences,
    moshi: Moshi,
    TAG: String = "DataSource_${T::class.java.name}" + (typeForHandlingLists?.let { " = $it" }
        ?: "")
): BaseDataSource<T> {
    return handleDataSourceCreation_DontUseThisDirectly(
        defaultValue,
        typeForHandlingLists,
        sharedPreferences,
        moshi,
        TAG
    )
}

inline fun <reified T : Any> createDataSourceForNonListBasedObjects(
    defaultValue: T?,
    sharedPreferences: SharedPreferences,
    moshi: Moshi,
    TAG: String = "DataSource_${T::class.java.name}"
): BaseDataSource<T> {
    return handleDataSourceCreation_DontUseThisDirectly(
        defaultValue,
        null,
        sharedPreferences,
        moshi,
        TAG
    )
}


/**
 * Don't use this directly
 */
inline fun <reified T : Any> handleDataSourceCreation_DontUseThisDirectly(
    defaultValue: T?,
    typeForHandlingLists: ParameterizedType?,
    sharedPreferences: SharedPreferences,
    moshi: Moshi,
    TAG: String
): BaseDataSource<T> = object : BaseDataSource<T>() {
    override suspend fun loadDataFromDisk(): T? {
        val json = sharedPreferences.getString(TAG, null)
        //        Log.d(TAG_P, "load($TAG): $value")
        return try {
            if (json != null) {
                val loadedValue = if (typeForHandlingLists != null) {
                    moshi.adapter<T>(typeForHandlingLists).fromJson(json)
                } else {
                    moshi.adapter(T::class.java).fromJson(json)
                }
                //                Log.d(TAG_P, "load($TAG)")
                loadedValue
            } else {
                //                Log.d(TAG_P, "load defaultValue($TAG)")
                defaultValue
            }
        } catch (e: Exception) {
            //            Log.d(TAG_P, "load($TAG) crashed. Clearing cache", e)
            clearCache()
            defaultValue
        }
    }

    override suspend fun persistDataToTheDisk(data: T?) {
//        Log.d(TAG_P, "save($TAG): is null value: ${data == null}")
        if (data != null) {
            try {
                val adapter = moshi.adapter(T::class.java)
                val json = adapter.toJson(data)
                sharedPreferences.edit().putString(TAG, json).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            clearCache()
        }
    }

    private fun clearCache() {
        sharedPreferences.edit().remove(TAG).apply()
    }
}
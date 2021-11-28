package hu.bme.biztonsagosgang.ciffcaff

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.biztonsagosgang.ciffcaff.android.baseUrl
import hu.bme.biztonsagosgang.ciffcaff.android.sharedData
import hu.bme.biztonsagosgang.ciffcaff.domain.api.APIService
import hu.bme.biztonsagosgang.ciffcaff.domain.api.NetworkDatasource
import hu.bme.biztonsagosgang.ciffcaff.domain.api.NetworkDatasourceImpl
import hu.bme.biztonsagosgang.ciffcaff.domain.filedownloadupload.FileLoader
import hu.bme.biztonsagosgang.ciffcaff.domain.filedownloadupload.FileLoaderImpl
import hu.bme.biztonsagosgang.ciffcaff.logic.login.CallInterceptor
import hu.bme.biztonsagosgang.ciffcaff.logic.login.LogoutHandler
import hu.bme.biztonsagosgang.ciffcaff.logic.login.LogoutHandlerImpl
import hu.bme.biztonsagosgang.ciffcaff.logic.models.CaffItem
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsModel
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.appsettings.AppSettingsRepositoryImpl
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.login.LoginRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.login.LoginRepositoryImpl
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs.CaffsRepository
import hu.bme.biztonsagosgang.ciffcaff.logic.repository.caffs.CaffsRepositoryImpl
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.login.LoginViewModel
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.CaffDetailsViewModel
import hu.bme.biztonsagosgang.ciffcaff.presentation.page.caffs.CaffsViewModel
import hu.bme.biztonsagosgang.ciffcaff.util.CustomDateAdapter
import hu.uni.corvinus.my.app.data.datasources.base.DataSource
import hu.uni.corvinus.my.app.data.datasources.base.createDataSourceForListBasedObjects
import hu.uni.corvinus.my.app.data.datasources.base.createDataSourceForNonListBasedObjects
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

enum class DatasourceTypes{
    CAFF,
    APP_SETTINGS
}

val ciffCaffModule = module{
//NETWORK


    factory<Moshi> {
        Moshi.Builder().apply {
            add(KotlinJsonAdapterFactory())
            add(CustomDateAdapter())
        }
            .build()
    }
    factory { MoshiConverterFactory.create(get()) }

    single {
        CallInterceptor(
            appSettingsRepository = get()
        )
    }

    factory {
        val interceptorFactory: CallInterceptor= get()
        OkHttpClient.Builder()
            .addInterceptor(interceptorFactory.createHeaderChangingInterceptor())
            .addInterceptor(interceptorFactory.createHeaderCatcherInterceptor())
            .build()
    }

    single{
        Retrofit.Builder()
            .client(get()).apply {
                addConverterFactory(get<MoshiConverterFactory>())
            }
            .baseUrl(baseUrl)
            .build()
    }

    factory {
        val retrofit = get<Retrofit>()
        retrofit.create(APIService::class.java)
    }

    single<NetworkDatasource> {
        NetworkDatasourceImpl(api = get())
    }

//LOCAL DATA
    factory {
        get<Context>().getSharedPreferences(
            sharedData,
            Context.MODE_PRIVATE
        )
    }

    single<DataSource<List<CaffItem>>>(named(DatasourceTypes.CAFF.name)) {
        createDataSourceForListBasedObjects(
            defaultValue = null,
            typeForHandlingLists = Types.newParameterizedType(
                List::class.java,
                CaffItem::class.java
            ),
            sharedPreferences = get(),
            moshi = get(),
            TAG = DatasourceTypes.CAFF.name
        )
    }


    single<DataSource<AppSettingsModel>>(named(DatasourceTypes.APP_SETTINGS.name)) {
        createDataSourceForNonListBasedObjects(
            defaultValue = null,
            sharedPreferences = get(),
            moshi = get(),
            TAG = DatasourceTypes.APP_SETTINGS.name
        )
    }

//REPOSITORY
    single<CaffsRepository>{
        CaffsRepositoryImpl(
            networkSource = get(),
            localDataSource = get(named(DatasourceTypes.CAFF.name)),
            appSettingsRepository = get()
        )
    }
    single<LoginRepository>{
        LoginRepositoryImpl(
            networkSource = get(),
            appSettingsRepository = get()
        )
    }

    single<AppSettingsRepository>{
        AppSettingsRepositoryImpl(
            localDataSource = get(named(DatasourceTypes.APP_SETTINGS.name))
        )
    }

    single<LogoutHandler>{
        LogoutHandlerImpl(
            appSettingsRepository = get(),
            loginRepository = get(),
            caffsRepository = get()
        )
    }

    single<FileLoader>{
        FileLoaderImpl(
            api = get(),
            appSettingsRepository = get()
        )
    }


//PRESENTATION
    viewModel{
        CaffsViewModel(
            caffsRepository = get(),
            appSettingsRepository = get(),
            fileLoader = get(),
            logoutHandler = get()
        )
    }
    viewModel{ (caffId: Int) ->
        CaffDetailsViewModel(
            caffId = caffId,
            caffsRepository = get(),
            fileLoader = get(),
            appSettingsRepository = get()
        )
    }
    viewModel{
        LoginViewModel(
            loginRepository = get(),
            logoutHandler = get(),
            appSettingsRepository = get()
        )
    }

}
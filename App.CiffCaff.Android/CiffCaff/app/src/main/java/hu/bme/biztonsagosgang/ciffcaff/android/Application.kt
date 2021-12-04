package hu.bme.biztonsagosgang.ciffcaff.android

import android.app.Application
import hu.bme.biztonsagosgang.ciffcaff.ciffCaffModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(org.koin.core.logger.Level.ERROR)
            androidContext(this@Application)
            modules(ciffCaffModule)
        }
    }
}
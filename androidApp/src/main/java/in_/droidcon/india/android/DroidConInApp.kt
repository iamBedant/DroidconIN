package in_.droidcon.india.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import in_.droidcon.india.di.AppInfo
import in_.droidcon.india.di.initKoin
import in_.droidcon.india.features.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class DroidConInApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            module {
                single<Context> { this@DroidConInApp }
                viewModel { ScheduleViewModel(get(),get{ parametersOf("ScheduleViewModel")}) }
                single<SharedPreferences> {
                    get<Context>().getSharedPreferences("DROIDCON_IN_SETTINGS", Context.MODE_PRIVATE)
                }
                single<AppInfo> { AndroidAppInfo }
                single {
                    { Log.i("Startup", "Android app setting up...") }
                }
            }
        )
    }
}

object AndroidAppInfo : AppInfo {
    override val appId: String = BuildConfig.APPLICATION_ID
}

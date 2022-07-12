package in_.droidcon.india.di

import co.touchlab.kermit.Logger
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import in_.droidcon.india.db.DroidConIndiaDb
import in_.droidcon.india.features.schedule.ScheduleCallbackViewModel
import in_.droidcon.india.features.schedule.ScheduleViewModel
import io.ktor.client.engine.darwin.*
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

fun initKoinIos(
    userDefaults: NSUserDefaults,
    appInfo: AppInfo,
    doOnStartup: () -> Unit
): KoinApplication = initKoin(
    module {
        single<Settings> { AppleSettings(userDefaults) }
        single { appInfo }
        single { doOnStartup }
    }
)

actual val platformModule = module {
    single<SqlDriver> { NativeSqliteDriver(DroidConIndiaDb.Schema, "DroidConIndiaDb") }

    single { Darwin.create() }

    single { ScheduleCallbackViewModel(get(), getWith("ScheduleCallbackViewModel")) }
}

// Access from Swift to create a logger
@Suppress("unused")
fun Koin.loggerWithTag(tag: String) =
    get<Logger>(qualifier = null) { parametersOf(tag) }


@Suppress("unused") // Called from Swift
object KotlinDependencies : KoinComponent {
    fun getScheduleViewModel() = getKoin().get<ScheduleCallbackViewModel>()
}
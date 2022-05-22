package in_.droidcon.india.di

import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import in_.droidcon.india.db.DroidConIndiaDb
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            DroidConIndiaDb.Schema,
            get(),
            "DroidConIndiaDb"
        )
    }

    single<Settings> {
        AndroidSettings(get())
    }

    single {
        OkHttp.create()
    }
}

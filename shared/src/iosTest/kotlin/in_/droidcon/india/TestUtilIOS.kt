package in_.droidcon.india

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import in_.droidcon.india.db.DroidConIndiaDb

internal actual fun testDbConnection(): SqlDriver {
    val schema = DroidConIndiaDb.Schema
    return NativeSqliteDriver(
        DatabaseConfiguration(
            name = "kampkitdb",
            version = schema.version,
            create = { connection ->
                wrapConnection(connection) { schema.create(it) }
            },
            upgrade = { connection, oldVersion, newVersion ->
                wrapConnection(connection) { schema.migrate(it, oldVersion, newVersion) }
            },
            inMemory = true
        )
    )
}

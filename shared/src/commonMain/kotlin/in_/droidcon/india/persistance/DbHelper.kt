package in_.droidcon.india.persistance

import co.touchlab.kermit.Logger
import com.squareup.sqldelight.db.SqlDriver
import in_.droidcon.india.db.DroidConIndiaDb
import kotlinx.coroutines.CoroutineDispatcher

class DbHelper(
    sqlDriver: SqlDriver,
    private val log: Logger,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    private val db : DroidConIndiaDb = DroidConIndiaDb(sqlDriver)
}
package in_.droidcon.india.persistance

import co.touchlab.kermit.Logger
import com.squareup.sqldelight.db.SqlDriver
import in_.droidcon.india.db.DroidConIndiaDb
import in_.droidcon.india.features.schedule.model.Schedule
import in_.droidcon.india.features.schedule.model.Sessions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class DbHelper(
    sqlDriver: SqlDriver,
    private val log: Logger,
    private val backgroundDispatcher: CoroutineDispatcher
) {

    private val db : DroidConIndiaDb = DroidConIndiaDb(sqlDriver)

    fun selectAllSessions(): Flow<List<Sessions>> {
        TODO("Not yet implemented")
    }

    fun updateFavorite(sessionId: Int, favorite: Boolean) {
        TODO("Not yet implemented")
    }

    fun selectFavoriteSessions(): Flow<List<Sessions>> {
        TODO("Not yet implemented")
    }

    fun updateScheduleList(list: List<Schedule>) {
        TODO("Not yet implemented")
    }
}
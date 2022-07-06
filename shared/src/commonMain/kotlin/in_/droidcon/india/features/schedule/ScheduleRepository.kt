package in_.droidcon.india.features.schedule

import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import com.russhwolf.settings.Settings
import in_.droidcon.india.features.schedule.model.Sessions
import in_.droidcon.india.network.NetworkApi
import in_.droidcon.india.network.error.NetworkResponse
import in_.droidcon.india.persistance.DbHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class ScheduleRepository(
    private val dbHelper: DbHelper,
    private val settings: Settings,
    private val networkApi: NetworkApi,
    log: Logger,
    private val clock: Clock
) {

    private val log = log.withTag("ScheduleRepository")

    companion object {
        internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
    }

    init {
        ensureNeverFrozen()
    }

    fun getSessions(): Flow<List<Sessions>> = dbHelper.selectAllSessions()

    suspend fun updateFavorite(session: Sessions) {
        dbHelper.updateFavorite(session.sessionId, !session.favorite)
    }

    fun getFavoriteSessions(): Flow<List<Sessions>> = dbHelper.selectFavoriteSessions()

    suspend fun syncScheduleIfRequired() {
        when (val result = networkApi.fetchDroidconSchedule()) {
            is NetworkResponse.Success -> {
                log.d("Result Fetched Successfully")
                dbHelper.updateScheduleList(result.value.data?.schedule ?: emptyList())
            }
            is NetworkResponse.NetworkError -> {
                log.d("Network Error")
            }
            else -> {
                log.d("Generic Error")
            }
        }

    }

    private fun ifSyncRequired(): Boolean {
        TODO()
    }
}
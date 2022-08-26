package in_.droidcon.india.features.schedule

import co.touchlab.kermit.Logger
import in_.droidcon.india.base.ViewModel
import in_.droidcon.india.features.schedule.model.Session
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val logger: Logger
) : ViewModel() {

    private val mutableSessionState: MutableStateFlow<ScheduleViewState> =
        MutableStateFlow(ScheduleViewState(isLoading = true))

    val sessionState : StateFlow<ScheduleViewState> = mutableSessionState

    init {
        observeSessions()
    }

    fun refreshSchedule(): Job {
        return viewModelScope.launch {
            scheduleRepository.syncScheduleIfRequired()
        }
    }

    fun updateBookmark(it: String){

    }

    private fun observeSessions() {
        viewModelScope.launch {
            getAllSessions().collect { sessions ->
                logger.d(sessions.toString())
                mutableSessionState.update {
                    ScheduleViewState(
                        isLoading = false,
                        sessions = sessions,
                        isEmpty = sessions.isEmpty()
                    )
                }
            }
        }
    }

    private fun getAllSessions(): Flow<List<Session>> = scheduleRepository.getSessions()

}

data class ScheduleViewState(
    val sessions: List<Session>? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)
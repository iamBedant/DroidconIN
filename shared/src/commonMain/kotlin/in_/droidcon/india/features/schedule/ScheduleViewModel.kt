package in_.droidcon.india.features.schedule

import co.touchlab.kermit.Logger
import in_.droidcon.india.base.ViewModel
import in_.droidcon.india.features.schedule.model.Session
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val logger: Logger
) : ViewModel() {
    fun refreshSchedule(): Job {
        return viewModelScope.launch {
            scheduleRepository. syncScheduleIfRequired()
        }
    }

    fun observeSessions(){
        viewModelScope.launch {
            delay(4000)
            getAllSessions().collect{
                logger.d(it.toString())
            }
        }
    }

    private fun getAllSessions() : Flow<List<Session>> = scheduleRepository.getSessions()
}
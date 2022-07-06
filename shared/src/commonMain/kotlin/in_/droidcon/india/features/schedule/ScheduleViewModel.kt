package in_.droidcon.india.features.schedule

import co.touchlab.kermit.Logger
import in_.droidcon.india.base.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    logger: Logger
) : ViewModel() {
    fun refreshSchedule(): Job {
        return viewModelScope.launch {
            scheduleRepository. syncScheduleIfRequired()
        }
    }
}
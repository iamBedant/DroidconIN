package in_.droidcon.india.features.schedule

import co.touchlab.kermit.Logger
import in_.droidcon.india.base.CallbackViewModel


@Suppress("Unused") // Members are called from Swift
class ScheduleCallbackViewModel(
    scheduleRepository: ScheduleRepository,
    log: Logger
) : CallbackViewModel() {

    override val viewModel = ScheduleViewModel(scheduleRepository, log)

    val schedules = viewModel.sessionState.asCallbacks()

    fun refreshSchedule() {
        viewModel.refreshSchedule()
    }

    fun updateBookmark(sessionId: Int, isBookmarked: Boolean){
        viewModel.updateBookmark(sessionId, isBookmarked)
    }
}

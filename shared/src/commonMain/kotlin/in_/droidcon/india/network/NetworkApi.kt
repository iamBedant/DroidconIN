package in_.droidcon.india.network

import in_.droidcon.india.features.schedule.model.ScheduleResponse
import in_.droidcon.india.network.error.NetworkResponse

interface NetworkApi {
    suspend fun fetchDroidconSchedule() : NetworkResponse<ScheduleResponse>
}
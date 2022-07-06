package in_.droidcon.india.features.schedule.model

import in_.droidcon.india.network.error.ApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    @SerialName("data")
    var data: Data?,
    @SerialName("status_code")
    override val statusCode: Int,
    @SerialName("error")
    override val error: String?
) : ApiResponse()

@Serializable
data class Data(
    @SerialName("last_updated")
    var lastUpdated: Int,
    @SerialName("schedule")
    var schedule: ArrayList<Schedule>
)

@Serializable
data class Schedule(
    @SerialName("id")
    var id: Int,
    @SerialName("title")
    var title: String,
    @SerialName("type")
    var type: Int,
    @SerialName("time")
    var time: String,
    @SerialName("meridiem")
    var meridiem: String,
    @SerialName("speaker")
    var speaker: ArrayList<Speaker>,
    @SerialName("tags")
    var tags: ArrayList<Tags>,
    @SerialName("audi")
    var audi: Audi,
    @SerialName("day")
    var day: String
)

@Serializable
data class Tags(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,
)

@Serializable
data class Speaker(
    @SerialName("id")
    var id: Int,
    @SerialName("name")
    var name: String
)

@Serializable
data class Audi(
    @SerialName("id")
    var id: Int,
    @SerialName("name")
    var name: String
)
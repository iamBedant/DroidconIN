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
    var version: Int,
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
    @SerialName("speakers")
    var speaker: ArrayList<SpeakerNetwork>,
    @SerialName("tags")
    var tags: ArrayList<TagsNetwork>,
    @SerialName("audiName")
    var audi: String,
    @SerialName("day")
    var day: String
)

@Serializable
data class TagsNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("displayName")
    val name: String,
    @SerialName("colorCode")
    var colorCode: String

)

@Serializable
data class SpeakerNetwork(
    @SerialName("id")
    var id: Int,
    @SerialName("name")
    var name: String,
    @SerialName("imageUrl")
    var imageUrl: String,
    @SerialName("description")
    var description: String,
    @SerialName("twitterHandle")
    var twitterHandle: String
)
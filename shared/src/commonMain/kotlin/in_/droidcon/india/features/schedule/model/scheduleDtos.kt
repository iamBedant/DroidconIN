package in_.droidcon.india.features.schedule.model

data class Session(
    val id: Long,
    val title: String,
    val type: Long,
    val time: String,
    val meridiem: String,
    val day: String,
    val audiName: String?,
    val speakers : List<Speaker>,
    val tags: List<Tags>,
    val isBookmarked : Boolean = false
)

data class Speaker(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val description: String,
    val twitterHandle: String?,
)

data class Tags(
    val id: Long,
    val displayName: String,
    val colorCode: String,
)
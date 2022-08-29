package in_.droidcon.india.persistance

import `in`.droidcon.india.db.*
import co.touchlab.kermit.Logger
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import in_.droidcon.india.db.DroidConIndiaDb
import in_.droidcon.india.features.schedule.model.Schedule
import in_.droidcon.india.features.schedule.model.Session
import in_.droidcon.india.features.schedule.model.Speaker
import in_.droidcon.india.features.schedule.model.Tags
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DbHelper(
    sqlDriver: SqlDriver,
    private val log: Logger,
    private val backgroundDispatcher: CoroutineDispatcher
) {

    private val db: DroidConIndiaDb = DroidConIndiaDb(sqlDriver)

    suspend fun updateBoommark(sessionId: Int, isBookmarked: Boolean) {
        log.i("Updating bookmarkStatus for session $sessionId with $isBookmarked")
        db.transactionWithContext(backgroundDispatcher){
            db.bookmarksQueries.insert(BookmarksTb(isBookmarked = isBookmarked, sessionId= sessionId.toLong()))
        }
    }

    fun selectFavoriteSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    suspend fun deleteAll() {
        log.i { "Database Cleared" }
        db.transactionWithContext(backgroundDispatcher) {
            db.scheduleQueries.deleteAll()
            db.tagQueries.deleteAll()
            db.speakerQueries.deleteAll()
            db.bookmarksQueries.deleteAll()
        }
    }

    fun selectAllSessions(): Flow<List<Session>> {
        return combine(
            getAllSchedules(),
            getAllSchedulesWithTags(),
            getAllSchedulesWithSpeakers(),
            getAllBookmarks()
        ) { allSchedules, scheduleTags, scheduleSpeakers, bookmarks ->

            val sessionListWithSpeakers = getSessionListWithSpeakers(allSchedules,scheduleSpeakers)

            val sessionList = mutableListOf<Session>()
            val tagMap = scheduleTags.groupBy { it.id }
            sessionListWithSpeakers.forEach {
                val tagList = createTagList(tagMap[it.value.id] ?: emptyList())
                sessionList.add(
                    it.value.copy(tags = tagList, isBookmarked = getBookmarkValue(bookmarks, it.value.id))
                )
            }
            return@combine sessionList
        }
    }

    private fun getAllBookmarks(): Flow<List<BookmarksTb>> {
        return db.bookmarksQueries.selectAll().asFlow().mapToList()
    }

    private fun getBookmarkValue(bookmarks: List<BookmarksTb>, id: Long): Boolean {
        val bookmarkList = bookmarks.filter { it.sessionId ==id }

        return if (bookmarkList.isEmpty()){
            false
        } else {
            bookmarkList.first().isBookmarked
        }
    }


    private fun getSessionListWithSpeakers(
        allSchedules: List<ScheduleTb>,
        scheduleSpeakers: List<SchedulesWithSpeakers>
    ): HashMap<Long, Session> {
        val sessionListWithSpeakers = hashMapOf<Long, Session>()
        val speakerListMap = scheduleSpeakers.groupBy { it.id }
        allSchedules.forEach { schedule ->
            sessionListWithSpeakers[schedule.id] =
                parseToSession(schedule, speakerListMap[schedule.id] ?: emptyList())
        }
        return sessionListWithSpeakers
    }



    private fun createTagList(tagList: List<SchedulesWithTags>): List<Tags> {
        val tags = mutableListOf<Tags>()
        tagList.forEach {
            tags.add(
                Tags(id = it.id, displayName = it.displayName, colorCode = it.colorCode)
            )
        }
        return tags
    }

    private fun parseToSession(
        schedule: ScheduleTb,
        speakerList: List<SchedulesWithSpeakers>
    ): Session {
        return Session(
            id = schedule.id,
            title = schedule.title,
            type = schedule.type,
            time = schedule.time,
            meridiem = schedule.meridiem,
            day = schedule.day,
            audiName = schedule.audiName,
            speakers = createSpeakerList(speakerList),
            tags = listOf()
        )
    }

    private fun createSpeakerList(speakerList: List<SchedulesWithSpeakers>): List<Speaker> {
        val speakers = mutableListOf<Speaker>()
        speakerList.forEach {
            speakers.add(
                Speaker(
                    id = it.speakerId,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    description = it.description,
                    twitterHandle = it.twitterHandle
                )
            )
        }
        return speakers
    }

    private fun getAllSchedules(): Flow<List<ScheduleTb>> {
        return db.scheduleQueries.selectAll().asFlow().mapToList()
    }


    private fun getAllSchedulesWithSpeakers(): Flow<List<SchedulesWithSpeakers>> {
        return db.scheduleQueries.schedulesWithSpeakers()
            .asFlow()
            .mapToList()
    }

    private fun getAllSchedulesWithTags(): Flow<List<SchedulesWithTags>> {
        return db.scheduleQueries.schedulesWithTags()
            .asFlow()
            .mapToList()
    }

    suspend fun updateScheduleList(list: List<Schedule>) {
        val scheduleList = mutableListOf<ScheduleTb>()
        val speakerList = mutableListOf<SpeakerTb>()
        val scheduleSpeakerList = mutableListOf<Schedule_Speaker>()
        val tagList = mutableListOf<TagTb>()
        val scheduleTagList = mutableListOf<Schedule_Tags>()

        for (schedule in list) {
            scheduleList.add(
                ScheduleTb(
                    id = schedule.id.toLong(),
                    title = schedule.title,
                    type = schedule.type.toLong(),
                    time = schedule.time,
                    meridiem = schedule.meridiem,
                    day = schedule.day,
                    audiName = schedule.audi
                )
            )

            for (speaker in schedule.speaker) {
                speakerList.add(
                    SpeakerTb(
                        id = speaker.id.toLong(),
                        name = speaker.name,
                        imageUrl = speaker.imageUrl,
                        description = speaker.description,
                        twitterHandle = speaker.twitterHandle
                    )
                )
                scheduleSpeakerList.add(
                    Schedule_Speaker(
                        scheduleId = schedule.id.toLong(),
                        speakerId = speaker.id.toLong()
                    )
                )
            }

            for (tag in schedule.tags) {
                tagList.add(
                    TagTb(
                        id = tag.id.toLong(),
                        displayName = tag.name,
                        colorCode = tag.colorCode
                    )
                )
                scheduleTagList.add(
                    Schedule_Tags(
                        scheduleId = schedule.id.toLong(),
                        tagId = tag.id.toLong()
                    )
                )
            }
        }

        db.transactionWithContext(backgroundDispatcher) {
            scheduleList.forEach { scheduleTb ->
                db.scheduleQueries.insert(scheduleTb)
            }
            speakerList.forEach { speakerTb ->
                db.speakerQueries.insert(speakerTb)
            }
            tagList.forEach { tagTb ->
                db.tagQueries.insert(tagTb)
            }
            scheduleTagList.forEach { scheduleTags ->
                db.schedule_TagsQueries.insert(scheduleTags)
            }
            scheduleSpeakerList.forEach { scheduleSpeaker ->
                db.schedule_SpeakerQueries.insert(scheduleSpeaker)
            }
        }
    }
}
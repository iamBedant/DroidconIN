package in_.droidcon.india.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import co.touchlab.kermit.Logger
import in_.droidcon.india.android.ui.theme.DroidconInTheme
import in_.droidcon.india.di.injectLogger
import in_.droidcon.india.features.schedule.ScheduleViewModel
import in_.droidcon.india.features.schedule.ScheduleViewState
import in_.droidcon.india.features.schedule.model.Session
import in_.droidcon.india.features.schedule.model.Speaker
import in_.droidcon.india.features.schedule.model.Tags
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent


class MainActivity : ComponentActivity(), KoinComponent {

    private val logger: Logger by injectLogger("MainActivity")
    private val viewModel: ScheduleViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroidconInTheme {
                MainScreen(viewModel, logger)
            }
        }
    }
}


@Composable
fun MainScreen(
    viewModel: ScheduleViewModel,
    logger: Logger
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleAwareSessionsFlow = remember(viewModel.sessionState, lifecycleOwner) {
        viewModel.sessionState.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val sessionState by lifecycleAwareSessionsFlow.collectAsState(viewModel.sessionState.value)
    MainScreenContent(
        sessionState = sessionState,
        onRefresh = { viewModel.refreshSchedule() },
        onSuccess = { data -> logger.v { "View updating with ${data.size} sessions" } },
        onError = { exception -> logger.e { "Displaying error: $exception" } },
        onBookmark = { sessionId, isBookmarked -> viewModel.updateBookmark(sessionId, isBookmarked) }
    )

}

@Composable
fun MainScreenContent(
    sessionState: ScheduleViewState,
    onRefresh: () -> Unit = {},
    onSuccess: (List<Session>) -> Unit = {},
    onError: (String) -> Unit = {},
    onBookmark: (Int, Boolean) -> Unit
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {

        if(sessionState.isEmpty){
            Empty()
        }
        val sessions = sessionState.sessions

        if(sessions != null){
            SessionListContent(sessions, onBookmark)
        }
    }
}

@Composable
fun Empty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.empty_sessions))
    }
}

@Composable
fun SessionListContent( sessions: List<Session>, onBookmark: (Int, Boolean) -> Unit) {
    LazyColumn{
        items(sessions) { session ->
            SessionRow(session = session)
            SpeakerView(session.speakers)
            TagsView(session.tags)
            if(session.isBookmarked){
                Button(onClick = { onBookmark(session.id.toInt(), false) }) {
                    Text("Remove")
                }
            } else {
                Button(onClick = { onBookmark(session.id.toInt(), true) }) {
                    Text("Add")
                }
            }
            Divider()
        }
    }
}

@Composable
fun SessionRow(session: Session) {
        Row(
            Modifier
                .clickable { }
                .padding(10.dp)
        ) {
            Text(session.title, Modifier.weight(1F))
        }
}

@Composable
fun TagsView(tags: List<Tags>) {
    LazyRow{
        items(tags){ tag ->
            Text(tag.displayName)
        }

    }
}

@Composable
fun SpeakerView(speakers: List<Speaker>) {
    LazyRow{
        items(speakers){ speaker ->
            Text(speaker.name)
        }
    }
}

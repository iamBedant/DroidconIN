package in_.droidcon.india.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import in_.droidcon.india.android.ui.theme.DroidconInTheme
import in_.droidcon.india.di.injectLogger
import in_.droidcon.india.features.schedule.ScheduleViewModel
import kotlinx.coroutines.delay
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
        viewModel.refreshSchedule()

        viewModel.observeSessions()
    }
}



@Composable
fun MainScreen(
    viewModel: ScheduleViewModel,
    logger: Logger
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {

    }
}

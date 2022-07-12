package in_.droidcon.india.base

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

actual abstract class ViewModel {
    actual val viewModelScope = MainScope()
    protected actual open fun onCleared() {
    }
    fun clear() {
        onCleared()
        viewModelScope.cancel()
    }
}

abstract class CallbackViewModel {
    protected abstract val viewModel: ViewModel

    /**
     * Create a [FlowAdapter] from this [Flow] to make it easier to interact with from Swift.
     */
    fun <T : Any> Flow<T>.asCallbacks() =
        FlowAdapter(viewModel.viewModelScope, this)

    @Suppress("Unused") // Called from Swift
    fun clear() = viewModel.clear()
}

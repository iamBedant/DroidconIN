package in_.droidcon.india.network.error

import co.touchlab.kermit.Logger
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.SerializationException


inline fun <reified T : ApiResponse> getApiError(e: Throwable): NetworkResponse<T> {
    return when (e) {
        is IOException -> NetworkResponse.NetworkError
        is ServerResponseException -> NetworkResponse.ServerError
        is SerializationException -> {
            Logger.e("SerializationException: ${e.message}")
            NetworkResponse.GenericError
        }
        else -> NetworkResponse.GenericError
    }
}
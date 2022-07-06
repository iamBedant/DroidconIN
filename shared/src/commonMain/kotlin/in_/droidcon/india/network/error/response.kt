package in_.droidcon.india.network.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
abstract class ApiResponse {
    @SerialName("status_code" ) abstract val statusCode : Int
    @SerialName("error"       ) abstract val error      : String?
}

sealed class NetworkResponse<out L> {
    data class Success<L : ApiResponse>(val value: L) : NetworkResponse<L>()
    data class Error(val error: String) : NetworkResponse<Nothing>()
    object NetworkError : NetworkResponse<Nothing>()
    object ServerError : NetworkResponse<Nothing>()
    object GenericError : NetworkResponse<Nothing>()
}
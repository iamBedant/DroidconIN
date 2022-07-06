package in_.droidcon.india.network

import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import in_.droidcon.india.features.schedule.model.ScheduleResponse
import in_.droidcon.india.network.error.ApiResponse
import in_.droidcon.india.network.error.NetworkResponse
import in_.droidcon.india.network.error.getApiError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlin.collections.get

class NetworkApiImpl(private val log: Logger, engine: HttpClientEngine) : NetworkApi {
    private val client = HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    log.v { message }
                }
            }

            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }

    init {
        ensureNeverFrozen()
    }

    override suspend fun fetchDroidconSchedule(): NetworkResponse<ScheduleResponse> {
        return try {
            val result: ScheduleResponse = client.get {
                droidConSchedule("api/schedule/all")
            }.body()
            NetworkResponse.Success(result)
        } catch (e: Throwable) {
            getApiError(e)
        }

    }

    private fun HttpRequestBuilder.droidConSchedule(path: String) {
        url {
            takeFrom("http://demo1492434.mockable.io/")
            encodedPath = path
        }
    }
}
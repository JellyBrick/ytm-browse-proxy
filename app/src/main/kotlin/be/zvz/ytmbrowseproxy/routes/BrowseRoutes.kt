package be.zvz.ytmbrowseproxy.routes

import be.zvz.ytmbrowseproxy.dto.YTMBrowse
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.jsonIo
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.serialization.ExperimentalSerializationApi

object BrowseRoutes {
    @OptIn(ExperimentalSerializationApi::class)
    private val httpClient =
        HttpClient(Apache5) {
            install(ContentNegotiation) {
                jsonIo()
            }
        }

    fun Route.browseRouting() {
        route("/browse") {
            rateLimit {
                post {
                    val browseRequest = call.receive<YTMBrowse>()

                    val request =
                        HttpRequestBuilder().apply {
                            method = HttpMethod.Post
                            url("https://youtubei.googleapis.com/youtubei/v1/browse?prettyPrint=false")
                            setBody(
                                YTMBrowse(
                                    browseId = browseRequest.browseId,
                                    context = browseRequest.context,
                                ),
                            )
                            contentType(ContentType.Application.Json)
                        }
                    call.respondOutputStream(ContentType.Application.Json) {
                        httpClient.post(request)
                            .bodyAsChannel()
                            .copyTo(this)
                    }
                }
            }
        }
    }
}

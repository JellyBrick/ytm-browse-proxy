package be.zvz.ytmbrowseproxy.routes

import be.zvz.ytmbrowseproxy.dto.YTMBrowse
import be.zvz.ytmbrowseproxy.utils.JacksonUtils
import com.fasterxml.jackson.databind.DeserializationFeature
import guru.zoroark.ratelimit.rateLimited
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.request.receive
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.jvm.javaio.copyTo
import java.time.Duration

object BrowseRoutes {
    private val httpClient =
        HttpClient(Apache) {
            install(ContentNegotiation) {
                jackson {
                    configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                    registerModule(JacksonUtils.blackbirdModule)
                }
            }
        }

    fun Route.browseRouting() {
        rateLimited(
            limit = 2,
            timeBeforeReset = Duration.ofSeconds(1),
        ) {
            route("/browse") {
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
                        httpClient.post(request).bodyAsChannel().copyTo(this)
                    }
                }
            }
        }
    }
}

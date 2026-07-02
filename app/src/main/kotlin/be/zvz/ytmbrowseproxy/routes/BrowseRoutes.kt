package be.zvz.ytmbrowseproxy.routes

import be.zvz.ytmbrowseproxy.dto.YTMBrowse
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.jsonIo
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.ExperimentalSerializationApi

object BrowseRoutes {
    @OptIn(ExperimentalSerializationApi::class)
    private val httpClient =
        HttpClient(Apache5) {
            install(ContentNegotiation) {
                jsonIo()
            }

            install(ContentEncoding) {
                // faster -> slower
                gzip(1.0F)
                deflate(0.1F)
                identity()
            }
        }

    fun Route.browseRouting() {
        route("/browse") {
            rateLimit {
                post {
                    val browseRequest = call.receive<YTMBrowse>()

                    httpClient.preparePost("https://youtubei.googleapis.com/youtubei/v1/browse?prettyPrint=false") {
                        contentType(ContentType.Application.Json)
                        setBody(YTMBrowse(browseId = browseRequest.browseId, context = browseRequest.context))
                    }.execute { upstream ->
                        val body = upstream.bodyAsChannel()

                        call.respond(object : OutgoingContent.ReadChannelContent() {
                            override val contentType: ContentType = ContentType.Application.Json
                            override val status: HttpStatusCode = upstream.status
                            override fun readFrom(): ByteReadChannel = body
                        })
                    }
                }
            }
        }
    }
}

package be.zvz.ytmbrowseproxy.routes

import be.zvz.ytmbrowseproxy.dto.YTMBrowse
import com.ensody.kompressor.brotli.ktor.BrotliContentEncoder
import com.ensody.kompressor.zlib.ktor.DeflateContentEncoder
import com.ensody.kompressor.zlib.ktor.GzipContentEncoder
import com.ensody.kompressor.zstd.ktor.ZstdContentEncoder
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.compression.ContentEncoding
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
import io.ktor.server.response.respondBytesWriter
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.copyTo
import kotlinx.serialization.ExperimentalSerializationApi

object BrowseRoutes {
    @OptIn(ExperimentalSerializationApi::class)
    private val httpClient =
        HttpClient(Apache5) {
            install(ContentNegotiation) {
                jsonIo()
            }

            install(ContentEncoding) {
                customEncoder(GzipContentEncoder())
                customEncoder(DeflateContentEncoder())
                customEncoder(ZstdContentEncoder())
                customEncoder(BrotliContentEncoder())
                identity()
            }
        }

    fun Route.browseRouting() {
        route("/browse") {
            rateLimit {
                post {
                    val browseRequest = call.receive<YTMBrowse>()

                    call.respondBytesWriter(ContentType.Application.Json) {
                        httpClient
                            .post(
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
                                },
                            ).bodyAsChannel()
                            .copyTo(this)
                    }
                }
            }
        }
    }
}

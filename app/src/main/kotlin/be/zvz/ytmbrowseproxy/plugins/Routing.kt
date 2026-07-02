package be.zvz.ytmbrowseproxy.plugins

import be.zvz.ytmbrowseproxy.routes.BrowseRoutes.browseRouting
import com.ensody.kompressor.brotli.ktor.BrotliContentEncoder
import com.ensody.kompressor.zlib.ktor.DeflateContentEncoder
import com.ensody.kompressor.zlib.ktor.GzipContentEncoder
import com.ensody.kompressor.zstd.ktor.ZstdContentEncoder
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.identity
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.routing.routing
import kotlin.time.Duration.Companion.seconds

object Routing {
    fun Application.configureRouting() {
        install(ForwardedHeaders)
        install(XForwardedHeaders)
        install(RateLimit) {
            register {
                rateLimiter(limit = 2, refillPeriod = 1.seconds)
                requestKey { call ->
                    call.request.origin.remoteAddress
                }
            }
        }
        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Post)
            allowHeader(HttpHeaders.AccessControlAllowOrigin)
            allowHeader(HttpHeaders.ContentType)
            anyHost()
        }
        install(Compression) {
            encoder(GzipContentEncoder())
            encoder(ZstdContentEncoder())
            encoder(DeflateContentEncoder())
            encoder(BrotliContentEncoder())
            identity()
        }

        routing {
            browseRouting()
        }
    }
}

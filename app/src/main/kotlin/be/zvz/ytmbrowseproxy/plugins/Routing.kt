package be.zvz.ytmbrowseproxy.plugins

import be.zvz.ytmbrowseproxy.routes.BrowseRoutes.browseRouting
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.identity
import io.ktor.server.plugins.compression.zstd.zstd
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
                rateLimiter(limit = 4, refillPeriod = 1.seconds)
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
            zstd(3) { priority = 1.0 }
            gzip { priority = 0.9 }
            deflate { priority = 0.2 }
            identity()
        }

        routing {
            browseRouting()
        }
    }
}

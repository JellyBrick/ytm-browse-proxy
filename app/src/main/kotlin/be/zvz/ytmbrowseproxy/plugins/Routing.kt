package be.zvz.ytmbrowseproxy.plugins

import be.zvz.ytmbrowseproxy.routes.BrowseRoutes.browseRouting
import io.ktor.server.application.Application
import io.ktor.server.application.install
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
                requestKey {
                        call -> call.request.origin.remoteAddress
                }
            }
        }

        routing {
            browseRouting()
        }
    }
}

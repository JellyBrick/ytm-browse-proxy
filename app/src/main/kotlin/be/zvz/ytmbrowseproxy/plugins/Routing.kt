package be.zvz.ytmbrowseproxy.plugins

import be.zvz.ytmbrowseproxy.routes.BrowseRoutes.browseRouting
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.routing.routing

object Routing {
    fun Application.configureRouting() {
        install(ForwardedHeaders)
        install(XForwardedHeaders)

        routing {
            browseRouting()
        }
    }
}

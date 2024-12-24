package be.zvz.ytmbrowseproxy

import be.zvz.ytmbrowseproxy.plugins.Routing.configureRouting
import be.zvz.ytmbrowseproxy.plugins.Serialization.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSerialization()
}

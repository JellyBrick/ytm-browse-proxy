package be.zvz.ytmbrowseproxy.plugins

import io.ktor.serialization.kotlinx.json.jsonIo
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.ExperimentalSerializationApi

object Serialization {
    @OptIn(ExperimentalSerializationApi::class)
    fun Application.configureSerialization() {
        install(ContentNegotiation) {
            jsonIo()
        }
    }
}

package be.zvz.ytmbrowseproxy.plugins

import be.zvz.ytmbrowseproxy.utils.JacksonUtils
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

object Serialization {
    fun Application.configureSerialization() {
        install(ContentNegotiation) {
            jackson {
                registerKotlinModule()
                registerModule(JacksonUtils.blackbirdModule)
            }
        }
    }
}

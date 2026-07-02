package be.zvz.ytmbrowseproxy.dto

import kotlinx.serialization.Serializable

@Serializable
data class YTMBrowse(
    val browseId: String,
    val context: Context,
) {
    @Serializable
    data class Context(
        val client: Client,
    ) {
        @Serializable
        data class Client(
            val clientName: String,
            val clientVersion: String,
        )
    }
}

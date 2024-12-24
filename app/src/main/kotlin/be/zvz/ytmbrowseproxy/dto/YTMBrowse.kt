package be.zvz.ytmbrowseproxy.dto

data class YTMBrowse(
    val browseId: String,
    val context: Context,
) {
    data class Context(
        val client: Client,
    ) {
        data class Client(
            val clientName: String,
            val clientVersion: String,
        )
    }
}

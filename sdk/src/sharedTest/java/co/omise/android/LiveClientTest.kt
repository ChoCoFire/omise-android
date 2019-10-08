package co.omise.android

import co.omise.android.api.Client


interface LiveClientTest {
    val publicKey: String
        get() = "[PUBLIC_API]"

    val liveClient: Client
        get() = Client(publicKey)
}

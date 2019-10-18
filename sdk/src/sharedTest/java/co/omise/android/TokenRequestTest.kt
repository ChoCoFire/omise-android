package co.omise.android

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import co.omise.android.api.RequestListener
import co.omise.android.models.CardParam
import co.omise.android.models.Token
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class TokenRequestTest : LiveClientTest {

    @Test
    fun createToken() {
        val clientLatch = CountDownLatch(1)

        runOnUiThread {
            val params = CardParam(
                    name = "John Doe",
                    number = "4242424242424242",
                    expirationMonth = 12,
                    expirationYear = 2020,
                    securityCode = "123"

            )
            val request = Token.CreateTokenRequestBuilder(params)
                    .build()
            liveClient.send(request, object : RequestListener<Token> {
                override fun onRequestSucceed(model: Token) {
                    Log.d("createToken", "$model")
                    assertNotNull(model)
                    clientLatch.countDown()
                }

                override fun onRequestFailed(throwable: Throwable) {
                    Log.e("createToken", throwable.message)
                    fail(throwable.message)
                    clientLatch.countDown()
                }
            })
        }

        clientLatch.await(10_000, TimeUnit.MILLISECONDS).run {
            if (!this) fail("Timeout!")
        }
    }
}

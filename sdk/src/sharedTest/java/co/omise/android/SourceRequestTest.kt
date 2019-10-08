package co.omise.android

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import co.omise.android.api.RequestListener
import co.omise.android.models.Source
import co.omise.android.models.SourceType
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class SourceRequestTest : LiveClientTest {

    @Test
    fun createSource() {
        val clientLatch = CountDownLatch(1)

        runOnUiThread {
            val request = Source.CreateSourceRequestBuilder(
                    500_000L,
                    "thb",
                    SourceType.InternetBanking.Bay)
                    .build()
            liveClient.send(request, object : RequestListener<Source> {
                override fun onRequestSucceed(model: Source) {
                    Log.d("createSource", "$model")
                    assertNotNull(model)
                    clientLatch.countDown()
                }

                override fun onRequestFailed(throwable: Throwable) {
                    Log.e("createSource", throwable.message)
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

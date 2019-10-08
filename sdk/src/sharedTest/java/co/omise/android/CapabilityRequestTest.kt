package co.omise.android

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import co.omise.android.api.RequestListener
import co.omise.android.models.Capability
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class CapabilityRequestTest : LiveClientTest {

    @Test
    fun getCapability() {
        val clientLatch = CountDownLatch(1)

        runOnUiThread {
            val request = Capability.GetCapabilitiesRequestBuilder().build()
            liveClient.send(request, object : RequestListener<Capability> {
                override fun onRequestSucceed(model: Capability) {
                    Log.d("getCapability", "$model")
                    clientLatch.countDown()
                }

                override fun onRequestFailed(throwable: Throwable) {
                    Log.e("getCapability", throwable.message)
                    clientLatch.countDown()
                }
            })
        }

        val isReached = clientLatch.await(10_000, TimeUnit.MILLISECONDS)
        if (!isReached) {
            fail("Timeout!")
        }
    }

}

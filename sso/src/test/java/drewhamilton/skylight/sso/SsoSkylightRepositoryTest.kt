package drewhamilton.skylight.sso

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import drewhamilton.skylight.models.Coordinates
import drewhamilton.skylight.sso.conversions.toSkylightInfo
import drewhamilton.skylight.sso.network.InfoClient
import drewhamilton.skylight.sso.network.models.Params
import drewhamilton.skylight.sso.network.models.SunriseSunsetInfo
import io.reactivex.Single
import org.junit.Test
import java.util.*

class SsoSkylightRepositoryTest {

    private val dummyDawn = Date(99_999_999_910_000L)
    private val dummySunrise = Date(99_999_999_920_000L)
    private val dummySunset = Date(99_999_999_930_000L)
    private val dummyDusk = Date(99_999_999_940_000L)

    private val dummyLatitude = 12.3
    private val dummyLongitude = 45.6
    private val dummyNow = Date(99_999_999_925_000L)
    private val dummyCoordinates = Coordinates(dummyLatitude, dummyLongitude)
    private val dummyParams = Params(dummyLatitude, dummyLongitude, dummyNow)

    private val dummySunriseSunsetInfo = SunriseSunsetInfo(dummyDawn, dummySunrise, dummySunset, dummyDusk)

    private lateinit var mockClient: InfoClient

    @Test
    fun `getSkylightInfo returns converted info from client`() {
        mockClient = mock {
            on { getInfo(dummyParams) } doReturn Single.fromCallable { dummySunriseSunsetInfo }
        }
        val ssoSkylightRepository = SsoSkylightRepository(mockClient)

        ssoSkylightRepository.getSkylightInfo(dummyCoordinates, dummyNow).test()
            .assertValueCount(1)
            .assertValue { it == dummySunriseSunsetInfo.toSkylightInfo() }
    }
}

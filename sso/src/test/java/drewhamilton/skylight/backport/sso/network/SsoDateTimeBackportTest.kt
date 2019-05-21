package drewhamilton.skylight.backport.sso.network

import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeParseException

class SsoDateTimeBackportTest {

    // 1999 Dec. 31 11:59:59.000pm UTC:
    private val testDateTime = ZonedDateTime.of(1999, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC)

    private val testDateTimeStringUtc = "1999-12-31T23:59:59+00:00"
    private val testDateTimeStringCest = "2000-01-01T01:59:59+02:00"

    @Test
    fun `parse with UTC time zone returns date from UTC string`() {
        assertEquals(testDateTime, testDateTimeStringUtc.toZonedDateTimeBackport())
    }

    @Test
    fun `parse with CEST time zone returns date from CEST string`() {
        assertEquals(
            testDateTime.withZoneSameInstant(ZoneOffset.ofHours(2)),
            testDateTimeStringCest.toZonedDateTimeBackport()
        )
    }

    @Test(expected = DateTimeParseException::class)
    fun `parse without time zone colon throws DateTimeParseException`() {
        "2017-03-31T07:48:23+0200".toZonedDateTimeBackport()
    }
}

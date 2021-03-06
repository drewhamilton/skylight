package dev.drewhamilton.skylight.sunrise_sunset_org.network

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class SunriseSunsetOrgDateFormatTest {

    private val testDateString = "2016-01-23"
    private val testDate = LocalDate.parse(testDateString)

    @Test
    fun `format produces expected string`() {
        assertEquals(testDateString, testDate.toSunriseSunsetOrgDateString())
    }
}

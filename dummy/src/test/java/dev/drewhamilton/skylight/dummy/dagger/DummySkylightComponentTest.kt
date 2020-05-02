package dev.drewhamilton.skylight.dummy.dagger

import dev.drewhamilton.skylight.SkylightDay
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DummySkylightComponentTest {

    private val testDate = LocalDate.ofEpochDay(4)
    private val testSkylightDay = SkylightDay.NeverLight { date = testDate }

    @Test
    fun `create returns DummySkylightComponent with dummySkylightDay`() {
        val dummySkylightComponent = DummySkylightComponent.create(testSkylightDay)
        val skylight = dummySkylightComponent.skylight()

        assertEquals(testSkylightDay, skylight.getSkylightDay(testDate))
    }
}
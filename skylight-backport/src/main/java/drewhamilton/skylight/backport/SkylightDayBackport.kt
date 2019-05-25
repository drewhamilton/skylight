package drewhamilton.skylight.backport

import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime

/**
 * Represents the Skylight information for a single day at a single location.
 *
 * TODO MISSING What about transition days that e.g. have a sunrise but no sunset?
 */
sealed class SkylightDayBackport {

    abstract val date: LocalDate

    /**
     * Represents a normal day, where dawn and dusk represent crossing civil twilight, and sunrise and sunset represent
     * crossing the horizon.
     */
    data class Typical(
        override val date: LocalDate,
        val dawn: OffsetTime,
        val sunrise: OffsetTime,
        val sunset: OffsetTime,
        val dusk: OffsetTime
    ) : SkylightDayBackport()

    /**
     * Represents a day that is always full light, i.e. the sun never goes below the horizon.
     */
    data class AlwaysDaytime(
        override val date: LocalDate
    ) : SkylightDayBackport()

    /**
     * Represents a day where there is full light and twilight, but no full darkness, i.e. the sun never goes below
     * civil twilight.
     */
    data class AlwaysLight(
        override val date: LocalDate,
        val sunrise: OffsetTime,
        val sunset: OffsetTime
    ) : SkylightDayBackport()

    /**
     * Represents a day where there is darkness and twilight, but no full light, i.e. the sun never goes above the
     * horizon.
     */
    data class NeverDaytime(
        override val date: LocalDate,
        val dawn: OffsetTime,
        val dusk: OffsetTime
    ) : SkylightDayBackport()

    /**
     * Represents a day that is always darkness, i.e. the sun never goes above civil twilight.
     */
    data class NeverLight(
        override val date: LocalDate
    ) : SkylightDayBackport()

}

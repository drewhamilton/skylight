package dev.drewhamilton.skylight.calculator

import dagger.Reusable
import dev.drewhamilton.skylight.Coordinates
import dev.drewhamilton.skylight.Skylight
import dev.drewhamilton.skylight.SkylightDay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

/**
 * Adapted from AndroidX's (internal) TwilightCalculator class.
 */
@Reusable
class CalculatorSkylight @Inject constructor() : Skylight {

    /**
     * Calculates the [SkylightDay] based on the given [coordinates] and [date].
     */
    override fun getSkylightDay(coordinates: Coordinates, date: LocalDate): SkylightDay {
        val epochMillis = date.toNoonUtcEpochMillis()
        return calculateSkylightInfo(epochMillis, coordinates.latitude, coordinates.longitude)
            .toSkylightDay(date)
    }

    private fun LocalDate.toNoonUtcEpochMillis() = atTime(12, 0).toInstant(ZoneOffset.UTC).toEpochMilli()

    private fun EpochMilliSkylightDay.toSkylightDay(date: LocalDate) = when (this) {
        is EpochMilliSkylightDay.Typical -> SkylightDay.Typical(
            date = date,
            dawn = dawn.asEpochMilliToDateTime(),
            sunrise = sunrise.asEpochMilliToDateTime(),
            sunset = sunset.asEpochMilliToDateTime(),
            dusk = dusk.asEpochMilliToDateTime()
        )
        is EpochMilliSkylightDay.AlwaysDaytime -> SkylightDay.AlwaysDaytime(date = date)
        is EpochMilliSkylightDay.NeverLight -> SkylightDay.NeverLight(date = date)
    }

    private fun Long?.asEpochMilliToDateTime(): Instant? = if (this == null) null else Instant.ofEpochMilli(this)
}

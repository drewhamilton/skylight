package drewhamilton.skylight.models

import java.util.*

sealed class SkylightInfo

/**
 * Represents a normal day, where dawn and dusk represent crossing civil twilight, and sunrise and sunset represent
 * crossing the horizon.
 *
 * TODO What about transition days that e.g. have a sunrise but no sunset?
 */
data class Typical(
    val dawn: Date,
    val sunrise: Date,
    val sunset: Date,
    val dusk: Date
) : SkylightInfo()

/**
 * Represents a day that is always full light, i.e. the sun never goes below the horizon.
 */
class AlwaysDaytime : SkylightInfo() {
  override fun equals(other: Any?) = this === other || other is AlwaysDaytime
  override fun hashCode() = javaClass.hashCode()
}

/**
 * Represents a day where there is full light and twilight, but no full darkness, i.e. the sun never goes below civil
 * twilight.
 */
data class AlwaysLight(
    val sunrise: Date,
    val sunset: Date
) : SkylightInfo()

/**
 * Represents a day where there is darkness and twilight, but no full light, i.e. the sun never goes above the horizon.
 */
data class NeverDaytime(
    val dawn: Date,
    val dusk: Date
) : SkylightInfo()

/**
 * Represents a day that is always darkness, i.e. the sun never goes above civil twilight.
 */
class NeverLight : SkylightInfo() {
  override fun equals(other: Any?) = this === other || other is NeverLight
  override fun hashCode() = javaClass.hashCode()
}


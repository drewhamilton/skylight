package drewhamilton.skylight.views.event

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.google.android.material.card.MaterialCardView
import drewhamilton.skylight.views.R
import drewhamilton.skylight.views.compat.setCompatAutoSizeTextTypeUniformWithConfiguration
import drewhamilton.skylight.views.compat.setCompatTextAppearance
import kotlinx.android.synthetic.main.view_skylight_event.view.label
import kotlinx.android.synthetic.main.view_skylight_event.view.time
import java.text.DateFormat
import java.time.OffsetTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

/**
 * A simple card view showing a single Skylight event. Typically used to display the time of
 * the event and the label, e.g. "Sunrise" or "Sunset".
 */
class SkylightEventView : MaterialCardView {

    var labelText: CharSequence
        get() = label.text
        set(text) {
            label.text = text
        }

    var timeText: CharSequence
        get() = time.text
        set(text) {
            time.text = text
            label.visibility = if (shouldShowLabel) View.VISIBLE else View.INVISIBLE
        }

    internal var timeHint: CharSequence?
        get() = time.hint
        set(hint) {
            time.hint = hint
            label.visibility = if (shouldShowLabel) View.VISIBLE else View.INVISIBLE
        }

    private val shouldShowLabel
        get() = !(time.text.isEmpty() && time.hint?.isEmpty() ?: true)

    init {
        inflate(context, R.layout.view_skylight_event, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributeSet(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributeSet(attrs)
    }

    fun setLabelText(@StringRes resId: Int) = label.setText(resId)

    /**
     * Set the text appearance for the label view.
     * @param resId The style to apply as the label view's text appearance
     */
    fun setLabelTextAppearance(@StyleRes resId: Int) {
        label.setCompatTextAppearance(resId)
    }

    fun setTimeText(@StringRes resId: Int) {
        time.setText(resId)
        label.visibility = if (shouldShowLabel) View.VISIBLE else View.INVISIBLE
    }

    /**
     * Set the text appearance for the time view.
     * @param resId The style to apply as the time view's text appearance
     */
    fun setTimeTextAppearance(@StyleRes resId: Int) {
        time.setCompatTextAppearance(resId)
    }

    /**
     * Set the time view to auto-size its text from [minSizePx] to [maxSizePx] based on the view size.
     */
    fun setTimeTextAutoSizeRange(
        minSizePx: Int, maxSizePx: Int,
        stepGranularity: Int = resources.getDimensionPixelSize(R.dimen.skylight_granularity_skylightEventTime)
    ) {
        time.setCompatAutoSizeTextTypeUniformWithConfiguration(
            minSizePx,
            maxSizePx,
            stepGranularity,
            TypedValue.COMPLEX_UNIT_PX
        )
    }

    private fun initAttributeSet(attrs: AttributeSet) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SkylightEventView)
        try {
            applyLabelTextAppearance(styledAttributes)
            label.text = styledAttributes.getString(R.styleable.SkylightEventView_skylightEventLabelText)

            applyTimeTextAppearance(styledAttributes)
            applyTimeTextAutoSizes(styledAttributes)
            time.text = styledAttributes.getString(R.styleable.SkylightEventView_skylightEventTimeText)

            if (shouldShowLabel) label.visibility = View.VISIBLE
        } finally {
            styledAttributes.recycle()
        }
    }

    private fun applyLabelTextAppearance(styledAttributes: TypedArray) {
        val labelTextAppearance = styledAttributes.getResourceId(
            R.styleable.SkylightEventView_skylightEventLabelTextAppearance,
            R.style.TextAppearance_AppCompat_Caption
        )
        label.setCompatTextAppearance(labelTextAppearance)
    }

    private fun applyTimeTextAppearance(styledAttributes: TypedArray) {
        val timeTextAppearance = styledAttributes.getResourceId(
            R.styleable.SkylightEventView_skylightEventTimeTextAppearance,
            R.style.TextAppearance_AppCompat_Display3
        )
        time.setCompatTextAppearance(timeTextAppearance)
    }

    private fun applyTimeTextAutoSizes(styledAttributes: TypedArray) {
        val unspecifiedTextSize = -1
        val timeTextMinSize = styledAttributes.getDimensionPixelSize(
            R.styleable.SkylightEventView_skylightEventTimeTextMinSize,
            unspecifiedTextSize
        )
        val timeTextMaxSize = styledAttributes.getDimensionPixelSize(
            R.styleable.SkylightEventView_skylightEventTimeTextMaxSize,
            unspecifiedTextSize
        )
        if (!(timeTextMinSize == unspecifiedTextSize && timeTextMaxSize == unspecifiedTextSize)) {
            when {
                timeTextMinSize == unspecifiedTextSize ->
                    time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeTextMaxSize.toFloat())
                timeTextMaxSize == unspecifiedTextSize ->
                    time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeTextMinSize.toFloat())
                else ->
                    setTimeTextAutoSizeRange(timeTextMinSize, timeTextMaxSize)
            }
        }
    }
}

//region Legacy Date extensions
fun SkylightEventView.setTime(time: Date?, @StringRes fallback: Int) =
    setTime(time, fallback = context.getString(fallback))

fun SkylightEventView.setTime(dateTime: Date?, format: DateFormat, @StringRes fallback: Int) =
    setTime(dateTime, format, context.getString(fallback))

fun SkylightEventView.setTime(
    dateTime: Date?,
    format: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT),
    fallback: String = ""
) {
    if (dateTime == null) {
        timeHint = fallback
        timeText = ""
    } else {
        timeText = format.format(dateTime)
        timeHint = ""
    }
}
//endregion

//region java.time extensions
@RequiresApi(Build.VERSION_CODES.O)
fun SkylightEventView.setTime(time: OffsetTime?, @StringRes fallback: Int) =
    setTime(time, fallback = context.getString(fallback))

@RequiresApi(Build.VERSION_CODES.O)
fun SkylightEventView.setTime(time: OffsetTime?, formatter: DateTimeFormatter, @StringRes fallback: Int) =
    setTime(time, formatter, context.getString(fallback))

@RequiresApi(Build.VERSION_CODES.O)
fun SkylightEventView.setTime(
    time: OffsetTime?,
    formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT),
    fallback: String = ""
) {
    if (time == null) {
        timeHint = fallback
        timeText = ""
    } else {
        timeText = formatter.format(time)
        timeHint = ""
    }
}
//endregion

package com.ogeedeveloper.local_event_finder_frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ogeedeveloper.local_event_finder_frontend.ui.theme.LocaleventfinderfrontendTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSlider(
    minPrice: Float,
    maxPrice: Float,
    initialValueMin: Float = minPrice,
    initialValueMax: Float = maxPrice,
    onValueChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    formatPrice: (Float) -> String = { "$${it.roundToInt()}" },
    steps: Int = 0
) {
    var sliderPosition by remember { mutableStateOf(initialValueMin..initialValueMax) }

    Column(modifier = modifier) {
        Text(
            text = "Price Range",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        RangeSlider(
            value = sliderPosition,
            onValueChange = { range ->
                sliderPosition = range
                onValueChange(range.start, range.endInclusive)
            },
            valueRange = minPrice..maxPrice,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            ),
            startThumb = { SliderThumb(isStart = true) },
            endThumb = { SliderThumb(isStart = false) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${formatPrice(sliderPosition.start)} - ${formatPrice(sliderPosition.endInclusive)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SliderThumb(
    isStart: Boolean
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = if (isStart) "||" else "||",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleRangeFilter(
    title: String,
    minValue: Float,
    maxValue: Float,
    initialMinValue: Float = minValue,
    initialMaxValue: Float = maxValue,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueFormatter: (Float) -> String,
    modifier: Modifier = Modifier
) {
    var range by remember { mutableStateOf(initialMinValue..initialMaxValue) }

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        RangeSlider(
            value = range,
            onValueChange = {
                range = it
                onValueChange(it)
            },
            valueRange = minValue..maxValue,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            ),
            startThumb = { SliderThumb(isStart = true) },
            endThumb = { SliderThumb(isStart = false) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${valueFormatter(range.start)} - ${valueFormatter(range.endInclusive)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PriceRangeSliderPreview() {
    LocaleventfinderfrontendTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            var minPrice by remember { mutableFloatStateOf(10f) }
            var maxPrice by remember { mutableFloatStateOf(25f) }

            PriceRangeSlider(
                minPrice = 0f,
                maxPrice = 100f,
                initialValueMin = minPrice,
                initialValueMax = maxPrice,
                onValueChange = { min, max ->
                    minPrice = min
                    maxPrice = max
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleRangeFilterPreview() {
    LocaleventfinderfrontendTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            SimpleRangeFilter(
                title = "Price Range",
                minValue = 0f,
                maxValue = 100f,
                initialMinValue = 10f,
                initialMaxValue = 25f,
                onValueChange = { },
                valueFormatter = { "$${it.roundToInt()}" }
            )
        }
    }
}
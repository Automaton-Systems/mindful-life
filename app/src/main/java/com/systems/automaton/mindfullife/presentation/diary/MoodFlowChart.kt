package com.systems.automaton.mindfullife.presentation.diary

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.systems.automaton.mindfullife.domain.model.DiaryEntry
import com.systems.automaton.mindfullife.util.diary.Mood
import com.systems.automaton.mindfullife.R

@Composable
fun MoodFlowChart(
    entries: List<DiaryEntry>,
    monthly: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.mood_flow),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val mostFrequentMood by remember(entries) {
                    derivedStateOf {
                        // if multiple ones with the same frequency, return the most positive one
                        val entriesGrouped = entries
                            .groupBy { it.mood }
                        val max = entriesGrouped.maxOf { it.value.size }
                        entriesGrouped
                            .filter { it.value.size == max }
                            .maxByOrNull {
                                it.key.value
                            }?.key ?: Mood.OKAY
                    }
                }
                val moods = listOf(Mood.AWESOME, Mood.GOOD, Mood.OKAY, Mood.BAD, Mood.TERRIBLE)
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    moods.forEach { mood ->
                        Icon(
                            painter = painterResource(mood.icon),
                            tint = mood.color,
                            contentDescription = stringResource(mood.title),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                if (entries.isNotEmpty())
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        val w = size.width
                        val h = size.height

                        val max = Mood.AWESOME.value
                        val count = entries.size

                        val offsets = entries.mapIndexed { index, entry ->
                            Offset(
                                w * ((if (index == 0) index.toFloat() else index + 1f) / count),
                                h * (1 - entry.mood.value.toFloat() / max.toFloat())
                            )
                        }
                        val path = Path().apply {
                            moveTo(offsets.first().x, offsets.first().y)
                            offsets.forEachIndexed { index, offset ->
                                if (index == 0) return@forEachIndexed
                                quadTo(offsets[index - 1], offset)
                            }
                        }
                        // workaround to copy compose path by using android path
                        val fillPath = android.graphics.Path(path.asAndroidPath())
                            .asComposePath()
                            .apply {
                                lineTo(
                                    if (offsets.size > 1)
                                        (offsets[offsets.size - 2].x + offsets.last().x) / 2
                                    else offsets.last().x, h
                                )
                                lineTo(0f, h)
                                close()
                            }
                        drawPath(
                            fillPath,
                            brush = Brush.verticalGradient(
                                listOf(
                                    mostFrequentMood.color,
                                    Color.Transparent
                                ),
                                endY = h
                            )
                        )
                        drawPath(
                            path,
                            color = mostFrequentMood.color,
                            style = Stroke(8f, cap = StrokeCap.Round)
                        )
                    } else {
                    Text(
                        text = stringResource(R.string.no_data_yet),
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Text(
                text = if (monthly) stringResource(R.string.mood_during_month)
                else stringResource(R.string.mood_during_year),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

fun Path.quadTo(point1: Offset, point2: Offset) {
    quadraticBezierTo(
        point1.x,
        point1.y,
        (point1.x + point2.x) / 2f,
        (point1.y + point2.y) / 2f,
    )
}

@Preview
@Composable
fun MoodFlowChartPreview() {
    MoodFlowChart(
        entries = listOf(
            DiaryEntry(
                id = 1,
                mood = Mood.AWESOME
            ),
            DiaryEntry(
                id = 1,
                mood = Mood.AWESOME
            ),
            DiaryEntry(
                id = 2,
                mood = Mood.GOOD,
            ),
            DiaryEntry(
                id = 3,
                mood = Mood.OKAY,
            ),
            DiaryEntry(
                id = 4,
                mood = Mood.GOOD,
            ),
            DiaryEntry(
                id = 5,
                mood = Mood.BAD,
            ),
            DiaryEntry(
                id = 6,
                mood = Mood.BAD,
            ),
            DiaryEntry(
                id = 7,
                mood = Mood.TERRIBLE,
            ),
            DiaryEntry(
                id = 8,
                mood = Mood.GOOD,
            ),
            DiaryEntry(
                id = 8,
                mood = Mood.BAD,
            )
        )
    )
}
package com.example.workschedule.Components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

@Composable
fun TaskItemInteractive(item: TaskItems) {
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "")

    val maxSwipeRight = 350f
    val maxSwipeLeft = -200f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4B4B4B))
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX = (offsetX + dragAmount).coerceIn(maxSwipeLeft, maxSwipeRight)
                    },
                    onDragEnd = {
                        offsetX = when {
                            offsetX > maxSwipeRight / 2 -> maxSwipeRight
                            offsetX < maxSwipeLeft / 2 -> maxSwipeLeft
                            else -> 0f
                        }
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF007BFF), shape = MaterialTheme.shapes.medium)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF00D26A), shape = MaterialTheme.shapes.medium)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color.White
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = { /* TODO: Reload */ },
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 16.dp)
                    .background(Color(0xFFFF9800), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reload",
                    tint = Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .background(Color(0xFF2D2D2D), shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.desc,
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

package com.example.workschedule.Components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workschedule.R
import com.example.workschedule.model.Schedule
import com.example.workschedule.play.AudioPlayer
import kotlin.math.roundToInt



// Dùng để hiển thị task có thể vuốt hai chiều (ví dụ Play / Done bên trái, Reload bên phải)
@Composable
fun TaskItemInteractive(item: Schedule, navController: NavController,  onDeleteClick: ((Schedule) -> Unit)? = null) {
    val context = LocalContext.current
    val audioPlayer = remember { AudioPlayer() }
    var isPlaying by remember { mutableStateOf(false) }

    SwipeableTaskItem(
        //phát âm thanh
        item = item,
        swipeRightContent = {
            IconAction(
                icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                desc = if (isPlaying) "Pause" else "Play",
                bgColor = Color(0xFF007BFF),
                onClick = {
                    item.audioUrl?.let { audioUrl ->
                        if (isPlaying) {
                            audioPlayer.stopAudio()
                            isPlaying = false
                        } else {
                            audioPlayer.playAudioFromUrl(audioUrl) {
                                isPlaying = false
                            }
                            isPlaying = true
                        }
                    }
                }
            )

            Spacer(Modifier.width(12.dp))
            IconAction(Icons.Default.Edit, "Edit", Color(0xFF00D26A),onClick = {
                println("DEBUG: Navigating to edit screen with id = ${item.id}")
                item.id?.let { id ->
                    navController.navigate("edit/$id")
                } ?: println("ERROR: item.id is NULL!")


            })
        },
        swipeLeftContent = {
            IconAction(Icons.Default.Delete, "Delete", Color.Red, tint = Color.Black,
                onClick = { onDeleteClick?.invoke(item) }
            )
        },
        enableRightSwipe = true
    )
}

// Dùng để hiển thị task có thể vuốt trái để xóa
@Composable
fun TaskItemDeletable(item: Schedule) {
    SwipeableTaskItem(
        item = item,
        swipeLeftContent = {
            IconAction(Icons.Default.Delete, "Delete", Color.Red, tint = Color.Black) { /* TODO */ }
        },
        enableRightSwipe = false
    )
}

// Dùng để hiển thị task đơn giản, không vuốt
@Composable
fun TaskItemSimple(item: Schedule) {
    TaskCard(item)
}

// Composable dùng chung cho Task có vuốt
@Composable
fun SwipeableTaskItem(
    item: Schedule,
    swipeRightContent: @Composable (RowScope.() -> Unit)? = null,
    swipeLeftContent: @Composable (() -> Unit)? = null,
    enableRightSwipe: Boolean
) {
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(offsetX, label = "")

    val maxSwipeRight = if (enableRightSwipe) 350f else 0f
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
        // Vuốt phải: các nút bên trái
        if (swipeRightContent != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(modifier = Modifier.padding(start = 16.dp), content = swipeRightContent)
            }
        }

        // Vuốt trái: các nút bên phải
        if (swipeLeftContent != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(modifier = Modifier.padding(end = 16.dp)) {
                    swipeLeftContent()
                }
            }
        }

        // Nội dung chính
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .fillMaxWidth()
        ) {
            TaskCard(item)
        }
    }
}

// Dùng chung để hiển thị card task
@Composable
fun TaskCard(item: Schedule) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2D2D2D), shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = item.description,
                color = Color(0xFFDDDDDD),
                fontSize = 16.sp,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.time),
                    contentDescription = "Time",
                    modifier = Modifier.size(18.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.time.toString(),
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Nút icon được tái sử dụng
@Composable
fun IconAction(
    icon: ImageVector,
    desc: String,
    bgColor: Color,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .background(bgColor, shape = CircleShape)
    ) {
        Icon(imageVector = icon, contentDescription = desc, tint = tint)
    }
}

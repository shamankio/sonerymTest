package com.rustan.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rustan.model.ImageItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    detailsViewModel: DetailScreenViewModel = koinViewModel(),
    item: ImageItem?,
    onNavigateToHome: () -> Unit
) {
    val imageHeight = 300.dp
    var headerHeight by remember { mutableStateOf(imageHeight) }
    val alpha by remember {
        derivedStateOf {
            val coerceIn = (headerHeight / imageHeight).coerceIn(0f, 0.7f)
            0.7f - coerceIn
        }
    }
    val isAudioPlayerVisible by detailsViewModel.isAudioPlayerVisible.collectAsState()

    Scaffold(
        bottomBar = {
            DetailsBottomBar(
                onHomeClick = { onNavigateToHome() },
                onAudioClick = { detailsViewModel.onEvent(DetailEvent.onPlayerVisible) }
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            Box(Modifier.fillMaxSize()) {
                // Image with dim overlay
                ImageSection(item, imageHeight, alpha)

                // Scrollable content
                ContentSection(item, { headerHeight = it }, imageHeight)
                // Animated audio player
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    AudioPlayerSection(isAudioPlayerVisible, detailsViewModel::onEvent)
                }
            }
        }
    }
}

@Composable
private fun ImageSection(photo: ImageItem?, imageHeight: Dp, alpha: Float) {
    Box {
        AsyncImage(
            model = photo?.urlString,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .drawBehind {
                    drawRect(Color.Black.copy(alpha = alpha))
                }
        )
    }
}

@Composable
private fun ContentSection(photo: ImageItem?, onSizeChange: (Dp) -> Unit, imageHeight: Dp) {
    val headerMinSize = 0.dp
    val headerMaxSize = imageHeight
    var headerSpaceHeight by remember { mutableStateOf(imageHeight) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount -> // Detecting drag gestures
                    change.consume()
                    val offsetY = with(density) { dragAmount.y.toDp() }
                    headerSpaceHeight = (headerSpaceHeight + offsetY).coerceIn(headerMinSize, headerMaxSize)
                    onSizeChange(headerSpaceHeight)
                }
            }
    ) {
        Spacer(modifier = Modifier.height(headerSpaceHeight))
        Column(modifier = Modifier.padding(16.dp)) {
            Text("AuthorId: ${photo?.authorId}", style = MaterialTheme.typography.bodyMedium)
            Text("Author: ${photo?.author}", style = MaterialTheme.typography.bodyMedium)
            Text("DateTaken: ${photo?.dateTaken}", style = MaterialTheme.typography.bodyMedium)
            Text("Link: ${photo?.link}", style = MaterialTheme.typography.bodyMedium)
            Text("UrlString: ${photo?.urlString}", style = MaterialTheme.typography.bodyMedium)
            Text("Published: ${photo?.published}", style = MaterialTheme.typography.bodyMedium)
            Text("Tags: ${photo?.tags}", style = MaterialTheme.typography.bodyMedium)
            Text("Title: ${photo?.title}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun DetailsBottomBar(onHomeClick: () -> Unit, onAudioClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onHomeClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Home")
        }
        TextButton(
            onClick = onAudioClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Audio")
        }
    }
}

@Composable
private fun AudioPlayerSection(visible: Boolean, onEvent: (DetailEvent) -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
    ) {
        AudioPlayer(onEvent = onEvent)
    }
}

@Composable
private fun AudioPlayer(onEvent: (DetailEvent) -> Unit) {
    var progress by remember { mutableFloatStateOf(0.3f) }
    var isDragging by remember { mutableStateOf(false) }
    val heightProgress: Dp by animateDpAsState(if (isDragging) 16.dp else 8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { onEvent(DetailEvent.onPrevious) }) {
                Icon(Icons.Default.ArrowBack, null)
            }
            IconButton(onClick = { onEvent(DetailEvent.onPlay) }) {
                Icon(Icons.Default.PlayArrow, null)
            }
            IconButton(onClick = { onEvent(DetailEvent.onNext) }) {
                Icon(Icons.Default.ArrowForward, null)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightProgress)
                .background(Color.Gray.copy(alpha = 0.3f))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false }) { change, _ ->
                        progress = (change.position.x / size.width).coerceIn(0f, 1f)
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(heightProgress)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}
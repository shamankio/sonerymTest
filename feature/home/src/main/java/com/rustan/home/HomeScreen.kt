package com.rustan.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rustan.model.ImageItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel(),
    onNavigateToDetail: (ImageItem) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        homeViewModel.navigateTo.collect {
            when (it) {
                is HomeNavigationEvent.NavigateToDetails -> {
                    onNavigateToDetail(it.imageItem)
                }
            }
        }
    }
    val screenState by homeViewModel.uiState.collectAsStateWithLifecycle()
    when (screenState) {
        is HomeScreenState.Success -> {
            HomeScreenContent(
                modifier = modifier,
                homeData = (screenState as HomeScreenState.Success).homeData,
                onClick = { homeViewModel.onEvent(it) })
        }

        is HomeScreenState.Loading -> {}
        is HomeScreenState.Error -> {}
        is HomeScreenState.Empty -> {}
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    homeData: HomeData,
    onClick: (HomeEvent) -> Unit
) {
    Column {
        RowView("Cat", homeData.row1, onClick)
        RowView("Dog", homeData.row2, onClick)
        RowView("Elephant", homeData.row3, onClick)
        RowView("Hamster", homeData.row4, onClick)
    }
}

@Composable
fun RowView(rowName: String, listItems: List<ImageItem>, onClick: (HomeEvent) -> Unit) {
    Column {
        Row(
            modifier = Modifier.padding(start = 15.dp, top = 20.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = rowName, style = MaterialTheme.typography.headlineMedium)
        }
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 15.dp)
            ) {
                items(listItems) { item ->
                    EventView(item = item, onClick = onClick)
                }
            }
        }
    }
}

@Composable
fun EventView(item: ImageItem, onClick: (HomeEvent) -> Unit) {
    Column(
        modifier = Modifier
            .width(190.dp)
            .clickable { onClick(HomeEvent.CardClick(item)) }
    ) {
        Card(
            modifier = Modifier
                .width(190.dp)
                .height(110.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = item.urlString).allowHardware(false)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.title ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    EventView(item = ImageItem(
        author = "author",
        authorId = "authorId",
        dateTaken = "dateTaken",
        description = "description",
        link = "link",
        urlString = "https://example.com/image.jpg",
        published = "published",
        tags = "tags",
        title = "title"
    ), onClick = {})
}


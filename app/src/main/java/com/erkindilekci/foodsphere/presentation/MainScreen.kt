package com.erkindilekci.foodsphere.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.erkindilekci.foodsphere.R
import com.erkindilekci.foodsphere.domain.RecipeModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val recipes = mainViewModel.recipes.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.27f))

                        Icon(
                            painter = painterResource(id = R.drawable.recipe),
                            contentDescription = null,
                            modifier = Modifier
                                .size(42.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "Recipes",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            )
        },
        content = {
            MainScreenContent(modifier = Modifier.padding(it), recipes = recipes)
        }
    )
}

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    recipes: LazyPagingItems<RecipeModel>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = recipes.loadState.prepend) {
            is LoadState.NotLoading -> Unit
            is LoadState.Loading -> {
                Loading()
            }

            is LoadState.Error -> {
                Error(message = state.error.message ?: "")
            }
        }
        when (val state = recipes.loadState.refresh) {
            is LoadState.NotLoading -> Unit
            is LoadState.Loading -> {
                Loading()
            }

            is LoadState.Error -> {
                Error(message = state.error.message ?: "")
            }
        }
        items(
            items = recipes,
            key = { it.id }
        ) {
            RecipeItem(recipeModel = it)
        }
        when (val state = recipes.loadState.append) {
            is LoadState.NotLoading -> Unit
            is LoadState.Loading -> {
                Loading()
            }

            is LoadState.Error -> {
                Error(message = state.error.message ?: "")
            }
        }
    }
}

@Composable
private fun RecipeItem(
    recipeModel: RecipeModel?
) {
    Spacer(modifier = Modifier.height(8.dp))

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(recipeModel?.thumbnailUrl),
                contentDescription = recipeModel?.thumbnailAltText ?: "",
                modifier = Modifier
                    .placeholder(
                        visible = recipeModel == null,
                        highlight = PlaceholderHighlight.fade(),
                    )
                    .size(72.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipeModel?.name ?: "",
                    modifier = Modifier
                        .placeholder(
                            visible = recipeModel == null,
                            highlight = PlaceholderHighlight.fade(),
                        )
                        .fillMaxWidth(),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recipeModel?.getRating() ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .placeholder(
                            visible = recipeModel == null,
                            highlight = PlaceholderHighlight.fade(),
                        )
                        .fillMaxWidth()
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

private fun LazyListScope.Loading() {
    item {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

private fun LazyListScope.Error(
    message: String
) {
    item {
        Text(
            text = message,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

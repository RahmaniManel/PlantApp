package com.example.planter_app.ui.screens.my_plants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.planter_app.R
import com.example.planter_app.appbar_and_navigation_drawer.AppBar
import com.example.planter_app.ui.screens.my_plants.plant_details.PlantDetails
import com.example.planter_app.ui.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme

object MyPlantsScreen : Screen {

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.MY_PLANTS_SCREEN_TITLE)
        val navigator = LocalNavigator.currentOrThrow

        MyPlantsScreenContent(
            paddingValues = PaddingValues(top = 0.dp),
            onClickCard = {image ->
                navigator.push(PlantDetails(
                    image,
                    comingFromMyPlants = true
                ))
            }
        )

    }
}

@Composable
fun MyPlantsScreenContent(
    comingFromPreview: Boolean? = false, paddingValues: PaddingValues,
    onClickCard: (String) -> Unit
) {
    val image1 = "https://cdn.pixabay.com/photo/2024/01/07/15/53/ai-generated-8493482_960_720.jpg"

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.leaf),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(10) {
            Images(
                image1,
                comingFromPreview!!,
                onClickCard
            )
        }
    }
}

@Composable
fun Images(
    image: String,
    comingFromPreview: Boolean,
    onClickCard: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        onClick = {
            /* TODO() */
            onClickCard(image)
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            val painter = if (!comingFromPreview) {
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = image)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(1000)
                            scale(scale = Scale.FIT)
                        }).build(),
                    //                    error = painterResource(),
                    //                    placeholder = painterResource()
                )
            } else {
                painterResource(id = R.drawable.tree_robots_11)
            }
            Image(
                painter = painter,
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
            )
            Column {
                Text(
                    text = "Disease: {X}",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                Text(
                    text = "Treatment: {asdasasdsadasdasdasdasdasd asdsad asd asd asd asd asd asd asd asdsgfsg df dgh fgh fg gfhgf hgf gfh }",
                    modifier = Modifier.padding(10.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify,
                    style = TextStyle(
                        fontSize = 16.sp,
                    )
                )

                Text(
                    text = "14 Jan 2024",
                    modifier = Modifier.padding(10.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.5f)
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun MyPlantsScreenPreview() {
    Planter_appTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    AppBar(
                        onNavigationIconClick = {},
                        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                        titleComingFromPreviews = stringResource(id = R.string.MY_PLANTS_SCREEN_TITLE)
                    )
                }
            ) { paddingValues ->
                MyPlantsScreenContent(
                    comingFromPreview = true,
                    paddingValues,
                    onClickCard = {})
            }
        }
    }
}
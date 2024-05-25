package com.example.planter_app.appbar_and_navigation_drawer


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.planter_app.R
import com.example.planter_app.ui.screens.my_plants.MyPlantsViewModel
import com.example.planter_app.ui.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    titleComingFromPreviews: String? = null
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titleComingFromPreviews ?: SettingsViewModel.appBarTitle.value,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        navigationIcon = {
            if (SettingsViewModel.appBarTitle.value != stringResource(id = R.string.SIGN_IN_SCREEN_TITLE) && SettingsViewModel.appBarTitle.value != stringResource(
                    id = R.string.LOADING_SCREEN
                )
            ) {
                IconButton(onClick = { onNavigationIconClick() }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Toggle drawer"
                    )
                }
            }

        },
        scrollBehavior = scrollBehavior,

        actions = {
            // for any action bar item... maybe notifications in future
//            if (SettingsViewModel.appBarTitle.value != stringResource(id = R.string.SIGN_IN_SCREEN_TITLE) && SettingsViewModel.appBarTitle.value != stringResource(
//                    id = R.string.LOADING_SCREEN
//                )
//            ) {
//                IconButton(
//                    onClick = { /*TODO*/ }
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.NotificationsNone,
//                        contentDescription = "NotificationsNone"
//                    )
//                }
//            }

            if (MyPlantsViewModel.displayPlantDeleteBottomSheet.value && SettingsViewModel.appBarTitle.value == stringResource(
                    id = R.string.PLANT_INFO_SCREEN_TITLE
                )
            ) {
                IconButton(
                    onClick = {
                        MyPlantsViewModel.triggerPlantDeleteBottomSheet.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Plant"
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun AppBarPreview() {
    Planter_appTheme {
        AppBar(
            onNavigationIconClick = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            titleComingFromPreviews = "App Bar"
        )
    }
}

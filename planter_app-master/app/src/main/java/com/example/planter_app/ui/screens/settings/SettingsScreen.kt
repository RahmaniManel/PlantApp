package com.example.planter_app.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.example.planter_app.MyApplication
import com.example.planter_app.R
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.example.planter_app.appbar_and_navigation_drawer.AppBar
import com.example.planter_app.shared_preferences.ThemePreferences
import com.example.planter_app.ui.screens.sign_in.SignInScreen
import com.example.planter_app.ui.screens.sign_in.SignInViewModel
import com.example.planter_app.ui.theme.Planter_appTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


object SettingsScreen : Screen {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            oneTapClient = Identity.getSignInClient(MyApplication.instance!!.applicationContext)
        )
    }

    const val normalTheme = "NormalTheme"
    const val dynamicTheme = "DynamicTheme"

    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.SETTINGS_SCREEN_TITLE)

        val navigator = LocalNavigator.currentOrThrow
        val signInScreenViewModel = viewModel<SignInViewModel>()
        val settingsViewModel = viewModel<SettingsViewModel>()

        val profilePicture = if (googleAuthUiClient.getSignedInUser()?.profilePictureURL != null) {
            googleAuthUiClient.getSignedInUser()?.profilePictureURL
        } else {
            R.drawable.guest_user_profile_dp
        }

        val userName = if (googleAuthUiClient.getSignedInUser()?.username != null) {
            googleAuthUiClient.getSignedInUser()?.username
        } else {
            "Guest User"
        }

        SettingsScreenContent(
            profilePicture,
            userName,
            onClickSignOut = {
                settingsViewModel.viewModelScope.launch {
                    googleAuthUiClient.signOut()
                    Toast.makeText(
                        MyApplication.instance!!.applicationContext,
                        "Signed out",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigator.replaceAll(SignInScreen)
                }
            },
            noSignedInUser = {
                googleAuthUiClient.getSignedInUser()?.username == null
            },
            onClickToggle = {
                val themePreferences = ThemePreferences(MyApplication.instance!!.applicationContext)
                themePreferences.saveTheme(
                    darkTheme = SettingsViewModel.darkMode.value,
                    dynamicTheme = SettingsViewModel.dynamicTheme.value
                )
            }
        )
    }
}


@Composable
fun SettingsScreenContent(
    profilePicture: Comparable<*>? = null,
    userName: String?,
    onClickSignOut: () -> Unit,
    noSignedInUser: () -> Boolean,
    paddingValesFromPreview: PaddingValues? = null,
    onClickToggle: () -> Unit
) {
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValesFromPreview?.calculateTopPadding() ?: 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (profilePicture == null) {
            Image(
                painter = painterResource(id = R.drawable.guest_user_profile_dp),
                contentDescription = "",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = profilePicture,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userName.toString(),
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier
                .padding(vertical = 20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SwitchSettings(
                            text = stringResource(id = R.string.dark_mode),
                            setting = SettingsScreen.normalTheme,
                            onClickToggle = onClickToggle
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(0.2f)
                    )

                    Row(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SwitchSettings(
                            text = stringResource(id = R.string.dynamic_theme),
                            setting = SettingsScreen.dynamicTheme,
                            onClickToggle = onClickToggle
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // sign in option only for anonymous users
            if (noSignedInUser()) {
                SignInOutButtons(
                    buttonText = stringResource(id = R.string.sign_in),
                    onClickSignInOut = { /*TODO*/ }
                )
            }

            SignInOutButtons(
                buttonText = stringResource(id = R.string.sign_out),
                onClickSignInOut = {
                    onClickSignOut()
                }
            )

        }
    }
}

@Composable
private fun SignInOutButtons(
    buttonText: String,
    onClickSignInOut: () -> Unit,
) {
    Button(
        modifier = Modifier
            .padding(bottom = 20.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 5.dp
        ),
        onClick = {
            onClickSignInOut()
        }) {
        Text(text = buttonText)
    }
}

@Composable
private fun RowScope.SwitchSettings(
    text: String,
    setting: String,
    onClickToggle: () -> Unit
) {

    Text(
        text = text,
        style = TextStyle(
            fontWeight = FontWeight.SemiBold
        ),
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize
    )
    Spacer(modifier = Modifier.weight(1f)) // Pushes the Switch to the right end

    Switch(
        checked = if (setting == SettingsScreen.normalTheme) SettingsViewModel.darkMode.value else SettingsViewModel.dynamicTheme.value,
        onCheckedChange = {
            if (setting == SettingsScreen.normalTheme) {
                SettingsViewModel.darkMode.value = !SettingsViewModel.darkMode.value
            } else {
                SettingsViewModel.dynamicTheme.value = !SettingsViewModel.dynamicTheme.value
            }
            onClickToggle()
        },
        modifier = Modifier
            .size(50.dp)
            .padding(horizontal = 16.dp),
        colors = SwitchDefaults.colors(
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun SettingsPreview() {
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
                        titleComingFromPreviews = stringResource(id = R.string.ABOUT_SCREEN_TITLE)
                    )
                }
            ) { paddingVales ->
                Spacer(modifier = Modifier.padding(top = paddingVales.calculateTopPadding()))
                SettingsScreenContent(
                    userName = "user name",
                    onClickSignOut = {},
                    noSignedInUser = { true },
                    paddingValesFromPreview = paddingVales,
                    onClickToggle = {}
                )
            }
        }
    }
}
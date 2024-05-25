package com.example.planter_app.ui.screens.sign_in

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.planter_app.MyApplication
import com.example.planter_app.R
import com.example.planter_app.firebase_login.sign_in.GoogleAuthUiClient
import com.example.planter_app.ui.screens.home.HomeScreen
import com.example.planter_app.ui.screens.settings.SettingsViewModel
import com.example.planter_app.ui.theme.Planter_appTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object SignInScreen : Screen {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            oneTapClient = Identity.getSignInClient(MyApplication.instance!!.applicationContext)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        SettingsViewModel.appBarTitle.value = stringResource(id = R.string.SIGN_IN_SCREEN_TITLE)

        val navigator = LocalNavigator.currentOrThrow

        val settingsViewModel = viewModel<SettingsViewModel>()
        val viewModel = viewModel<SignInViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()
        val loadingIcon = viewModel.loadingIcon.collectAsStateWithLifecycle()

        val context = LocalContext.current
        LaunchedEffect(key1 = state.signInError) {
            state.signInError?.let { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == ComponentActivity.RESULT_OK) {
                    viewModel.viewModelScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            intent = result.data ?: return@launch
                        )
                        viewModel.onSignInResult(signInResult)
                    }
                }
            }
        )

        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (state.isSignInSuccessful) {
                viewModel.setLoadingIcon(loading = false)
                Toast.makeText(
                    context,
                    "Sign in successful",
                    Toast.LENGTH_LONG
                ).show()
                navigator.replaceAll(HomeScreen)
                viewModel.resetState()
            }
        }

        SignInScreenContent(
            loadingIcon,
            onClickSignInWithGoogle = {
                settingsViewModel.updateConnectionStatus()
                CoroutineScope(Dispatchers.Main).launch {
//                    delay(100)
                    if (SettingsViewModel.isNetworkAvailable.value) {
                        viewModel.setLoadingIcon(loading = true)

                        viewModel.viewModelScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                }
            },
            onClickSignAsGuest = {
                settingsViewModel.updateConnectionStatus()
                CoroutineScope(Dispatchers.Main).launch {
//                    delay(100)
                    if (SettingsViewModel.isNetworkAvailable.value) {
                        viewModel.setLoadingIcon(loading = true)
                        // Call the anonymous sign-in method
                        viewModel.viewModelScope.launch {
                            val signInResult =
                                googleAuthUiClient.signInAnonymously()
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            },
            comingFromPreviews = false
        )
    }
}


@Composable
fun SignInScreenContent(
    loadingIcon: State<Boolean>,
    onClickSignInWithGoogle: () -> Unit,
    onClickSignAsGuest: () -> Unit,
    comingFromPreviews: Boolean,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
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
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            if (!comingFromPreviews) {
                val painter =
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = R.drawable.logo)
                            .apply(block = fun ImageRequest.Builder.() {
                                transformations(
                                    CircleCropTransformation(),
                                )
                                crossfade(800)
                                scale(scale = Scale.FIT)
                            }).build(),
//                    error = painterResource(id = R.drawable.logo),
//                    placeholder = painterResource(id = R.drawable.logo)
                    )

                Image(
                    painter = painter,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(width = 320.dp, height = 320.dp) // Fixed container size
                        .padding(top = 30.dp, bottom = 30.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                fontWeight = FontWeight.SemiBold
            )

            if (loadingIcon.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(top = 25.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 50.dp),
                shape = RoundedCornerShape(35.dp),
                contentPadding = PaddingValues(vertical = 20.dp, horizontal = 80.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 5.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    onClickSignInWithGoogle()
                },
                enabled = SettingsViewModel.isNetworkAvailable.value
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        modifier = Modifier
                            .height(25.dp)
                            .padding(end = 20.dp),
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "Leading Google Icon",
                    )

                    Text(text = stringResource(id = R.string.sign_in_with_google))
                }
            }


            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(0.2f)
            )
            Text(modifier = Modifier.padding(bottom = 20.dp), text = "or")


            OutlinedButton(
                modifier = Modifier
                    .padding(0.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(35.dp)
                    ),
                shape = RoundedCornerShape(35.dp),
                contentPadding = PaddingValues(vertical = 20.dp, horizontal = 80.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 5.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                onClick = {
                    onClickSignAsGuest()
                },
                enabled = SettingsViewModel.isNetworkAvailable.value
            ) {
                Text(text = stringResource(id = R.string.sign_in_as_a_guest))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp)
            ) {
                if (!SettingsViewModel.isNetworkAvailable.value) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                        text = stringResource(id = R.string.no_internet)
                    )
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
fun SignInScreenPreview() {
    Planter_appTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold { paddingVales ->
                Spacer(modifier = Modifier.padding(top = paddingVales.calculateTopPadding()))
                SignInScreenContent(
                    loadingIcon = remember { mutableStateOf(false) },
                    onClickSignInWithGoogle = { },
                    onClickSignAsGuest = {},
                    comingFromPreviews = true,
                )
            }
        }
    }
}
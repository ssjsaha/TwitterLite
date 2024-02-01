import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.twitterlite.BuildConfig
import com.example.twitterlite.R
import com.example.twitterlite.presentation.ui.HomePageEvent
import com.example.twitterlite.presentation.ui.createImageFile
import java.io.File
import java.util.Objects

@Composable
@ExperimentalMaterial3Api

fun AddPostPage(onEvent: (HomePageEvent) -> Unit, navController: NavController) {
    val context = LocalContext.current
    var isCameraPermissionGranted by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("")
    }
    val dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
    val dispatcher = dispatcherOwner?.onBackPressedDispatcher

    val file = context.createImageFile()
    var filePath by remember { mutableStateOf<String?>(null) }
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (file.length() != 0L) {
            filePath = file.path
        }
    }


    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            isCameraPermissionGranted = isGranted
            if (isGranted) {
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "You need to give camera permission", Toast.LENGTH_LONG)
                    .show()
            }
        }

    // Handle the back button press to navigate back to the "home" composable
    DisposableEffect(dispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack("main", inclusive = false)
            }
        }
        dispatcher?.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display selected image or placeholder
        if (filePath != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(filePath)
                .build(),
            contentDescription = "icon",
            contentScale = ContentScale.Inside,
            modifier =  Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(30.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Color.LightGray)
                .clickable {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )
        } else {
            Image(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(30.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Gray)
                    .clickable {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Text field for adding text
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Add Text") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {

                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (text.isBlank()) {
                    Toast.makeText(context, "Please add some text", Toast.LENGTH_LONG).show()
                    return@Button
                }
                if (filePath != null) {
                    onEvent.invoke(
                        HomePageEvent.UploadPost(
                            File(filePath!!),
                            text,
                        )
                    )
                    navController.popBackStack("main", inclusive = false)
                    Toast.makeText(context,"Your post will be shared soon",Toast.LENGTH_LONG).show()
                } else {
                    onEvent.invoke(HomePageEvent.UploadPost(null, text))
                    navController.popBackStack("main", inclusive = false)
                    Toast.makeText(context,"Your post will be shared soon",Toast.LENGTH_LONG).show()

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Upload")
        }
    }
}
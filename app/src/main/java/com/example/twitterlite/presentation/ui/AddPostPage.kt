
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twitterlite.R

@Composable
@ExperimentalMaterial3Api

fun AddPostPage(navController: NavController) {
    // You can implement your custom image picking logic here
    // For simplicity, I'm using a placeholder image
    val placeholderImage = painterResource(id = R.drawable.ic_launcher_foreground)

    var selectedImage by remember { mutableStateOf<String?>(null) }
    val dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
    val dispatcher = dispatcherOwner?.onBackPressedDispatcher

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
        Image(
            painter = if (selectedImage != null) {
                painterResource(id = R.drawable.ic_launcher_foreground) // Use your logic to load the selected image
            } else {
                placeholderImage
            },
            contentDescription = "Selected Image",
            modifier = Modifier
                .size(120.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background)
                .clickable {
                    // You can implement your image picking logic here
                    // For simplicity, I'm just updating the selected image with a placeholder
                    selectedImage = "your_image_path_or_base64_string_here"
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text field for adding text
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle text change */ },
            label = { Text("Add Text") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Handle text input done
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to confirm and close the bottom sheet
        Button(
            onClick = {
                // Handle the selected image and text
                // Close the bottom sheet
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Add")
        }
    }
}
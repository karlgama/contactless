package impacta.contactless.features.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import impacta.contactless.R


@Composable
fun LoginScreen(
    navController: NavController? = null
) {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.login_title))
        Image(
            modifier = Modifier.fillMaxSize(0.5f),
            painter = painterResource(R.drawable.login_lock_screen),
            contentDescription = stringResource(R.string.img_description_grey_lock)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LoginOptionButton("Google")
            LoginOptionButton("Facebook")
        }
    }
}

@Composable
private fun LoginOptionButton(content: String) {
    Button(
        content = { Text(text = content) },
        onClick = {
            Toast.makeText(null, "google", Toast.LENGTH_SHORT).show()
        },
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .widthIn(min = 120.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}


@Composable
@Preview(showBackground = true, widthDp = 300, heightDp = 600)
private fun PreviewSignInScreen() {
    LoginScreen()
}
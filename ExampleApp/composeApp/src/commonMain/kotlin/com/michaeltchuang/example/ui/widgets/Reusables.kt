package com.michaeltchuang.example.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaeltchuang.example.utils.Log
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

var passphraseTextField: String = ""

@Composable
fun AlgorandButton(
    stringResourceId: StringResource,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        // colors = ButtonDefaults.buttonColors(colorResource(Color.)),
        shape = RoundedCornerShape(8.dp),
        modifier =
            Modifier
                .width(190.dp)
                .height(50.dp),
    ) {
        Text(
            stringResource(resource = stringResourceId),
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
        )
    }
}

@Composable
fun AlgorandDivider() {
    Divider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier =
            Modifier
                .width(300.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassphraseField(
    label: String,
    textData: String,
) {
    var textInput by remember { mutableStateOf(textData) }
    OutlinedTextField(
        value = textInput,
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        onValueChange = {
            textInput = it
            passphraseTextField = it
        },
        label = {
            Text(
                text = label,
                color = Color.Black,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp),
    )
}

@Composable
fun ShowSnackbar(message: String) {
    Log.e("CHUANGM", message)
}

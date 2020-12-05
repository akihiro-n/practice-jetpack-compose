package com.example.practicejetpackcompose.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview

private val columnModifier = Modifier.fillMaxWidth()

@Composable
fun ErrorMessageScreen(message: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = columnModifier) {
        Text(text = message, fontSize = 16.sp, color = Color.Yellow)
    }
}

@Preview
@Composable
fun PreviewErrorMessageScreen() {
    ErrorMessageScreen(message = "Preview Error Message!")
}
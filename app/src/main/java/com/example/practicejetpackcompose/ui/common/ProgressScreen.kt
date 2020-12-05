package com.example.practicejetpackcompose.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

private val columnModifier = Modifier.fillMaxWidth()
private val indicatorModifier = Modifier.padding(8.dp)

@Composable
fun ProgressScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = columnModifier) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = indicatorModifier
        )
    }
}

@Preview
@Composable
fun PreviewProgressScreen() = ProgressScreen()
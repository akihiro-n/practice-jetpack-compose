package com.example.practicejetpackcompose.ui.common

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingImageScreen(modifier: Modifier) {
    ConstraintLayout(modifier) {
        val progress = createRef()
        ProgressScreen(
            modifier = Modifier.constrainAs(progress) {
                centerTo(parent)
            }
        )
    }
}
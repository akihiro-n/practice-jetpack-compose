package com.example.practicejetpackcompose.ui.common

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/**
 * Viewを横一列に配置
 * 横幅の最大値に達した場合は改行してから配置するカスタムレイアウト
 */
@Composable
fun FlexibleGrid(
    modifier: Modifier = Modifier.wrapContentWidth().wrapContentHeight(),
    children: @Composable () -> Unit
) {

    Layout(children, modifier) { measuarbles, constraints ->

        val maxWidth = constraints.maxWidth

        layout(constraints.maxWidth, constraints.maxHeight) {

            var width = 0
            var y = 0

            measuarbles
                .map { it.measure(constraints) }
                .forEach { placeable ->
                    width += placeable.width
                    if (width > maxWidth) {
                        width = placeable.width
                        y += placeable.height
                    }
                    placeable.place(width - placeable.width, y)
                }
        }
    }
}
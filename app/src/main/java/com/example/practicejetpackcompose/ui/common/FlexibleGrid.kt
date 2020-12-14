package com.example.practicejetpackcompose.ui.common

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlinx.coroutines.launch

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

        val placeables = measuarbles.map { it.measure(constraints) }
        val maxWidth = constraints.maxWidth
        val placeablesLastIndex = placeables.lastIndex
        // 一番高いViewに合わせる TODO: 高さを行ごとに柔軟に変えられるようにしたい
        val placeableHeight = placeables.maxOfOrNull { it.height } ?: 0
        val widths = placeables.map { it.width }

        val columnWidths = mutableListOf<Int>()
        var columnWidth = 0

        widths.forEachIndexed { index, placeableWidth ->
            columnWidth += placeableWidth
            if (columnWidth > maxWidth) {
                /**
                 * カラムの横幅を確定する
                 */
                columnWidths.add(columnWidth - placeableWidth)
                /**
                 * 次の行のカラムに横幅を追加
                 */
                columnWidth = placeableWidth
                return@forEachIndexed
            }
            if (index == placeablesLastIndex) columnWidths.add(columnWidth)
        }

        layout(
            width = columnWidths.maxOrNull() ?: 0,
            height = placeableHeight * columnWidths.size
        ) {

            var width = 0
            var y = 0

            placeables.forEach { placeable ->
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
package com.example.practicejetpackcompose.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

/**
 * 表形式で表示するカスタムレイアウト
 */
@Composable
fun Table(
    modifier: Modifier = Modifier,
    rowsCount: Int = 1,
    children: @Composable () -> Unit
) {

    Layout(children, modifier) { measuarbles, constraints ->

        val rowWidth = constraints.maxWidth / rowsCount
        val placeables = measuarbles.map { it.measure(Constraints.fixedWidth(rowWidth)) }
        val tables = placeables.chunked(rowsCount)
        val heights = tables.map { list -> list.maxOf { it.height } }

        layout(constraints.maxWidth, heights.sum()) {
            tables.forEachIndexed { columnsIndex, columns ->

                val y = heights.take(columnsIndex).sum()
                columns.forEachIndexed { index, cell -> cell.place(x = index * rowWidth, y = y) }
            }
        }
    }
}
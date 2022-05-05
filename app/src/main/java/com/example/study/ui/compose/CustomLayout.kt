package com.example.study.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Constraints

/**
 * Created by chenyy on 2022/4/21.
 */

@Composable
fun ParentLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    // 布局进行测量
    val measurePolicy = MeasurePolicy { measurables, constraints ->
        // 盒子模型
        // 1. 开始测量子视图
        val placeables = measurables.map { child ->
            // 测量宽高
            child.measure(Constraints(0, constraints.maxWidth, 0, constraints.maxHeight))
        }
        // 记录坐标
        var xPosition = 0
        var yPosition = 0
        var yMaxHeight = 0


        // 记录父视图的最大宽度和高度，不涉及测量只需循环子视图
        var maxHeight = 0
        var maxWidth = 0
        placeables.forEachIndexed { index, placeable ->
            if (placeable.height > yMaxHeight) { // 记录一行中子视图的最大高度
                yMaxHeight = placeable.height
            }
            if (xPosition + placeable.width > constraints.maxWidth) {
                maxWidth = constraints.maxWidth
                // 换行
                yPosition += yMaxHeight
                // 重置最大高度
                yMaxHeight = 0
                // 重置X坐标
                xPosition = 0
            }
            // 横向坐标增加
            xPosition += placeable.width
        }

        if (maxWidth == 0) {
            maxWidth = xPosition
        }
        if (yPosition > 0) {
            maxHeight = yPosition
        }
        xPosition = 0
        yPosition = 0
        yMaxHeight = 0
        // 2. 放置子视图
        layout(maxWidth, maxHeight, placementBlock = {
            placeables.forEachIndexed { index, placeable ->
                if (placeable.height > yMaxHeight) { // 记录一行中子视图的最大高度
                    yMaxHeight = placeable.height
                }
                if (xPosition + placeable.width > constraints.maxWidth) {
                    // 换行
                    yPosition += yMaxHeight
                    // 重置最大高度
                    yMaxHeight = 0
                    // 重置X坐标
                    xPosition = 0
                }
                placeable.placeRelative(xPosition, yPosition)
                // 横向坐标增加
                xPosition += placeable.width
            }
        })
    }
    Layout(content = content, modifier = modifier, measurePolicy = measurePolicy)
}

@Composable
fun ParentLayoutV2(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    //布局的测量策略
    val measurePolicy = MeasurePolicy { measurables, constraints ->
        //1.测量 children
        val placeables = measurables.map { child ->
            child.measure(constraints)
        }

        var xPosition = 0
        //2.放置 children
        layout(constraints.minWidth, constraints.minHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(xPosition, 0)
                xPosition += placeable.width
            }
        }
    }
    //代码分析入口
    Layout(content = content, modifier = modifier, measurePolicy = measurePolicy)
}

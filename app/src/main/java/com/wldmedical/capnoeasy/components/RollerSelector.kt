package com.wldmedical.capnoeasy.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun WheelPicker(
    items: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItems: Int = 5
) {
    val itemHeight = 48.dp
    val density = LocalDensity.current
    val visibleItemCount = visibleItems.coerceAtLeast(3)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)

    // 自动居中逻辑
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            density.run {
                val currentIndex = listState.firstVisibleItemIndex
                val offset = listState.firstVisibleItemScrollOffset
                // 修正1：通过 density 进行单位转换
                val pixelHeight = itemHeight.roundToPx()
                val targetIndex = if (offset > pixelHeight / 2) currentIndex + 1 else currentIndex

                if (targetIndex in items.indices) {
                    listState.animateScrollToItem(targetIndex)
                    onSelected(targetIndex)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .height(itemHeight * visibleItemCount)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = itemHeight * (visibleItemCount / 2)),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items.size) { index ->
                val offset = remember {
                    derivedStateOf {
                        val itemInfo = listState.layoutInfo.visibleItemsInfo
                            .firstOrNull { it.index == index }
                        itemInfo?.let {
                            (it.offset + it.size / 2) - listState.layoutInfo.viewportStartOffset
                        } ?: 0
                    }
                }

                Text(
                    text = items[index],
                    modifier = Modifier
                        .size(width = 200.dp, height = itemHeight)
                        .graphicsLayer {
                            val distanceFromCenter = abs(offset.value - listState.layoutInfo.viewportEndOffset / 2)
                            val scale = 1f - (distanceFromCenter / listState.layoutInfo.viewportEndOffset.toFloat()).coerceIn(0f, 0.5f)
                            scaleX = scale
                            scaleY = scale
                            alpha = scale.coerceIn(0.5f, 1f)
                        }
                        .clickable { onSelected(index) },
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }

        // 修正2：在 density 上下文中计算渐变参数
        val gradientEndY = with(density) {
            itemHeight.toPx() * visibleItemCount
        }
        // 顶部和底部渐变遮罩
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, Color.Transparent, Color.White),
                        startY = 0f,
                        endY = gradientEndY
                    )
                )
        )
    }
}

/**
 * App 通用滚动选择器
 * 适用于
 * 1、显示参数
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollerSelector(
    title: String = "",
) {
    var expanded = remember { mutableStateOf(false) }
    var selectedIndex = remember { mutableStateOf(0) }
    val items = listOf("Item 1", "Item 2", "Item 3")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        if(title.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        var selectedIndex = remember { mutableStateOf(0) }
        val items = listOf("Apple", "Banana", "Orange", "Mango", "Grape", "Pineapple")

        WheelPicker(
            items = items,
            selectedIndex = selectedIndex.value,
            onSelected = { selectedIndex.value = it },
            modifier = Modifier.width(200.dp)
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color(0xffDFE6E9))
                .alpha(0.4f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RollerSelectorPreview() {
    CapnoEasyTheme {
        Column {
            RollerSelector(
                title = "大气压(mmHg)",
            )
        }
    }
}
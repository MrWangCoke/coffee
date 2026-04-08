package com.mrwang.coffeeapp.presentation.screens.detailsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SelectSizeChip(
    sizeText: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. 定义核心棕色
    val coffeeBrown = Color(0xFFC67C4F)

    // 2. 根据选中状态定义颜色逻辑
    // 选中的时候：背景使用棕色的 10% 透明度 (alpha = 0.1f) 形成“淡棕色”
    val backgroundColor = if (selected) coffeeBrown.copy(alpha = 0.1f) else Color.Transparent
    // 选中的时候：边框使用纯棕色
    val borderColor = if (selected) coffeeBrown else Color(0xFFE1E1E1)
    // 选中的时候：文字使用纯棕色
    val textColor = if (selected) coffeeBrown else Color(0xFF2F2D2C)

    Box(
        modifier = modifier
            // 如果外部没传宽高度，这里作为默认值，如果传了会覆盖
            .width(80.dp)
            .height(35.dp)
            // 边框放在背景前面
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(color = backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = sizeText,
            fontSize = 16.sp,
            // 选中的时候加粗，视觉效果更好
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}
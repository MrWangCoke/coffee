package com.mrwang.coffeeapp.presentation.screens.detailsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // 👈 引入网络图片加载库
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.domain.model.Product
import com.mrwang.coffeeapp.presentation.theme.IvoryWhite

@Composable
fun ProductDetailsContent(product: Product, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(innerPadding)
    ) {
        // 1. 【核心修改】将本地 Image 替换为网络 AsyncImage
        AsyncImage(
            model = product.imageUrl ?: "", // 使用数据库里的网络链接，如果为空传空字符串
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.66f) // 完美保留你设定的宽高比
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            // 加上占位图和错误图，网络慢的时候体验更好
            placeholder = painterResource(R.drawable.banner_1),
            error = painterResource(R.drawable.banner_1)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 冰热选项和你的专属豆子 Icon (保留原样)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ice / Hot",
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(R.drawable.default_bean),
                contentDescription = "Bean",
                modifier = Modifier
                    .background(
                        color = IvoryWhite,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(36.dp)
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Description",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2. 【核心修改】处理可空的商品描述，加上 ?: 兜底
        Text(
            text = product.description ?: "No description yet.",
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Size",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        var selectedSizeText by remember { mutableStateOf("M") }

        // Size 选择器 (保留原样)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            listOf("S", "M", "L").forEach { size ->
                SelectSizeChip(
                    sizeText = size,
                    selected = selectedSizeText == size,
                    onClick = { selectedSizeText = size },
                    Modifier
                        .weight(1f)
                        .height(46.dp)
                )
            }
        }
    }
}

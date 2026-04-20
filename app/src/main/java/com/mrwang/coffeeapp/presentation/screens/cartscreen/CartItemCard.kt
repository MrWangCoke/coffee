package com.mrwang.coffeeapp.presentation.screens.cartscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mrwang.coffeeapp.domain.model.OrderItem
import com.mrwang.coffeeapp.presentation.screens.shop.ProductSelection
import com.mrwang.coffeeapp.presentation.theme.LightBrown
import com.mrwang.coffeeapp.presentation.theme.LightGray


@Composable
fun CartItemCard(
    item: OrderItem,
    selection: ProductSelection,
    isChinese: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val product = item.products
    Card(
        modifier = Modifier.fillMaxSize().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGray
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = product?.imageUrl ?: "",
                contentDescription = product?.name ?: "Product",
                contentScale = ContentScale.Crop, // 保持你原来的缩放属性
                modifier = Modifier.size(80.dp) // 保持你原来的尺寸
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            ) {
                Text(
                    product?.name ?: if (isChinese) "商品已下架" else "Product removed",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                val temperatureText = when {
                    isChinese && selection.temperature == "Hot" -> "热"
                    isChinese && selection.temperature == "Iced" -> "冰"
                    else -> selection.temperature
                }
                Text(
                    text = if (isChinese) {
                        "规格：${selection.size} / $temperatureText"
                    } else {
                        "Size: ${selection.size} / $temperatureText"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.DarkGray
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){


                IconButton (
                    onClick = onDecrease,
                    modifier = Modifier.background(
                        color = LightBrown.copy(0.1f),
                        shape = CircleShape
                    )
                        .size(24.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = LightBrown
                    )
                }
                Text(text = item.quantity.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                IconButton (
                    onClick = onIncrease,
                    modifier = Modifier.background(
                        color = LightBrown.copy(0.1f),
                        shape = CircleShape
                    )
                        .size(24.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = LightBrown
                    )
                }

                IconButton (
                    onClick = onRemove,
                    modifier = Modifier.background(
                        color = LightBrown.copy(0.1f),
                        shape = CircleShape
                    )
                        .size(24.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }



        }
    }
}

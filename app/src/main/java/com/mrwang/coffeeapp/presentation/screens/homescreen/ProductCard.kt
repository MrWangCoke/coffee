package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.domain.model.Product
import com.mrwang.coffeeapp.presentation.navigation.Routes
import com.mrwang.coffeeapp.presentation.theme.IvoryWhite
import com.mrwang.coffeeapp.presentation.theme.LightBrown
import com.mrwang.coffeeapp.presentation.theme.LightGray

//产品卡片
@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier=Modifier,
    navController: NavController,
    isFavourite: Boolean,
    onToggleFavourite: (Product) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{
                navController.navigate(Routes.DetailScreen(productId = product.id))
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGray
        ),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = 2.dp
//        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl, // 👈 使用数据库里存的真实图片网址,
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clickable { onToggleFavourite(product) }
                        .background(
                            color = LightGray.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(painter = painterResource(R.drawable.regular_outline_heart),
                        contentDescription = "Add to Favorite",
                        tint = if (isFavourite) Color.Red else LightBrown,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                style = typography.titleMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.description?: "No description",
                style = typography.bodySmall.copy(
                    color = Color.Gray
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¥${product.price}",
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = LightBrown
                    )
                )
                IconButton(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.background(
                        color = LightBrown,
                        shape = RoundedCornerShape(10.dp)
                    )
                ){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to Cart",
                        tint = IvoryWhite
                    )
                }
            }
        }
    }
}

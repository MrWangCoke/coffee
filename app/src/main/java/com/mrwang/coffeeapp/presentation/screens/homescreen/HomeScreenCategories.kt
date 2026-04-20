package com.mrwang.coffeeapp.presentation.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mrwang.coffeeapp.presentation.settings.AppLanguage

@Composable
fun HomeScreenCategories(
    categories: List<HomeCategory>,       // 👈 从外部传入分类列表
    selectedCategory: String,       // 👈 从外部传入当前选中的是谁
    language: AppLanguage,
    onCategorySelected: (String) -> Unit // 👈 当被点击时，通知外部
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(categories) { category ->
            val label = if (language == AppLanguage.Chinese) category.chineseLabel else category.englishLabel
            CategoryChip(
                text = label,
                isSelected = category.key == selectedCategory,
                onSelected = { onCategorySelected(category.key) } // 把用户的选择汇报给外面的 ViewModel
            )
        }
    }
}

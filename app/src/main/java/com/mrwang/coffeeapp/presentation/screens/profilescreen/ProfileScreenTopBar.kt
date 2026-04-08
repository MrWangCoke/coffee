package com.mrwang.coffeeapp.presentation.screens.profilescreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileScreenTopBar() {
    TopAppBar(
        title = {
            Text("Profile",
                modifier = Modifier,
                fontWeight = FontWeight.Bold
            )
        }
    )

}
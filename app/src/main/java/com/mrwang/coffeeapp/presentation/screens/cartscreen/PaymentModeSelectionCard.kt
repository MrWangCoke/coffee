package com.mrwang.coffeeapp.presentation.screens.cartscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrwang.coffeeapp.R
import com.mrwang.coffeeapp.presentation.theme.LightBrown

@Composable
fun PaymentModeSelectionCard(
    totalAmount: Double,
    isChinese: Boolean,
    enabled: Boolean = true,
    onConfirmPayment: () -> Unit = {},
    onCancelPayment: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf("Online") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val paymentModes = listOf("Online", "Cash")


    Card(
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(24.dp)
        ){

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){


                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(painter = painterResource(id = if (selectedMode=="Online") R.drawable.mobile_banking
                    else R.drawable.wallet
                    ),
                        contentDescription = "Payment Mode",
                        modifier = Modifier.size(30.dp),
                        tint = LightBrown
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column() {
                        Text(
                            text = when {
                                isChinese && selectedMode == "Online" -> "在线支付"
                                isChinese && selectedMode == "Cash" -> "现金支付"
                                else -> selectedMode
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text ="¥ ${"%.2f".format(totalAmount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightBrown
                        )
                    }

                }
                Box{
                    Icon(
                        painter = painterResource(R.drawable.regular_outline_arrow_down),
                        contentDescription = "Change Payment Mode",
                        modifier = Modifier.size(20.dp).clickable{expanded =true}
                    )
                    DropdownMenu(
                        expanded =expanded,
                        onDismissRequest = {expanded =false}
                    ) {
                        paymentModes.forEach {mode->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = when {
                                            isChinese && mode == "Online" -> "在线支付"
                                            isChinese && mode == "Cash" -> "现金支付"
                                            else -> mode
                                        },
                                        style = MaterialTheme.typography.bodyLarge)
                                },
                                onClick = {
                                    selectedMode = mode
                                    expanded =false
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(
                                            if (mode =="Online") R.drawable.mobile_banking
                                            else R.drawable.wallet
                                        ),
                                        contentDescription = null ,
                                        tint =LightBrown,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .background(
                                    color = if (selectedMode==mode) LightBrown.copy(alpha = 0.15f)
                                    else Color.Transparent
                                )
                            )
                        }
                    }
                }


            }

            Spacer(modifier = Modifier.height(16.dp))

            Button (
                onClick = { showConfirmDialog = true },
                enabled = enabled,
                modifier= Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors= ButtonDefaults.buttonColors(
                    containerColor = LightBrown,
                )
            ){
                Text(text = if (isChinese) "提交订单" else "Place Order",
                    fontSize = 18.sp
                )
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(if (isChinese) "确认支付" else "Confirm Payment") },
            text = {
                Text(
                    if (isChinese) {
                        "确认支付 ¥ ${"%.2f".format(totalAmount)} 吗？"
                    } else {
                        "Pay ¥ ${"%.2f".format(totalAmount)} now?"
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onConfirmPayment()
                    }
                ) {
                    Text(if (isChinese) "确认支付" else "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onCancelPayment()
                    }
                ) {
                    Text(if (isChinese) "取消" else "Cancel")
                }
            }
        )
    }
}

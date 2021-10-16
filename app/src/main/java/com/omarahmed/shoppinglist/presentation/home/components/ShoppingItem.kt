package com.omarahmed.shoppinglist.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.omarahmed.shoppinglist.R
import com.omarahmed.shoppinglist.data.ShoppingItem
import com.omarahmed.shoppinglist.presentation.shared.IconButton
import com.omarahmed.shoppinglist.presentation.shared.ImageWithText
import com.omarahmed.shoppinglist.presentation.ui.theme.ButtonHeight
import com.omarahmed.shoppinglist.presentation.ui.theme.LargeCornerRadius
import com.omarahmed.shoppinglist.presentation.ui.theme.SmallSpace

@Composable
fun ShoppingItem(
    shoppingItem: ShoppingItem,
) {
    var isAddedToCart by remember {
        mutableStateOf(shoppingItem.isAddedToCart)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(SmallSpace)
            .clip(
                RoundedCornerShape(
                    topStart = LargeCornerRadius,
                    bottomEnd = LargeCornerRadius
                )
            )
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        ImageWithText(item = shoppingItem)
        IconButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(bottomEnd = LargeCornerRadius))
                .background(if (isAddedToCart) Color.Red else MaterialTheme.colors.primary)
                .size(ButtonHeight),
            onClick = {
                isAddedToCart = !isAddedToCart
            },
            imageVector = if (isAddedToCart) Icons.Default.Check else Icons.Default.Add,
            contentDescription = if (isAddedToCart) stringResource(id = R.string.remove) else stringResource(
                id = R.string.add_to_cart
            )
        )
    }
}
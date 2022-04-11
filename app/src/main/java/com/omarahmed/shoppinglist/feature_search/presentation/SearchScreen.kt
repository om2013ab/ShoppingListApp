package com.omarahmed.shoppinglist.feature_search.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.omarahmed.shoppinglist.feature_list.presentation.screen_home.components.ShoppingItem
import com.omarahmed.shoppinglist.core.presentation.ui.theme.SmallSpace
import com.omarahmed.shoppinglist.core.presentation.ui.theme.SuperLargeSpace
import com.omarahmed.shoppinglist.core.presentation.util.UiEvent
import com.omarahmed.shoppinglist.feature_cart.data.entity.CartEntity
import com.omarahmed.shoppinglist.feature_cart.presentation.CartViewModel
import com.omarahmed.shoppinglist.feature_list.presentation.screen_home.HomeViewModel
import com.omarahmed.shoppinglist.feature_search.presentation.components.SearchTextField
import kotlinx.coroutines.flow.collectLatest


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    onBackClick: () -> Unit
) {
    val state = searchViewModel.state.value
    val searchQuery = searchViewModel.searchQuery.value

    LaunchedEffect(key1 = true) {
        searchViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        Column {
            SearchTextField(
                searchQuery = searchQuery.text ?: "",
                onBackClick = onBackClick
            )
            Spacer(modifier = Modifier.height(SmallSpace))
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    bottom = SuperLargeSpace,
                    start = SmallSpace,
                    end = SmallSpace,
                    top = SmallSpace
                ),
            ) {
                items(state.searchResult.size) {
                    val searchResult = state.searchResult[it]
                    ShoppingItem(shoppingItem = searchResult){
                        if (!searchResult.isAddedToCart){
                            cartViewModel.insertItem(
                                CartEntity(
                                    itemName = searchResult.name,
                                    itemIconUrl = searchResult.imageUrl ?: "",
                                    itemId = searchResult.id
                                )
                            )
                        } else {
                            cartViewModel.deleteItem(searchResult.id)
                        }

                        homeViewModel.updateItem(searchResult)
                    }
                }

            }
        }

    }
}
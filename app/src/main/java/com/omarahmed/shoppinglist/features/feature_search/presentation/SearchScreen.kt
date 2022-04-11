package com.omarahmed.shoppinglist.features.feature_search.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.omarahmed.shoppinglist.R
import com.omarahmed.shoppinglist.features.feature_list.presentation.screen_home.components.ShoppingItem
import com.omarahmed.shoppinglist.core.presentation.ui.theme.SmallSpace
import com.omarahmed.shoppinglist.core.presentation.ui.theme.SuperLargeSpace
import com.omarahmed.shoppinglist.core.presentation.ui.theme.White
import com.omarahmed.shoppinglist.core.presentation.util.UiEvent
import com.omarahmed.shoppinglist.features.feature_cart.data.entity.CartEntity
import com.omarahmed.shoppinglist.features.feature_cart.presentation.CartViewModel
import com.omarahmed.shoppinglist.features.feature_list.presentation.screen_home.HomeViewModel
import com.omarahmed.shoppinglist.features.feature_search.presentation.components.SearchTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest


@Destination
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    hideBottomNav: Boolean = true
) {
    val state by searchViewModel.state
    val searchQuery = searchViewModel.searchQuery.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        searchViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Navigate -> Unit
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        Column {
            SearchTextField(
                searchQuery = searchQuery.text,
                onSearchQueryChange = {
                    searchViewModel.onEvent(SearchEvent.EnteredQuery(it))
                    searchViewModel.onEvent(SearchEvent.Search(it))
                },
                onBackClick = { navigator.popBackStack()}
            )
            if (state.searchResult.isEmpty() && searchQuery.text.isNotEmpty()){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = White
                    )
                    Spacer(modifier = Modifier.height(SmallSpace))
                    Text(text = stringResource(R.string.no_result))
                }
            }
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
                    ShoppingItem(shoppingItem = searchResult) {
                        if (!searchResult.isAddedToCart) {
                            cartViewModel.insertItem(
                                CartEntity(
                                    itemName = searchResult.name,
                                    itemIconUrl = searchResult.imageUrl ?: "",
                                    itemId = searchResult.id
                                )
                            )
                            homeViewModel.updateItem(searchResult.id, true)
                        } else {
                            cartViewModel.deleteItem(searchResult.id)
                            homeViewModel.updateItem(searchResult.id, false)

                        }
                    }
                }
            }
        }
    }
}
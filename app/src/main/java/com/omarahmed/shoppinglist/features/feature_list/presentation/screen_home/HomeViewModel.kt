package com.omarahmed.shoppinglist.features.feature_list.presentation.screen_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.omarahmed.shoppinglist.core.data.model.ShoppingItem
import com.omarahmed.shoppinglist.core.presentation.util.UiEvent
import com.omarahmed.shoppinglist.features.destinations.LoginScreenDestination
import com.omarahmed.shoppinglist.features.feature_auth.domain.repository.AuthRepository
import com.omarahmed.shoppinglist.features.feature_cart.domain.reposirtory.CartRepo
import com.omarahmed.shoppinglist.features.feature_list.domain.repository.ShoppingListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shoppingListRepo: ShoppingListRepo,
    private val authRepo: AuthRepository,
) : ViewModel() {

    val items = shoppingListRepo.allItems.cachedIn(viewModelScope)

    private val eventChannel = Channel<UiEvent>()
    val events = eventChannel.receiveAsFlow()

    fun updateItem(itemId: String,isAddedToCart: Boolean) {
        viewModelScope.launch {
             shoppingListRepo.updateItem(
                itemId = itemId,
                isAddedToCart = isAddedToCart
            )

        }
    }

    fun logout() = viewModelScope.launch {
        authRepo.logout()
        eventChannel.send(UiEvent.Navigate(LoginScreenDestination()))
    }

}
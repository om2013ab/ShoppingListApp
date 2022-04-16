package com.omarahmed.shoppinglist.features.feature_search.presentation

sealed class SearchEvent{
    data class EnteredQuery(val query: String): SearchEvent()
}

package com.example.nasaspaceimages.domain.model

data class SearchFilters(
    val center: String? = null,
    val yearStart: Int? = null,
    val yearEnd: Int? = null
)
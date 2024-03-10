package com.example.technotestvk.data

data class FilterChip(val name: String, val isChecked: Boolean)

fun List<String>.toFilterChip() = map { FilterChip(it, false) }
package com.example.progresshabitplanner.model

data class HabitResponse(
    val id: Int,
    val name: String,
    val description: String,
    val goal: String,
    val category: Category
)

data class Category(
    val id: Int,
    val name: String,
    val iconUrl: String?
)

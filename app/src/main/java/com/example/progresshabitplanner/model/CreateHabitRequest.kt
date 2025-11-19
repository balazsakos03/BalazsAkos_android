package com.example.progresshabitplanner.model

data class CreateHabitRequest(
    val name: String,
    val description: String,
    val categoryId: Int,
    val goal: String
)

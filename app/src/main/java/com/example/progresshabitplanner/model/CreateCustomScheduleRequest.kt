package com.example.progresshabitplanner.model

data class CreateCustomScheduleRequest(
    val habitId: Int,
    val date: String,
    val start_time: String,
    val end_time: String,
    val duration_minutes: Int,
    val is_custom: Boolean = true,
    val participantIds: List<Int> = emptyList(),
    val notes: String? = null
)

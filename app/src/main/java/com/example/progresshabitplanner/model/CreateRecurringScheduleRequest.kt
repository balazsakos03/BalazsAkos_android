package com.example.progresshabitplanner.model

data class CreateRecurringScheduleRequest(
    val habitId: Int,
    val start_time: String,
    val end_time: String,
    val duration_minutes: Int,
    val repeatPattern: String,
    val repeatDays: Int,
    val is_custom: Boolean = true,
    val participantIds: List<Int> = emptyList(),
    val notes: String? = null
)

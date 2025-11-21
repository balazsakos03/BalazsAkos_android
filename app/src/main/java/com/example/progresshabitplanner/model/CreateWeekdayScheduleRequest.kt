package com.example.progresshabitplanner.model

data class CreateWeekdayScheduleRequest(
    val habitId: Int,
    val start_time: String,
    val end_time: String,
    val duration_minutes: Int,
    val daysOfWeek: List<Int>,
    val numberOfWeeks: Int,
    val participantIds: List<Int> = emptyList(),
    val notes: String? = null
)

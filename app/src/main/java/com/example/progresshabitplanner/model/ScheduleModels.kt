package com.example.progresshabitplanner.model

data class ScheduleResponse(
    val id: Int,
    val start_time: String,
    val end_time: String,
    val status: String,
    val date: String,
    val is_custom: Boolean,
    val created_at: String,
    val updated_at: String,
    val habit: ScheduleHabit,        // <-- EZ a fontos!!!
    val progress: List<ProgressItem>,
    val participants: List<ParticipantDto>,
    val type: String,
    val duration_minutes: Int,
    val is_participant_only: Boolean,
    val notes: String?
)

data class ScheduleHabit(
    val id: Int,
    val name: String,
    val description: String?,
    val goal: String?,
    val category: Category,
    val created_at: String,
    val updated_at: String
)

data class ProgressItem(
    val id: Int,
    val scheduleId: Int,
    val date: String,
    val logged_time: Int,
    val notes: String?,
    val is_completed: Boolean,
    val created_at: String,
    val updated_at: String
)

data class ParticipantDto(
    val id: Int,
    val name: String,
    val email: String,
    val profile_image: String?
)

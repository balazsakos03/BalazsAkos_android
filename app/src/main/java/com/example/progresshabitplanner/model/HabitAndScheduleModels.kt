package com.example.progresshabitplanner.model
import java.time.LocalDateTime

data class ScheduleResponse(
    val id: Int,
    val habit: HabitScheduleResponse,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val notes: String?,
    val progress: ProgressResponseDto?
)

data class HabitScheduleResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val category: HabitCategory?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val participants: List<ParticipantDto>?
)

data class HabitCategory(
    val id: Int,
    val name: String,
    val description: String?
)

data class ProgressResponseDto(
    val id: Int,
    val date: LocalDateTime?,
    val progressValue: Int
)

data class ParticipantDto(
    val id: Int,
    val username: String,
    val email: String?
)
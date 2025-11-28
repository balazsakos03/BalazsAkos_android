package com.example.progresshabitplanner.model

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val id: Int?,
    val email: String?,
    val username: String?,
    val description: String?,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,

    @SerializedName("profileImageBase64")
    val profileImageBase64: String?,

    val coverImageUrl: String?,
    val fcmToken: String?,

    // ha nem használod, nyugodtan lehet Map helyett Any?
    val preferences: Any?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
)

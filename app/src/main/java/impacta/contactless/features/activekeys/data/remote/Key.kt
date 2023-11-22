package impacta.contactless.features.activekeys.data.remote

import com.google.gson.annotations.SerializedName

data class Key(
    val id: String,
    val value: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
    )

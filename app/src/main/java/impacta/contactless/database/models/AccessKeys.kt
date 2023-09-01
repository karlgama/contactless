package impacta.contactless.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "access_keys")
data class AccessKeys(
    @PrimaryKey val id: UUID,
    @ColumnInfo(name = "first_name") val value: UUID,
    @ColumnInfo(name = "user_id") val user: UUID,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDateTime
)
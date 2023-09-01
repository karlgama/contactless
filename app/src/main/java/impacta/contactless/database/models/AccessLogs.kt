package impacta.contactless.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "access_logs")
data class AccessLogs(
    @PrimaryKey val id: UUID,
    @ColumnInfo val event: String
)
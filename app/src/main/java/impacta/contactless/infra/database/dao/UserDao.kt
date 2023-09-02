package impacta.contactless.infra.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import impacta.contactless.infra.database.models.User
import java.util.UUID

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users u WHERE u.id = :id")
    fun findById(id: UUID): User?

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}
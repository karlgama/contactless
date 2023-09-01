package impacta.contactless.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import impacta.contactless.database.dao.UserDao
import impacta.contactless.database.models.AccessKeys
import impacta.contactless.database.models.AccessLogs
import impacta.contactless.database.models.User

@Database(entities = [User::class, AccessKeys::class, AccessLogs::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "contactless.db"
            ).build()
        }
    }
}
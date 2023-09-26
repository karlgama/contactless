package impacta.contactless.infra

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import impacta.contactless.infra.database.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import impacta.contactless.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class KZAppDI {

    @Provides
    fun provideAWShttpClient(): Retrofit = Retrofit
        .Builder()
        .baseUrl(BuildConfig.AWS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideDB(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "contactless.db").build()
}
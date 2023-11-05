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
import impacta.contactless.infra.network.BaseURLInterceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

class Constants {
    fun setBaseURL(url: String) {
        kBaseURL = url
    }
    companion object {
        var kBaseURL: String = BuildConfig.AWS_BASE_URL
    }
}


@Module
@InstallIn(SingletonComponent::class)
class KZAppDI {

    @Provides
    @Singleton
    fun getChangeURLInterceptor() = BaseURLInterceptor()

    @Provides
    @Singleton
    fun baseHttpClient() = OkHttpClient.Builder()
        .addInterceptor(getChangeURLInterceptor())
        .build()

    @Provides
    fun provideAWShttpClient(): Retrofit = Retrofit
        .Builder()
        .baseUrl(Constants.kBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(baseHttpClient())
        .build()

    @Provides
    fun provideDB(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "contactless.db").build()
}
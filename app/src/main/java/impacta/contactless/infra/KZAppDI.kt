package impacta.contactless.infra

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import impacta.contactless.infra.database.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import impacta.contactless.BuildConfig
import impacta.contactless.ui.GoogleOneTapAuthenticator

@Module
@InstallIn(SingletonComponent::class)
class KZAppDI {

    @Provides
    fun provideAWShttpClient(): Retrofit = Retrofit
        .Builder()
        .baseUrl(BuildConfig.AWS_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideDB(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "contactless.db").build()

    @Provides
    fun provideOneTapClient(@ApplicationContext context: Context): SignInClient = Identity.getSignInClient(context)

    @Provides
    fun provideGoogleOneTapAuthenticator(@ApplicationContext context: Context, oneTapClient: SignInClient) = GoogleOneTapAuthenticator(context, oneTapClient)
}
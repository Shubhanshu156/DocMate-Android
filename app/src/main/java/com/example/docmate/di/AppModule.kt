package com.example.docmate.di

//import com.example.docmate.Service.APIService
import android.content.Context
import com.example.docmate.Service.APIService
import com.example.docmate.Utility.datastore.StoredToken
import com.example.docmate.data.Repositories.AuthRepository
import com.example.docmate.data.Repositories.HomeRepositry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Boolean
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideapi(storedToken: StoredToken): APIService {
        val HTTP_CONNECTION_TIMEOUT = 70L
        val HTTP_READ_TIMEOUT = 70L
        val HTTP_WRITE_TIMEOUT = 100L
        val logging = HttpLoggingInterceptor()
        if (Boolean.parseBoolean("true")) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            // We really shouldn't log anuthing if we're in release mode
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(TokenInterceptor(storedToken))
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://ktorbackend-env.eba-ptp4z4g6.us-east-1.elasticbeanstalk.com/")
            .build()
        return retrofit.create(APIService::class.java)
    }

    @Singleton
    @Provides
    fun ProvideRepo(ApiService: APIService): AuthRepository {
        return AuthRepository(ApiService)
    }
    @Singleton
    @Provides
    fun ProvideDataStore(@ApplicationContext context: Context): StoredToken {
        return StoredToken(context)
    }
}

class TokenInterceptor(private val storedToken: StoredToken) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val token = runBlocking { storedToken.getToken().first() } // Use `first()` to get the first emitted value

        val modifiedRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(modifiedRequest)
    }
}
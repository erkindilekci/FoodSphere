package com.erkindilekci.foodsphere.di

import com.erkindilekci.foodsphere.data.RecipesApiService
import com.erkindilekci.foodsphere.data.RecipesRepositoryImpl
import com.erkindilekci.foodsphere.domain.RecipesRepository
import com.erkindilekci.foodsphere.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder()
                    .header("X-RapidAPI-Key", "d256ce8a44mshd38aabe8b4270f2p147ee4jsn2af970cbf203")
                    .header("X-RapidAPI-Host", "tasty.p.rapidapi.com")
                    .build()
                it.proceed(request)
            }
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideRecipesApiService(retrofit: Retrofit): RecipesApiService = retrofit.create()

    @Singleton
    @Provides
    fun provideRecipesRepository(recipesApiService: RecipesApiService): RecipesRepository =
        RecipesRepositoryImpl(recipesApiService = recipesApiService)
}

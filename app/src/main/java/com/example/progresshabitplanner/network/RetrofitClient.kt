package com.example.progresshabitplanner.network

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.progresshabitplanner.utils.LocalDateTimeAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.time.LocalDateTime

object RetrofitClient {
    private const val BASE_URL = "https://unreined-kathleen-untutelary.ngrok-free.dev"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getInstance(context: Context): ApiService {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}

package umc.onairmate.ui.login

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import umc.onairmate.data.api.TestService

object RetrofitClient {
    private const val BASE_URL = "http://54.180.254.48:3000"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit API 서비스 생성
    val instance: TestService by lazy {
        retrofit.create(TestService::class.java)
    }
}